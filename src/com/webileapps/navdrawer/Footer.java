package com.webileapps.navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class Footer extends SherlockFragment {
	public static final String TAG = Footer.class
	.getSimpleName();

public static Footer newInstance() {
return new Footer();
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.footer, container,
				false);
		
		return rootView;
	}
	
	
}

