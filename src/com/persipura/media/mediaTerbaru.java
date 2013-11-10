package com.persipura.media;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.persipura.bean.mediaBean;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.DetailNews;
import com.webileapps.navdrawer.R;
//import com.markupartist.android.widget.PullToRefreshListView;
//import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class mediaTerbaru extends SherlockFragment {

	private LayoutInflater mInflater;
	List<mediaBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	int hitung = 10;
	int offset = 10;
	ProgressDialog progressDialog;
	String nid;
	String type;
	
	public static final String TAG = mediaTerbaru.class.getSimpleName();

	public static mediaTerbaru newInstance() {
		return new mediaTerbaru();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		setRetainInstance(true);
		Integer[] param = new Integer[] { hitung, 0 };
		new fetchLocationFromServer().execute(param);
		View rootView = inflater.inflate(R.layout.media_terbaru, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);
		showProgressDialog();
		mPullRefreshScrollView = (PullToRefreshScrollView) rootView
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// new GetDataTask().execute();

						Integer[] param = new Integer[] { hitung, offset };
						new fetchLocationFromServer().execute(param);
						offset = offset + 10;

					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);

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
			AsyncTask<Integer, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Integer... param) {

			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/media", "limit=" + param[0] + "&offset="
							+ param[1]);
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
					thisWeekBean.setType(resObject.getString("type"));
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				} else {
					offset = offset - 10;
					mPullRefreshScrollView.onRefreshComplete();
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
						R.layout.media_terbaru_list, null);
				TextView title = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView created = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);
				ImageView img = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				AppConstants.fontrobotoTextView(created, 11, "A6A5A2",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(title, 15, "ffffff",
						getActivity().getApplicationContext().getAssets());

				title.setText("");
				created.setText("");
				nid = null;
				nid = thisWeekBean.getId();
				type = null;
				type = thisWeekBean.getType();
				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());

				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				img.setTag(thisWeekBean.getvideo_image());
				imageLoader.DisplayImage(thisWeekBean.getvideo_image(),
						getActivity(), img);

				cellViewMainLayout.setTag(nid);
				cellViewMainLayout.setTag(R.string.section1, type);

				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						String type = (String) v.getTag(R.string.section1);
						Log.d("-------------", type);
						if (type.equals("picture")) {
							
							Bundle data = new Bundle();
							data.putString("myString", (String) v.getTag());

							FragmentTransaction t = getFragmentManager()
									.beginTransaction();
							GaleryView mFrag = new GaleryView();
							mFrag.setArguments(data);
							t.replace(R.id.parentpager, mFrag, GaleryView.TAG).commit();
							
						} else {
							Bundle data = new Bundle();
							data.putString("myString", (String) v.getTag());
							FragmentTransaction t = getFragmentManager()
									.beginTransaction();
							videoPlayer mFrag = new videoPlayer();
							mFrag.setArguments(data);
							t.replace(R.id.parentpager, mFrag, videoPlayer.TAG).commit();
						}
					}
				});
				lifePageCellContainerLayout.addView(cellViewMainLayout);
				mPullRefreshScrollView.onRefreshComplete();
			}
		}

	}

}
