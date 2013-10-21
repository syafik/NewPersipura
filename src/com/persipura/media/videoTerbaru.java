package com.persipura.media;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.mediaBean;

import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.DetailNews;
//import com.webileapps.navdrawer.DetailNews;
//import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.R;
import com.persipura.home.Home;
import com.persipura.media.*;


public class videoTerbaru extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout videoContainer;
	ViewGroup newContainer;
	String nid;

	public static final String TAG = videoTerbaru.class.getSimpleName();

	public static videoTerbaru newInstance() {
		return new videoTerbaru();
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
		
		new fetchLocationFromServer().execute("");
		
		rootView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				// hideProgressDialog();
			}

		});
		return rootView;
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass
					.httpGetServiceWithoutparam("/restapi/get/video");
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
				
				AppConstants.fontrobotoTextView(created, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(title, 15, "ffffff", getActivity().getApplicationContext().getAssets());

				title.setText("");
				created.setText("");
				nid = null;
				nid = thisWeekBean.getId();
				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				Log.d("6666666666666666666", nid);
				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				img.setTag(thisWeekBean.getvideo_image());
				imageLoader.DisplayImage(thisWeekBean.getvideo_image(),
						getActivity(), img);

				videoContainer.addView(cellViewMainLayout);
				cellViewMainLayout.setTag(nid);
				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {					
						
						videoPlayer vp = new videoPlayer();
						
						Bundle b = new Bundle();
						b.putString("myString",(String) v.getTag());
						vp.setArguments(b);	
						getActivity().getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, vp,"detail")
						.addToBackStack("")
						.commit();
					}
				});
		

			}
		}

	}

}
