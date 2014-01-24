package com.persipura.socialize;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.application.app.utility.Constants;
import com.application.app.utility.TwitterSession;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.persipura.bean.AdsBean;
import com.persipura.bean.FooterBean;
import com.persipura.bean.NewsBean;
import com.persipura.bean.TweetBean;
import com.persipura.home.Home;
import com.persipura.main.MainActivity;
import com.persipura.utils.*;
import com.persipura.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Stream extends SherlockFragment {
	// private static String url =
	// "http://prspura.tk/restapi/get/news?limit=20&offset=1";
	private LayoutInflater mInflater;
	List<TweetBean> listThisWeekBean;
	List<FooterBean> listFooterBean;
	List<AdsBean> listAdsBean;
	List<twitter4j.Status> statuses;
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
	EditText tweetText;
	LinearLayout closeParentTweet;
	LinearLayout openParentTweet;
	ArrayList<String> stringKeywordList = new ArrayList<String>();
	ArrayList<String> stringKeywordsPost = new ArrayList<String>();

	MainActivity attachingActivityLock;
	Twitter twitter;
	String keywords;
	TextView labelLimit;
	QueryResult results;

	public static final String TAG = Stream.class.getSimpleName();

	public static Stream newInstance() {
		return new Stream();
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
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		showProgressDialog();
		

		View rootView = inflater.inflate(R.layout.stream, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		
		Integer[] param = new Integer[] { hitung, 1 };
		new fetchLocationFromServer().execute(param);

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		if(attachingActivityLock.is_tablet){
			footerLayout.setBackgroundColor(Color.parseColor("#2E2C2C"));
		}else{
			new fetchFooterFromServer().execute("");
		}
//		new fetchFooterFromServer().execute("");
		Button send = (Button) rootView.findViewById(R.id.sendTweet);
		Button cancel = (Button) rootView.findViewById(R.id.cancelTweet);
		tweetText = (EditText) rootView.findViewById(R.id.tweetText);
		openParentTweet = (LinearLayout) rootView.findViewById(R.id.openTweet);
		closeParentTweet = (LinearLayout) rootView
				.findViewById(R.id.closeTweet);
		labelLimit = (TextView) rootView.findViewById(R.id.labelLimit);
		Button closeTweet = (Button) rootView.findViewById(R.id.btnCloseTweet);
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
		builder.setDebugEnabled(true);
		// Access Token
		TwitterSession twitterSession = attachingActivityLock.twitterSession;
		String access_token = twitterSession.getDefaultAccessToaken();
		// Access Token Secret
		String access_token_secret = twitterSession.getDefaultSecret();
		AccessToken accessToken = new AccessToken(access_token,
				access_token_secret);
		twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
		
		String keywords_result = WebHTTPMethodClass
				.httpGetServiceWithoutparam("/restapi/get/post_keys");
		JSONArray keywordsjsonArray;
		try {
			keywordsjsonArray = new JSONArray(keywords_result);
			for (int i = 0; i < keywordsjsonArray.length(); i++) {
				JSONObject resObject = keywordsjsonArray.getJSONObject(i);
				stringKeywordsPost.add(resObject.getString("key"));

			}
			keywords = "";
			for (String string : stringKeywordsPost) {
				keywords = keywords + " " + string;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputFilter[] FilterArray = new InputFilter[1];
		int limit = 140 - keywords.toString().length()
				- tweetText.getText().length() - 1;
		FilterArray[0] = new InputFilter.LengthFilter(limit);
		tweetText.setFilters(FilterArray);
		labelLimit.setText(Integer.toString(limit));

		tweetText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				InputFilter[] FilterArray = new InputFilter[1];
				int limit = 140 - keywords.toString().length()
						- tweetText.getText().length() - 1;
				Log.d("limitText", "limitText : " + limit);
//				FilterArray[0] = new InputFilter.LengthFilter(limit);
//				tweetText.setFilters(FilterArray);
				labelLimit.setText(Integer.toString(limit));
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		closeTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tweetText.setText("");
				openParentTweet.setVisibility(View.VISIBLE);
				closeParentTweet.setVisibility(View.GONE);

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openParentTweet.setVisibility(View.GONE);
				closeParentTweet.setVisibility(View.VISIBLE);
				tweetText.setText("");

			}
		});
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tweet = tweetText.getText().toString();
				Log.d("tweet", "tweet this : " + tweet);

				// Update status
				twitter4j.Status response;
				try {

					response = twitter.updateStatus(tweet + " " + keywords);
					tweetText.setText("");
					Log.d(TAG, "Response Text=>" + response.getText());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		
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

							Integer[] param = new Integer[] { hitung, 1 };
							new fetchLocationFromServer().execute(param);
						}else{
							Integer[] param = new Integer[] { hitung, offset };
							new fetchLocationFromServer().execute(param);
							offset = offset + 15;
	
						}
						
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();


		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
				attachingActivityLock.getApplicationContext().getAssets());

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
			// String result = WebHTTPMethodClass.httpGetService(
			// "/restapi/get/news", "limit=" + param[0] + "&offset="
			// + param[1]);
			String result = null;

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
			builder.setDebugEnabled(true);
			// Access Token
			TwitterSession twitterSession = attachingActivityLock.twitterSession;
			String access_token = twitterSession.getDefaultAccessToaken();
			// Access Token Secret
			String access_token_secret = twitterSession.getDefaultSecret();
			AccessToken accessToken = new AccessToken(access_token,
					access_token_secret);
			Twitter twitter = new TwitterFactory(builder.build())
					.getInstance(accessToken);

			try {
				String keywords_result = WebHTTPMethodClass
						.httpGetServiceWithoutparam("/restapi/get/filter_keys");
				try {
					JSONArray keywordsjsonArray = new JSONArray(keywords_result);
					for (int i = 0; i < keywordsjsonArray.length(); i++) {
						JSONObject resObject = keywordsjsonArray
								.getJSONObject(i);
						stringKeywordList.add(resObject.getString("key"));

					}
					String keywords = "";
					for (String string : stringKeywordList) {
						keywords = keywords + "," + string;
					}

					Query query = new Query(keywords);
					query.setCount(20);
					
					if (param[1] == 1) {
						Log.d("masuk", "masuk1 ea");
						results = twitter.search(query);
						statuses = results.getTweets();
						result = statuses.toString();
					} else {
						Log.d("masuk", "masuk2 ea");
						result = results.nextQuery().toString();
						
						 query=results.nextQuery();
				          if(query!=null){
				        	results = twitter.search(query);
				        	statuses = results.getTweets();
							result = statuses.toString();
				          }
				               
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// statuses = twitter.getUserTimeline("IDPersipura", new
				// Paging(param[1], param[0]));

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("resulTweet", "resultTweet : " + result);

			listThisWeekBean = new ArrayList<TweetBean>();
			try {
				for (twitter4j.Status status3 : statuses) {
					TimeSpanConverter converter = new TimeSpanConverter();
					String createdAt = converter.toTimeSpanString(status3
							.getCreatedAt());

					TweetBean thisWeekBean = new TweetBean();
					thisWeekBean.setTweet(status3.getText());
					thisWeekBean.setTime(createdAt);
					thisWeekBean.setUserImg(status3.getUser()
							.getOriginalProfileImageURL().toString());
					thisWeekBean.setName(status3.getUser().getName());
					thisWeekBean.setUsername(status3.getUser().getScreenName());
					listThisWeekBean.add(thisWeekBean);

				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				} else {
					offset = offset - 15;
					// mPullRefreshScrollView.onRefreshComplete();
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;

			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<TweetBean> listThisWeekBean) throws IOException {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				TweetBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(
						R.layout.tweet_list, null);
				TextView tweetDesc = (TextView) cellViewMainLayout
						.findViewById(R.id.tweetDesc);
				TextView tweetTime = (TextView) cellViewMainLayout
						.findViewById(R.id.tweetTime);
				TextView tweetUsername = (TextView) cellViewMainLayout
						.findViewById(R.id.tweetUsername);
				TextView username = (TextView) cellViewMainLayout
						.findViewById(R.id.username);

				tweetDesc.setText(thisWeekBean.getTweet());
				tweetUsername.setText(thisWeekBean.getName());
				username.setText("@" + thisWeekBean.getUsername());

				AppConstants.fontrobotoTextViewBold(tweetUsername, 11,
						"ffffff", attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextView(username, 11, "cccccc",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextView(tweetDesc, 10, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				tweetDesc.setLinkTextColor(Color.RED); // for example
				Linkify.addLinks(tweetDesc, Linkify.ALL);
				AppConstants.fontrobotoTextView(tweetTime, 10, "cccccc",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				ImageView img = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.staff_placeholder2x;
				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getUserImg(), loader, img);

				 mPullRefreshScrollView.onRefreshComplete();

				lifePageCellContainerLayout.addView(cellViewMainLayout);

			}
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
				failedRetrieveCount++;
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

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				if (!thisWeekBean.getfooter_logo().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getfooter_logo(),
							loader, imgNews);

					LinkId = null;
					LinkId = thisWeekBean.getlink();
					Log.d("clickable",
							"clickable : " + thisWeekBean.getclickable());
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

				// Collections.sort(listAdsBean, new CustomComparator());
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}

			if (failedRetrieveCount > 0) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					Toast.makeText(
							attachingActivityLock.getApplicationContext(),
							"Failed to retrieve data from server",
							Toast.LENGTH_LONG).show();
				}
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
				int loader = R.drawable.staff_placeholder2x;

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
								+ lifePageCellContainerLayout.getChildCount());
				Log.d("lifePageCellContainerLayout.size()", "counter : "
						+ newCount);

				if (newCount >= lifePageCellContainerLayout.getChildCount()) {
					newCount = lifePageCellContainerLayout.getChildCount();
				}

				lifePageCellContainerLayout.addView(cellViewMainLayout,
						newCount);

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
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("onPause", "onPauseCalled");
		if (progressDialog != null) {
			progressDialog.dismiss();

		}
	}

	public class URLSpanNoUnderline extends URLSpan {
		public URLSpanNoUnderline(String url) {
			super(url);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
		}
	}
}