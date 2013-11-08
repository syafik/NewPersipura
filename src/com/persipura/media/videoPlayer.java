package com.persipura.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	
	public static final String TAG = videoPlayer.class.getSimpleName();

	public static videoPlayer newInstance() {
		return new videoPlayer();
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
				getActivity().getApplicationContext().getAssets());
		MainActivity.newInstance().HideOtherActivities();
		return rootView;
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
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
							.getString("video_uri"));
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
				VideoView videoView = (VideoView) getView().findViewById(
						R.id.videoView1);
				
				AppConstants.fontrobotoTextViewBold(title, 15, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(created, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(description, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				

				title.setText("");
				created.setText("");
				description.setText("");

				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				description.setText(Html.fromHtml(thisWeekBean.getdescription()));

				
				
				videoView
						.setMediaController(new MediaController(getActivity()));
				Uri uri = Uri
						.parse(thisWeekBean.getvideo_uri());
				
				videoView.setVideoURI(uri);
				videoView.start();
				videoView.requestFocus();

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
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(getActivity()
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

}
