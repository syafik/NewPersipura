package com.persipura.socialize;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.HasilBean;
import com.persipura.bean.SejarahBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;

public class FacebookSocial extends SherlockFragment {

	private LayoutInflater mInflater;
	List<SejarahBean> listThisWeekBean;
	RelativeLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;

	public static final String TAG = FacebookSocial.class.getSimpleName();

	public static FacebookSocial newInstance() {
		return new FacebookSocial();
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.sejarah, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		
		
		return rootView;
	}


	
}
