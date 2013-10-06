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
import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.imageBean;
import com.persipura.bean.mediaBean;

import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;




public class ListGalery extends SherlockFragment {

	private LayoutInflater mInflater;
	List<imageBean> listThisWeekBean;
	LinearLayout videoContainer;
	ViewGroup newContainer;
	String nid;

	public static final String TAG = ListGalery.class.getSimpleName();

	public static ListGalery newInstance() {
		return new ListGalery();
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.galery, container, false);
		newContainer = container;
		mInflater = getLayoutInflater(savedInstanceState);

		videoContainer = (LinearLayout) rootView
				.findViewById(R.id.parent_Image);
		
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
					.httpGetServiceWithoutparam("/restapi/get/pictures");
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<imageBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					imageBean thisWeekBean = new imageBean();
					thisWeekBean.setNid(resObject.getString("id"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setimg_uri(resObject
							.getString("picture_url"));
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
				List<imageBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				imageBean thisWeekBean = listThisWeekBean.get(i);

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
				
				String[] parts = thisWeekBean.getpictureUrl().split(" | ");
				Log.d("---------------------", parts[0]);
				
				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				img.setTag(parts[0]);
				imageLoader.DisplayImage(parts[0],
						getActivity(), img);

				videoContainer.addView(cellViewMainLayout);
				
				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {					
						newContainer.setTag(nid);
						GaleryView vp = new GaleryView();
						
						Bundle b = new Bundle();
						b.putString("myString",nid);
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
