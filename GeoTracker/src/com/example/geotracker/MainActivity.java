package com.example.geotracker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.geotracker.fragments.TrackWorkoutFragment;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
}
