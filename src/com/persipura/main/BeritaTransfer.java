package com.persipura.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.persipura.bean.AdsBean;
import com.persipura.bean.FooterBean;
import com.persipura.bean.NewsBean;
import com.persipura.utils.*;
import com.persipura.main.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BeritaTransfer extends SherlockFragment {
	// private static String url =
	// "http://prspura.tk/restapi/get/news?limit=20&offset=1";
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	List<FooterBean> listFooterBean;
	List<AdsBean> listAdsBean;
	LinearLayout lifePageCellContainerLayout;
	FrameLayout footerLayout;
	ViewGroup newContainer;
	String nid;
	String LinkId;
	private ProgressDialog progressDialog;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	int hitung = 15;
	int offset = 15;
	int failedRetrieveCount = 0;

	MainActivity attachingActivityLock;

	public static final String TAG = BeritaTransfer.class.getSimpleName();

	public static BeritaTransfer newInstance() {
		return new BeritaTransfer();
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
	    }
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		// new fetchLocationFromServer().execute("");
		  

//		new fetchFooterFromServer().execute("");
		
		View rootView = inflater.inflate(R.layout.news, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;

		Integer[] param = new Integer[] { hitung, 0 };
		new fetchLocationFromServer().execute(param);

		mPullRefreshScrollView = (PullToRefreshScrollView) rootView
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// new GetDataTask().execute();
						if(refreshView.getheaderScroll() < 0){
//							showProgressDialog();
							lifePageCellContainerLayout.removeAllViews();

							Integer[] param = new Integer[] { hitung, 0 };
							new fetchLocationFromServer().execute(param);
							new fetchAdsFromServer().execute("");
						}else{
							Integer[] param = new Integer[] { hitung, offset };
							new fetchLocationFromServer().execute(param);
							offset = offset + 15;
	
						}
						
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
//		footerLayout = (FrameLayout) rootView
//				.findViewById(R.id.bottom_control_bar);
//
//		TextView footerTitle = (TextView) rootView
//				.findViewById(R.id.footerText);
//		AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
//				attachingActivityLock.getApplicationContext().getAssets());
		new fetchAdsFromServer().execute("");
		return rootView;
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(attachingActivityLock);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private class fetchLocationFromServer extends
			AsyncTask<Integer, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Integer... param) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/news", "category=berita-transfer" + "&limit=" + param[0] + "&offset="
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

				listThisWeekBean = new ArrayList<NewsBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					NewsBean thisWeekBean = new NewsBean();
					thisWeekBean.setNid(resObject.getString("nid"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setteaser(resObject.getString("teaser"));
					thisWeekBean.setimg_uri(resObject.getString("img_uri"));
					thisWeekBean.setcreated(resObject.getString("created"));
					

					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				} else {
					offset = offset - 15;
					mPullRefreshScrollView.onRefreshComplete();
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;

			}
		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<NewsBean> listThisWeekBean) throws IOException {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				NewsBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(R.layout.news_list,
						null);
				TextView titleNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView descNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);
				TextView cellnumTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_cellnum);
				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				titleNews.setText("");
				descNews.setText("");
				cellnumTextView.setText("");
				nid = null;
				nid = thisWeekBean.getNid();

				cellViewMainLayout.setTag(nid);
				Log.d("NewsId", "NewsId : " + cellViewMainLayout.getTag());

				titleNews.setText(thisWeekBean.gettitle());
				descNews.setText(Html.fromHtml(thisWeekBean.getcreated()));
				
				AppConstants.fontrobotoTextViewBold(titleNews, 13, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.staff_placeholder2x;
				ImageLoader imgLoader = new ImageLoader(attachingActivityLock
						.getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getimg_uri(), loader,
						imgNews);
				
				
				View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {
						SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(attachingActivityLock);
						Editor editor = mPrefs.edit();
						
						editor.putString("currentFragment", DetailNews.TAG);
						editor.putString("prevFragment", PageSlidingNews.TAG);
						editor.commit();
//						
						Bundle data = new Bundle();
						data.putString("NewsId", (String) v.getTag());
						data.putString("FragmentTag", PageSlidingNews.TAG);
						FragmentTransaction t = getActivity()
								.getSupportFragmentManager().beginTransaction();
						DetailNews mFrag = new DetailNews();
						mFrag.setArguments(data);
						t.add(R.id.content, mFrag, DetailNews.TAG).commit();

					}
				};
				mPullRefreshScrollView.onRefreshComplete();
				cellViewMainLayout.setOnClickListener(myhandler1);

				lifePageCellContainerLayout.addView(cellViewMainLayout);

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

					int screenSize = attachingActivityLock.getApplicationContext().getResources().getConfiguration().screenLayout
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

				// Collections.sort(listAdsBean, new CustomComparator());
				if(progressDialog != null){
					progressDialog.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}
			
			if(failedRetrieveCount >0){
				if(progressDialog != null){
					progressDialog.dismiss();
					Toast.makeText(attachingActivityLock.getApplicationContext(),
							"Failed to retrieve data from server",
							Toast.LENGTH_LONG).show();
				}
			}

		}

		private void createAdsView(List<AdsBean> listAdsBean)
				throws IOException {
			int counter = 3; // will use API ads
			
			int newCount;
			Log.d("listAdsBean.size()", "listAdsBean.size() : " + listAdsBean.size());
			
			
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

				ImageLoader imgLoader = new ImageLoader(attachingActivityLock
						.getApplicationContext());

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
				Log.d("lifePageCellContainerLayout.size()", "lifePageCellContainerLayout.size() : " + lifePageCellContainerLayout.getChildCount());
				Log.d("lifePageCellContainerLayout.size()", "counter : " + newCount);
				
				if(newCount >= lifePageCellContainerLayout.getChildCount()){
					newCount = lifePageCellContainerLayout.getChildCount();
				}
				
				lifePageCellContainerLayout.addView(cellViewMainLayout, newCount);

			}
		}

	}

	public class CustomComparator implements Comparator<AdsBean> {
		@Override
		public int compare(AdsBean o1, AdsBean o2) {
			return o2.getad_rank().compareTo(o1.getad_rank());
		}
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "onDestroyCalled");
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d("onPause", "onPauseCalled");
		if (progressDialog != null){
			progressDialog.dismiss();
		
		}
	}
}