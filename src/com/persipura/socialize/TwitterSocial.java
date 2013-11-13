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
import org.json.JSONObject;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

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
import com.persipura.home.Home;
import com.persipura.utils.*;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TwitterSocial extends SherlockFragment {
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
	int hitung = 10;
	int offset = 10;
	int failedRetrieveCount = 0;

	MainActivity attachingActivityLock;

	public static final String TAG = TwitterSocial.class.getSimpleName();

	public static TwitterSocial newInstance() {
		return new TwitterSocial();
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
		setRetainInstance(true);
		
		View rootView = inflater.inflate(R.layout.twitter_main, container, false);

		TextView username = (TextView) rootView.findViewById(R.id.username);
		ImageView imgPic = (ImageView) rootView.findViewById(R.id.imageView1);
		ProgressDialog pd = new ProgressDialog(attachingActivityLock);
		pd.setMessage("Loading...");
		pd.setCancelable(false);
		pd.show();
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
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
			username.setText(twitter.getScreenName());
			User user = twitter.showUser(twitter.getId());
			String url = user.getProfileImageURL();
			BitmapFactory.Options bmOptions;

			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;
			int loader = R.drawable.loader;
			ImageLoader imgLoader = new ImageLoader(getActivity()
					.getApplicationContext());

			imgLoader.DisplayImage(url, loader,
					imgPic);
			

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AppConstants.fontrobotoTextViewBold(username, 14, "ffffff",
				attachingActivityLock.getApplicationContext().getAssets());
		
		
		return rootView;
	}

	
}