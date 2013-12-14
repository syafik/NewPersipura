package com.persipura.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class AppConstants {

	// INTENT_EXTRA CONSTATNS

	public static final String INTENT_EXTRA_HOME_TABNUMBER = "homepage.tab.number";
	public static final String INTENT_EXTRA_HOME_ISFROMTAB = "homepage.tab.isfromTab";

	// INTENT_EXTRA CONSTATNS


	public static String ERROR401 = "Forbiden access";
	
	// FACEBOOK PARAMS

	public static final String FACEBOOK_APPID = "457180554390902";
	public static final String[] FACEBOOK_PERMISSIONARR = new String[] {
			"read_stream", "email", "user_photos", "publish_checkins",
			"publish_stream", "offline_access", "photo_upload" , "offline_access", "publish_actions"};
	public static String FB_APPID = "457180554390902";
	public static final String POSTID = "postid";


	// TWITTER KEY

	public static final String CONSUMER_KEY = "qzooqEGzPmfB5Da2qVdsw";
	public static final String CONSUMER_SECRET = "bTcVQvfUWWhO8JvTLCfVirVUbEFa72QBxp5GfLpYdo";
	public static final String TWITTER_MEDIA_API_KEY = "0dcf25567ddfcf5a1491dd1d5e0cf4e8";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "persipura-scheme";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

	// TWITTER KEY

	public static String LOCALE_HEADER_VALUE = "en";
	/* WEB SERVICE URL CONSTANTS */

	// Live
	public static String APPKEY = "cGU8o7zrsXqBM3pv";
	 
	public static final String ROOT_URL = "http://103.28.15.26";
	public static final String ROOT_URL_HTTPS = "http://103.28.15.26";
	public static String BASE_URL = ROOT_URL;
	public static String BASE_URL_HTTPS = ROOT_URL_HTTPS + "/api/v1"; // Live
	public static String URL_TERMS_OF_USE = ROOT_URL + "/terms_of_use";
	public static String URL_PRIVACY_URL = ROOT_URL + "/privacy_policy";
	public static String URL_FB_PAGE = ROOT_URL + "/url/7/roti-facebook";
	public static String URL_TWITTER_PAGE = ROOT_URL + "/url/7/roti-twitter";

	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkEnable = false;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else
				isNetworkEnable = false;
		} catch (Exception e) {
			isNetworkEnable = false;
			e.printStackTrace();
		}
		return isNetworkEnable;
	}


	public static final String COLORORANGETEXTRGB = "DF821D";
	public static final String COLORBROWNTEXTRGB = "3F2513";
	public static final String COLORDARKGRAYRGB = "666666";
	public static final String COLORWHITERGB = "FFFFFF";
	
	public static void setTextViewAttribute(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
	}

	public static void setTextViewAttributeBold(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(null, Typeface.BOLD);
	}

	private static String roboto = "Roboto-Medium.ttf";
	private static String robotoLight = "Roboto-Regular.ttf";
	public static final String COLORKATHTHI = "5f3032";
	public static final String COLORLOCATIONKATHTHI = "5a2a2b";
	public static final String COLORABOUTRED = "902e26";
	public static final String COLORABOUTKATHTHI = "2d0a0a";


	private static void setTextViewAttribute(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face);
	}


	public static void fontrobotoTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + robotoLight);
		setTextViewAttribute(textView, size, color, face);
	}
	
	public static void fontrobotoTextViewBold(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + roboto);
		setTextViewAttributeBold(textView, size, color, face);
	}
	
	
	private static void setTextViewAttributeBold(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face, Typeface.BOLD);
	}


}