package com.example.geotracker.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

public class JsonUtil {

	private static final String LATITUDE = "com.example.geotracker.util.name.LATITUDE";
	private static final String LONGITUDE = "com.example.geotracker.util.name.LONGITUDE";
	private static final String TAG = "myLog";
	private Context context;

	public JsonUtil(Context context) {
		this.context = context;
	}

	/**
	 * Save a location/key pair.
	 * 
	 * @param key
	 *            the key associated with the location
	 * @param location
	 *            the location for the key
	 * @return true if saved successfully false otherwise
	 */
	public boolean saveLocation(String key, Location location) {
		Log.v(TAG, "Saving location");
		SharedPreferences preferences;
		try {
			JSONObject locationJson = new JSONObject();

			locationJson.put(LATITUDE, location.getLatitude());
			locationJson.put(LONGITUDE, location.getLongitude());
			// other location data
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			SharedPreferences.Editor edit = preferences.edit();
			edit.putString(key, locationJson.toString());
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

	/**
	 * Gets location data for a key.
	 * 
	 * @param key
	 *            the key for the saved location
	 * @return a {@link Location} object or null if there is no entry for the
	 *         key
	 */
	public Location getLocation(String key) {
		Log.v(TAG, "Retrieving location at key {} " + key);
		try {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String json = preferences.getString(key, null);

			if (json != null) {
				JSONObject locationJson = new JSONObject(json);
				Location location = new Location("");
				location.setLatitude(locationJson.getInt(LATITUDE));
				location.setLongitude(locationJson.getInt(LONGITUDE));
				Log.v(TAG, "Returning location: {}" + location);
				return location;
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

}
