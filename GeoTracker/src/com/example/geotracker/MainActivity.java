package com.example.geotracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geotracker.fragments.HistoryFragment;
import com.example.geotracker.fragments.HomeFragment;
import com.example.geotracker.fragments.TrackWorkoutFragment;
import com.example.geotracker.fragments.TrackWorkoutListener;
import com.example.geotracker.util.JsonUtil;
import com.example.geotracker.util.Tracker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements OnClickListener,
		TrackWorkoutListener, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {

	private Button btnHome;
	private Button btnHistory;
	private Button btnTrackWorkout;
	private TextView tvBtnAction;

	// ////////////////////////////

	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	private Location mLastLocation;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	// boolean flag to toggle periodic location updates
	private boolean mRequestingLocationUpdates = false;

	private LocationRequest mLocationRequest;

	// Location updates intervals in sec
	private static int UPDATE_INTERVAL = 10000; // 10 sec
	private static int FATEST_INTERVAL = 5000; // 5 sec
	private static int DISPLACEMENT = 10; // 10 meters

	// ////////////////////////////

	private Tracker tracker;
	private JsonUtil jSonUtil;
	private String trackerName;
	private List<Location> locationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnHome = (Button) findViewById(R.id.btnHome);
		btnHistory = (Button) findViewById(R.id.btnHistory);
		btnTrackWorkout = (Button) findViewById(R.id.btnTrackWorkout);
		tvBtnAction = (TextView) findViewById(R.id.tvBtnAction);
		btnHistory.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		btnTrackWorkout.setOnClickListener(this);

		if (checkPlayServices()) {

			buildGoogleApiClient();

			createLocationRequest();
		}

	}

	@Override
	public void onClick(View v) {
		tvBtnAction.setText(((Button) v).getText().toString());
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		switch (v.getId()) {
		case R.id.btnHome:
			HomeFragment fragmentHome = new HomeFragment();
			fragmentHome.setNetworkConnected(mGoogleApiClient.isConnected());
			fragmentTransaction.replace(R.id.fragmentContainer, fragmentHome);
			fragmentTransaction.commit();

			break;
		case R.id.btnTrackWorkout:

			TrackWorkoutFragment fragmentWorkout = new TrackWorkoutFragment();
			fragmentTransaction
					.replace(R.id.fragmentContainer, fragmentWorkout);
			fragmentTransaction.commit();
			fragmentWorkout.setListener(this);

			break;
		case R.id.btnHistory:
			HistoryFragment fragmentHistory = new HistoryFragment();
			fragmentTransaction
					.replace(R.id.fragmentContainer, fragmentHistory);
			fragmentTransaction.commit();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		checkPlayServices();

		if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocationUpdates();
	}

	private void displayLocation() {

		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			double latitude = mLastLocation.getLatitude();
			double longitude = mLastLocation.getLongitude();

			Toast.makeText(getApplicationContext(),
					latitude + ", " + longitude, Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(
					getApplicationContext(),
					"(Couldn't get the location. Make sure location is enabled on the device)",
					Toast.LENGTH_LONG).show();
		}
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Starting the location updates
	 * */
	protected void startLocationUpdates() {

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
		locationList.add(mLastLocation);

	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {

		displayLocation();

		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;

		Toast.makeText(getApplicationContext(), "Location changed!",
				Toast.LENGTH_SHORT).show();

		displayLocation();
	}

	@Override
	public void startGPSRecord(String trackerName) {

		Toast.makeText(getApplicationContext(),
				"the tracker name is " + trackerName, Toast.LENGTH_LONG).show();

		// displayLocation();
		this.trackerName = trackerName;
		togglePeriodicLocationUpdates();

	}

	private void togglePeriodicLocationUpdates() {
		Button btnStartLocationRecord = (Button) findViewById(R.id.btnStartTracking);
		if (!mRequestingLocationUpdates) {
			tracker = new Tracker();
			tracker.setTrackerName(trackerName);
			tracker.setStartDate(new Date());

			locationList = new ArrayList();

			trackerName = "";
			// Changing the button text
			btnStartLocationRecord.setText("Stop record");
			mRequestingLocationUpdates = true;

			startLocationUpdates();

			Log.d(TAG, "Periodic location updates started!");

		} else {

			btnStartLocationRecord.setText("Start record");
			tracker.setLocation(locationList);
			tracker.setEndDate(new Date());

			mRequestingLocationUpdates = false;

			// Stopping the location updates
			stopLocationUpdates();

			Log.d(TAG, "Periodic location updates stopped!");
		}
	}

}
