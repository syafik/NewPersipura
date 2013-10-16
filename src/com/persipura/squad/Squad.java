package com.persipura.squad;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.persipura.match.HasilPertandingan;
import com.webileapps.navdrawer.R;

public class Squad extends SherlockFragment {
	MyPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	public static final String TAG = Squad.class.getSimpleName();

	public static Squad newInstance() {
		return new Squad();
	}

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.news);
	// mSectionsPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
	// mViewPager = (ViewPager) findViewById(R.id.pager);
	// mViewPager.setAdapter(mSectionsPagerAdapter);
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSectionsPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args = null;
			switch (position) {
			case 0:
//				fragment = new Sejarah();
				args = new Bundle();
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position
				// + 1);
				fragment.setArguments(args);
				return fragment;
			case 1:
				fragment = new Formasi();
				args = new Bundle();
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position
				// + 1);
				fragment.setArguments(args);
				return fragment;
			case 2:
				fragment = new Pemain();
				args = new Bundle();
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position
				// + 1);
				fragment.setArguments(args);
				return fragment;
			case 3:
				fragment = new StaffAndManagement();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
						position + 1);
				fragment.setArguments(args);
				return fragment;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.sejarah).toUpperCase(l);
			case 1:
				return getString(R.string.formasi).toUpperCase(l);
			case 2:
				return getString(R.string.pemain).toUpperCase(l);
			case 3:
				return getString(R.string.staffandmanagement).toUpperCase(l);
			}
			return null;
		}

	}

	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
