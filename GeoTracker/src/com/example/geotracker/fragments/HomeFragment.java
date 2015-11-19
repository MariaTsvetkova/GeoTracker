package com.example.geotracker.fragments;

import com.example.geotracker.R;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, null);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Switch switcher = (Switch) view.findViewById(R.id.switchInternet);

		if (!isNetworkConnected(getActivity())) {
			switcher.setChecked(false);
		} else {
			switcher.setChecked(true);
		}

	}

	public boolean isNetworkConnected(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();

		if (netinfo != null && netinfo.isConnectedOrConnecting()) {
			android.net.NetworkInfo wifi = cm
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			android.net.NetworkInfo mobile = cm
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if ((mobile != null && mobile.isConnectedOrConnecting())
					|| (wifi != null && wifi.isConnectedOrConnecting()))
				return true;
			else
				return false;
		} else
			return false;
	}

}
