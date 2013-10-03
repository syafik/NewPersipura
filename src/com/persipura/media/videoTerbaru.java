package com.persipura.media;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.mediaBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
//import com.webileapps.navdrawer.DetailNews;
//import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.R.id;
import com.webileapps.navdrawer.R.layout;

public class videoTerbaru extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;

	public static final String TAG = videoTerbaru.class.getSimpleName();

	public static videoTerbaru newInstance() {
		return new videoTerbaru();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new fetchLocationFromServer().execute("");
		newContainer = container;
		View rootView = inflater.inflate(R.layout.video, container, false);
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

				title.setText("");
				created.setText("");
				nid = thisWeekBean.getId();
				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				Log.d("6666666666666666666", nid);
				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				img.setTag(thisWeekBean.getvideo_image());
				imageLoader.DisplayImage(thisWeekBean.getvideo_image(),
						getActivity(), img);

				View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {
						// getChildFragmentManager()
						// .beginTransaction()
						// .replace(R.id.list_parent,
						// ,
						// DetailNews.TAG).commit();

						final FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.remove(videoTerbaru.this);
						newContainer.setTag(nid);
						ft.replace(R.id.content, videoPlayer.newInstance(),
								"videoPlayer");
						// ft.replace(R.id.content, DetailNews.newInstance(),
						// "videoPlayer");
						ft.addToBackStack(null);

						ft.commit();

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				lifePageCellContainerLayout.addView(cellViewMainLayout);

			}
		}

	}

}
