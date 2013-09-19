package com.webileapps.navdrawer;


import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


//public class News extends Activity {
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.news);
//	}
//public class News extends Fragment {
//
//	public static final String TAG = News.class
//			.getSimpleName();
//
//	public static News newInstance() {
//		return new News();
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.news, container, false);
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//
//
//	}
	

public class News extends SherlockFragment {
	public static final String TAG = News.class
	.getSimpleName();

public static News newInstance() {
return new News();
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.news, container,
				false);
		
		return rootView;
	}
	
	
}
