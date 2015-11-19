package com.example.geotracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geotracker.R;

public class TrackWorkoutFragment extends Fragment implements OnClickListener {

	private TextView tvTrackerName;
	private EditText etTrackerName;
	private String trackerName = "";
	private Button btnStopTracking;
	private Button btnStartTracking;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.track_workout, null);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tvTrackerName = (TextView) getActivity().findViewById(
				R.id.tvTrackerName);
		etTrackerName = (EditText) getActivity().findViewById(
				R.id.etTrackerName);
		btnStartTracking = (Button) getActivity().findViewById(
				R.id.btnStartTracking);
		btnStopTracking = (Button) getActivity().findViewById(
				R.id.btnStopTracking);
		btnStartTracking.setOnClickListener(this);
		btnStopTracking.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		trackerName = etTrackerName.getText().toString();

		switch (v.getId()) {
		case R.id.btnStartTracking:
			etTrackerName.setText("");
			etTrackerName.setVisibility(View.GONE);
			tvTrackerName.setText("tracking " + trackerName);
			Toast.makeText(getActivity(), "tracking name" + trackerName,
					Toast.LENGTH_LONG).show();
			break;
		case R.id.btnStopTracking:
			tvTrackerName.setText("Its saved " + trackerName);
			etTrackerName.setVisibility(View.VISIBLE);

			break;
		default:
			break;
		}

	}

}
