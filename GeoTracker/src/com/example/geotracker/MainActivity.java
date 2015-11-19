package com.example.geotracker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geotracker.fragments.HistoryFragment;
import com.example.geotracker.fragments.HomeFragment;
import com.example.geotracker.fragments.TrackWorkoutFragment;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnHome;
	private Button btnHistory;
	private Button btnTrackWorkout;
	private TextView tvBtnAction;

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case R.id.action_home:
			Toast.makeText(this, "Refresh home", Toast.LENGTH_SHORT).show();
			break;

		case R.id.action_track:
			Toast.makeText(this, "Settings track", Toast.LENGTH_SHORT).show();

			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();

			TrackWorkoutFragment fragmentWorkout = new TrackWorkoutFragment();
			fragmentTransaction.add(R.id.fragmentContainer, fragmentWorkout);
			fragmentTransaction.commit();

			break;

		case R.id.action_history:
			Toast.makeText(this, "Settings history", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		tvBtnAction.setText(((Button) v).getText().toString());
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		switch (v.getId()) {
		case R.id.btnHome:
			HomeFragment fragmentHome = new HomeFragment();
			fragmentTransaction.replace(R.id.fragmentContainer, fragmentHome);
			fragmentTransaction.commit();

			break;
		case R.id.btnTrackWorkout:

			TrackWorkoutFragment fragmentWorkout = new TrackWorkoutFragment();
			fragmentTransaction
					.replace(R.id.fragmentContainer, fragmentWorkout);
			fragmentTransaction.commit();

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

}
