package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class Home extends SherlockFragment {
	public static final String TAG = Home.class
	.getSimpleName();

public static Home newInstance() {
return new Home();
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home, container,
				false);
		
		return rootView;
	}
	
	
}

