package com.example.geotracker.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

public class JsonUtil {

	private static final String LOCATION = "com.example.geotracker.util.name.LOCATION";
	private static final String TRACKER_NAME = "com.example.geotracker.util.name.TRACKER_NAME";
	private static final String START_DATE = "com.example.geotracker.util.name.START_DATE";
	private static final String END_DATE = "com.example.geotracker.util.name.END_DATE";
	private static final String DISTANCE = "com.example.geotracker.util.name.DISTANCE";
	private static final String LONGITUDE = "com.example.geotracker.util.name.LONGITUDE";
	private static final String LATTITUDE = "com.example.geotracker.util.name.LATTITUDE";

	private static final String TAG = "myLog";
	private Context context;

	public JsonUtil(Context context) {
		this.context = context;

	}

	public boolean saveTracker(String key, Tracker tracker) {
		Log.v(TAG, "Saving tracker");
		SharedPreferences preferences;
		try {
			JSONArray locationJSONArray = new JSONArray();
			for (Location loc : tracker.getLocation()) {
				JSONObject locationJson = new JSONObject();
				locationJson.put(LONGITUDE, loc.getLongitude());
				locationJson.put(LATTITUDE, loc.getLatitude());
				locationJSONArray.put(locationJson);
			}

			JSONObject trackerJson = new JSONObject();

			trackerJson.put(LOCATION, locationJSONArray);
			trackerJson.put(TRACKER_NAME, tracker.getTrackerName());
			trackerJson.put(START_DATE, String.valueOf(tracker.getStartDate()));
			trackerJson.put(END_DATE, String.valueOf(tracker.getEndDate()));
			trackerJson.put(DISTANCE, tracker.getDistance());

			// other location data
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			SharedPreferences.Editor edit = preferences.edit();
			edit.putString(key, trackerJson.toString());
			edit.commit();
		} catch (JSONException e) {
			Log.v(TAG, "JSON Exception", e);
			return false;
		}

		Log.v(TAG,
				"Location {}  saved successfully at key: {} "
						+ preferences.getString(key, null));
		return true;
	}

	public Tracker getTrackern(String key) {
		Log.v(TAG, "Retrieving location at key {} " + key);
		try {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String json = preferences.getString(key, null);
			JSONArray jLocationArray;
			List<Location> locationList = new ArrayList();
			Location location;
			if (json != null) {
				JSONObject trackerJson = new JSONObject(json);
				Tracker tracker = new Tracker();

				// //////////////////////

				jLocationArray = trackerJson.optJSONArray(LOCATION);
				if (jLocationArray != null) {

					for (int i = 0; i < jLocationArray.length(); i++) {
						location = new Location("");
						JSONObject locationJson = jLocationArray
								.getJSONObject(i);
						location.setLongitude(locationJson.getDouble(LONGITUDE));
						location.setLatitude(locationJson.getDouble(LATTITUDE));
						locationList.add(location);

					}
				}

				// /////////////////////////

				tracker.setLocation(locationList);
				tracker.setTrackerName(trackerJson.getString(TRACKER_NAME));
				tracker.setStartDate(parseDateTime(trackerJson
						.getString(START_DATE)));
				tracker.setEndDate(parseDateTime(trackerJson
						.getString(END_DATE)));
				tracker.setDistance(trackerJson.getDouble(DISTANCE));

				// Log.v(TAG, "Returning location: {}" + location);
				return tracker;
			}
		} catch (JSONException e) {
			Log.v(TAG, "JSON Exception " + e);
		}

		Log.v(TAG, "No location found at key {} " + key);
		// or throw exception depending on your logic
		return null;
	}

	public double getDistance(double lat1, double lon1, double lat2, double lon2) {
		double r = 6371; // km
		double dLat = (lat2 - lat1) * (Math.PI / 180);
		double dLon = (lon2 - lon1) * (Math.PI / 180);
		lat1 = lat1 * (Math.PI / 180);
		lat2 = lat2 * (Math.PI / 180);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = r * c;

		return d;

	}

	public static Date parseDateTime(String dateString) {
		DateFormat fmt;
		if (dateString.endsWith("Z")) {
			fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		} else {
			fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		}
		Date date = new Date();
		try {
			date = fmt.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

}
