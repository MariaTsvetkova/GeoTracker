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

	private Switch switcher;
	private boolean isConnected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, null);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		switcher = (Switch) view.findViewById(R.id.switchInternet);
		switcher.setChecked(isConnected);

	}

	public void setNetworkConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

}
