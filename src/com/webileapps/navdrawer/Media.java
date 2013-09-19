package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class Media extends SherlockFragment {
	public static final String TAG = News.class
	.getSimpleName();

public static Media newInstance() {
return new Media();
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.media, container,
				false);
		
		return rootView;
	}
	
	
}

