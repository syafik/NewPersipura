package com.persipura.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;

public class PageSlidingNews extends SherlockFragment {
	MyPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	List<FooterBean> listFooterBean;
	LinearLayout footerLayout;
	String LinkId;
	MainActivity attachingActivityLock;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachingActivityLock = (MainActivity) activity;

	}
	public static final String TAG = PageSlidingNews.class
			.getSimpleName();

	public static PageSlidingNews newInstance() {
		return new PageSlidingNews();
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
		footerLayout = (LinearLayout) view.findViewById(R.id.outer);
		
		if(attachingActivityLock.is_tablet){
			((ViewGroup) mViewPager.getParent().getParent()).removeViewAt(1);
		}else{
			new fetchFooterFromServer().execute("");
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = null;
			Bundle args = null;
			switch (position) {
			case 0:
				fragment = new News();
				args = new Bundle();
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position
				// + 1);
				
				fragment.setArguments(args);
				break;
			case 1:
				fragment = new BeritaTransfer();
				args = new Bundle();
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position
				// + 1);
				fragment.setArguments(args);
				break;
			}
			

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Berita Terbaru".toUpperCase(l);
			case 1:
				return "Berita Transfer".toUpperCase(l);
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

	private class fetchFooterFromServer extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/footer", "");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.d("test1", "test1 : " + jsonArray);
				listFooterBean = new ArrayList<FooterBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					FooterBean thisWeekBean = new FooterBean();
					thisWeekBean.setclickable(resObject.getString("clickable"));
					thisWeekBean.setfooter_logo(resObject
							.getString("footer_logo"));
					thisWeekBean.setlink(resObject.getString("link"));
					//
					listFooterBean.add(thisWeekBean);

				}
				if (listFooterBean != null && listFooterBean.size() > 0) {
					createFooterView(listFooterBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity().getApplicationContext(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		private void createFooterView(List<FooterBean> listFooterBean)
				throws IOException {
			for (int i = 0; i < listFooterBean.size(); i++) {
				FooterBean thisWeekBean = listFooterBean.get(i);

				ImageView imgNews = (ImageView) footerLayout
						.findViewById(R.id.footerImg);

				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.staff_placeholder2x;

				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				if (!thisWeekBean.getfooter_logo().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getfooter_logo(),
							loader, imgNews);

					LinkId = null;
					LinkId = thisWeekBean.getlink();
					if (thisWeekBean.getclickable().equals("1")) {

						imgNews.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {

								Uri uri = Uri.parse(LinkId);
								Intent intent = new Intent(Intent.ACTION_VIEW,
										uri);
								startActivity(intent);

							}
						});

					}

				}

			}
			

			TextView footerTitle = (TextView) footerLayout
					.findViewById(R.id.footerText);
			footerTitle.setText("Proudly Sponsored by");
			AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
					getActivity().getApplicationContext().getAssets());	
		}

	}
}
