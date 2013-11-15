package com.persipura.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.bean.mediaBean;
import com.persipura.socialize.TwitterSocial;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

public class videoPlayer extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;
	FrameLayout footerLayout;
	ProgressDialog progressDialog;
	List<FooterBean> listFooterBean;
	String LinkId;
	private MediaController ctlr;
	MainActivity attachingActivityLock;
	VideoView videoView;

	public static final String TAG = videoPlayer.class.getSimpleName();

	public static videoPlayer newInstance() {
		return new videoPlayer();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachingActivityLock = (MainActivity) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try{
			videoPlayer video_Player = (videoPlayer) attachingActivityLock.getSupportFragmentManager().findFragmentByTag(videoPlayer.TAG);
			FragmentTransaction trans = attachingActivityLock.getSupportFragmentManager().beginTransaction();
	        trans.remove(video_Player);
	        trans.commit();	
		}catch(Exception e){
			Log.d(TAG, e.getMessage());
		}
		attachingActivityLock = null;
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "onDestroyCalled");
		try{
			videoPlayer video_Player = (videoPlayer) attachingActivityLock.getSupportFragmentManager().findFragmentByTag(videoPlayer.TAG);
			FragmentTransaction trans = attachingActivityLock.getSupportFragmentManager().beginTransaction();
	        trans.remove(video_Player);
	        trans.commit();	
		}catch(Exception e){
			Log.d(TAG, e.getMessage());
		}
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle b = getArguments();
		nid = b.getString("myString");
		showProgressDialog();
		
		Log.d("tagID +++++++++++++++++++++++++++++++", "tagID : " + nid);

		new fetchLocationFromServer().execute("");
		new fetchFooterFromServer().execute("");
		
		View rootView = inflater.inflate(R.layout.video_detail, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				attachingActivityLock.getApplicationContext().getAssets());
//		MainActivity.getInstance().HideOtherActivities();
		return rootView;
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(attachingActivityLock);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		final Handler h = new Handler();
		final Runnable r2 = new Runnable() {

			@Override
			public void run() {
				progressDialog.dismiss();
			}
		};

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				progressDialog.show();
				h.postDelayed(r2, 5000);
			}
		};

		h.postDelayed(r1, 500);

		progressDialog.show();
	}
	
	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/video", "id=" + nid);
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
					Log.d("+++++++++++++++++++++++",
							resObject.getString("title"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setvideo_image(resObject
							.getString("video_image"));
					thisWeekBean.setvideo_uri(resObject
							.getString("embed_url"));
					thisWeekBean.setdescription(resObject
							.getString("description"));
					listThisWeekBean.add(thisWeekBean);

				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<mediaBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				mediaBean thisWeekBean = listThisWeekBean.get(i);

				TextView title = (TextView) getView().findViewById(
						R.id.textTitle);
				TextView created = (TextView) getView().findViewById(
						R.id.textTime);
				TextView description = (TextView) getView().findViewById(
						R.id.textDesc);
//				videoView = (VideoView) getView().findViewById(
//						R.id.videoView1);
				WebView mWebView = (WebView) getView().findViewById(
						R.id.webView1);
				
				AppConstants.fontrobotoTextViewBold(title, 15, "ffffff", attachingActivityLock.getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(created, 11, "A6A5A2", attachingActivityLock.getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(description, 11, "A6A5A2", attachingActivityLock.getApplicationContext().getAssets());
				

				title.setText("");
				created.setText("");
				description.setText("");

				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				description.setText(Html.fromHtml(thisWeekBean.getdescription()));
				
				String page;
				String videoUrl = ""; 
				try {
					String videoID = thisWeekBean.getvideo_uri().split("v=")[1];
					page = new Communicator().httpGetService("http://gdata.youtube.com/feeds/mobile/videos/"+videoID, "");
					Document doc = Jsoup.parse(page);
					Element ab = doc.getElementsByTag("media:content").get(1);
					videoUrl = ab.attr("url");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

//				videoView
//						.setMediaController(new MediaController(attachingActivityLock));
//				Log.d("entry", "entry : " + videoUrl);
//				Uri uri = Uri
//						.parse(videoUrl);
//				videoView.setVideoURI(uri);
//				videoView.requestFocus();
//				
//				videoView.start();
//				
//				String videoSource = "http://173.193.24.66/~kanz/video/mp4/9.mp4";

//				videoView.setMediaController(new MediaController(attachingActivityLock));
//				videoView.setVideoPath(videoUrl);
//				videoView.requestFocus();
//				videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//				  public void onPrepared(MediaPlayer mp) {
//					  videoView.start();
//				  }
//				});
				
				mWebView.getSettings().setJavaScriptEnabled(true);
				mWebView.getSettings().setPluginState(PluginState.ON);
				String videoID = thisWeekBean.getvideo_uri().split("v=")[1];
				mWebView.loadUrl("http://www.youtube.com/embed/" + videoID + "?autoplay=1&vq=small");
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
					"/restapi/get/footer", "id=68");

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
				Toast.makeText(attachingActivityLock.getApplicationContext(),
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
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(attachingActivityLock
						.getApplicationContext());

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
	
	public class Communicator {
		public String httpGetService(String serviceName, String param) {
			String result = "";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet getMethod = new HttpGet(serviceName
						+ "?" + param);
				Log.e(serviceName + " GetURL = ", serviceName + "?" + param);
				BufferedReader in = null;
				BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
						.execute(getMethod);
				if (httpResponse.getStatusLine().getStatusCode() == 401) {
					Log.i("Response Json Failure: 401",
							"" + httpResponse.toString());
					AppConstants.ERROR401 = httpResponse.getStatusLine()
							.getStatusCode() + "";
				} else
					Log.i("Response Json 401 not fount", ""
							+ "httpResponse.toString()");

				in = new BufferedReader(new InputStreamReader(httpResponse
						.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				in.close();
				result = sb.toString();
				System.out.println(serviceName + " result = " + result);
				// result = checkFor401Error(httpResponse, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		}
	

}
