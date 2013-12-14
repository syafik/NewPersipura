package com.persipura.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.persipura.bean.AdsBean;
import com.persipura.bean.mediaBean;
import com.persipura.main.MainActivity;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
//import com.persipura.main.R;
import com.persipura.main.R;

public class videoTerbaru extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout videoContainer;
	ViewGroup newContainer;
	String nid;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	List<AdsBean> listAdsBean;
	MainActivity attachingActivityLock;
	String LinkId;

	int hitung = 15;
	int offset = 15;

	public static final String TAG = videoTerbaru.class.getSimpleName();

	public static videoTerbaru newInstance() {
		return new videoTerbaru();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachingActivityLock = (MainActivity) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		attachingActivityLock = null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// setRetainInstance(true);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.video, container, false);
		newContainer = container;
		mInflater = getLayoutInflater(savedInstanceState);

		videoContainer = (LinearLayout) rootView
				.findViewById(R.id.parent_video);

		Integer[] param = new Integer[] { hitung, 0 };
		new fetchLocationFromServer().execute(param);

		mPullRefreshScrollView = (PullToRefreshScrollView) rootView
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						if (refreshView.getheaderScroll() < 0) {
							videoContainer.removeAllViews();

							Integer[] param = new Integer[] { hitung, 0 };
							new fetchLocationFromServer().execute(param);
							new fetchAdsFromServer().execute("");
						} else {
							Integer[] param = new Integer[] { hitung, offset };
							new fetchLocationFromServer().execute(param);
							offset = offset + 10;
						}
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

		rootView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				// hideProgressDialog();
			}

		});
		MainActivity.getInstance().HideOtherActivities();
		new fetchAdsFromServer().execute("");
		return rootView;
	}

	private class fetchLocationFromServer extends
			AsyncTask<Integer, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Integer... param) {

			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/video", "limit=" + param[0] + "&offset="
							+ param[1]);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<mediaBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					mediaBean thisWeekBean = new mediaBean();
					thisWeekBean.setId(resObject.getString("id"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setvideo_image(resObject
							.getString("video_image"));
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				} else {
					offset = offset - 10;
					mPullRefreshScrollView.onRefreshComplete();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<mediaBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				mediaBean thisWeekBean = listThisWeekBean.get(i);

				View cellViewMainLayout = mInflater.inflate(
						R.layout.video_list, null);
				TextView title = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView created = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);
				ImageView img = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				AppConstants.fontrobotoTextView(created, 11, "A6A5A2",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(title, 15, "ffffff",
						getActivity().getApplicationContext().getAssets());

				title.setText("");
				created.setText("");
				nid = null;
				nid = thisWeekBean.getId();
				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				int loader = R.drawable.staff_placeholder2x;
				ImageLoader imageLoader = new ImageLoader(getActivity()
						.getApplicationContext());
				img.setTag(thisWeekBean.getvideo_image());
				imageLoader.DisplayImage(thisWeekBean.getvideo_image(),
						loader, img);

				videoContainer.addView(cellViewMainLayout);
				cellViewMainLayout.setTag(nid);
				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						// videoPlayer vp = new videoPlayer();
						//
						// Bundle b = new Bundle();
						//
						// b.putString("myString",(String) v.getTag());
						// vp.setArguments(b);
						// getActivity().getSupportFragmentManager().beginTransaction()
						// .add(R.id.parentpager, vp,videoPlayer.TAG)
						// .addToBackStack("")
						// .commit();
						SharedPreferences mPrefs = PreferenceManager
								.getDefaultSharedPreferences(getActivity());
						Editor editor = mPrefs.edit();
						editor.putString("prevFragment", pageSliding.TAG);
						editor.putString("currentFragment", videoPlayer.TAG);
						
						videoPlayer vp = new videoPlayer();
						Bundle b = new Bundle();
						b.putString("myString",(String) v.getTag());
						vp.setArguments(b);	
						
						editor.commit();
						getActivity().getSupportFragmentManager()
						.beginTransaction()
//						.remove(vp)
						.add(R.id.content, vp, videoPlayer.TAG)
						.commit();

						// Bundle data = new Bundle();
						// data.putString("NewsId", (String) v.getTag());
						//
						// FragmentTransaction t = getFragmentManager()
						// .beginTransaction();
						// DetailNews mFrag = new DetailNews();
						// mFrag.setArguments(data);
						// t.add(R.id.content, mFrag, DetailNews.TAG).commit();
					}
				});
				mPullRefreshScrollView.onRefreshComplete();

			}
		}

	}

	private class fetchAdsFromServer extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/ads", "ad_location=berita_terbaru");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.d("test1", "result ads : " + jsonArray);
				listAdsBean = new ArrayList<AdsBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					AdsBean thisWeekBean = new AdsBean();
					thisWeekBean.setclickable(resObject.getString("clickable"));
					thisWeekBean.setad_rank(resObject.getString("ad_rank"));
					thisWeekBean.setlink(resObject.getString("ad_link"));

					int screenSize = attachingActivityLock
							.getApplicationContext().getResources()
							.getConfiguration().screenLayout
							& Configuration.SCREENLAYOUT_SIZE_MASK;

					switch (screenSize) {
					case Configuration.SCREENLAYOUT_SIZE_LARGE:
						thisWeekBean.setimage(resObject
								.getString("ad_image_ldpi"));
						break;
					case Configuration.SCREENLAYOUT_SIZE_NORMAL:
						thisWeekBean.setimage(resObject
								.getString("ad_image_hdpi"));
						break;
					case Configuration.SCREENLAYOUT_SIZE_SMALL:
						thisWeekBean.setimage(resObject
								.getString("ad_image_mdpi"));
						break;
					default:
						thisWeekBean.setimage(resObject
								.getString("ad_image_xhdpi"));
					}

					thisWeekBean.setclickcounter(resObject
							.getString("clickcounter"));

					//
					listAdsBean.add(thisWeekBean);

				}
				Collections.sort(listAdsBean, new Comparator<AdsBean>() {
					@Override
					public int compare(AdsBean o1, AdsBean o2) {
						Log.d("o1", "o1 obj :" + o1);
						Log.d("o2", "o2 obj :" + o2);
						return o1.getad_rank().compareToIgnoreCase(
								o2.getad_rank());
					}
				});

				if (listAdsBean != null && listAdsBean.size() > 0) {
					createAdsView(listAdsBean);
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		private void createAdsView(List<AdsBean> listAdsBean)
				throws IOException {
			int counter = 3; // will use API ads

			int newCount;
			Log.d("listAdsBean.size()",
					"listAdsBean.size() : " + listAdsBean.size());

			for (int i = 0; i < listAdsBean.size(); i++) {
				AdsBean thisWeekBean = listAdsBean.get(i);

				View cellViewMainLayout = mInflater.inflate(R.layout.ads_list,
						null);

				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.ads_img);

				imgNews.setVisibility(View.VISIBLE);
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.ads2x;

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				if (!thisWeekBean.getimage().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getimage(), loader,
							imgNews);

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
				newCount = counter * (i + 1) + i;
				Log.d("lifePageCellContainerLayout.size()",
						"lifePageCellContainerLayout.size() : "
								+ videoContainer.getChildCount());
				Log.d("lifePageCellContainerLayout.size()", "counter : "
						+ newCount);

				if (newCount >= videoContainer.getChildCount()) {
					newCount = videoContainer.getChildCount();
				}

				videoContainer.addView(cellViewMainLayout, newCount);

			}
		}

	}

	public class CustomComparator implements Comparator<AdsBean> {
		@Override
		public int compare(AdsBean o1, AdsBean o2) {
			return o2.getad_rank().compareTo(o1.getad_rank());
		}
	}

}
