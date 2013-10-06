package com.persipura.media;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;

import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.mediaBean;

import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

public class videoPlayer extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;

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
	
		Log.d("tagID +++++++++++++++++++++++++++++++", "tagID : " + nid);

		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.video_detail, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		
		return rootView;
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

				title.setText("");
				created.setText("");
				description.setText("");

				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				description.setText(thisWeekBean.getcreated());

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

}
