package com.persipura.match;

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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.sax.RootElement;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.FooterBean;
import com.persipura.bean.HasilBean;

import com.persipura.home.Home;
import com.persipura.media.videoPlayer;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.R.id;
import com.webileapps.navdrawer.R.layout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class HasilPertandingan extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HasilBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;
	public static final String TAG = HasilPertandingan.class.getSimpleName();
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	int hitung = 10;
	int offset = 10;
	FrameLayout footerLayout;
	List<FooterBean> listFooterBean;
	String LinkId;
	
	public static HasilPertandingan newInstance() {
		return new HasilPertandingan();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		
		View rootView = inflater.inflate(R.layout.hasil_pertandingan,
				container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		
		Integer[] param = new Integer[] { hitung, 0 };
		new fetchLocationFromServer().execute(param);

		
		mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				new GetDataTask().execute();
			  
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
				h.postDelayed(r2, 10000);
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

	
			String result = WebHTTPMethodClass
					.httpGetService("/restapi/get/match_results", "limit=" + param[0] + "&offset=" + param[1]);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<HasilBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HasilBean thisWeekBean = new HasilBean();
					thisWeekBean.setId(resObject.getString("id"));
					thisWeekBean.setTime(resObject.getString("time"));
					thisWeekBean.setDate(resObject.getString("date"));
					thisWeekBean.setHlogo(resObject.getString("h_logo"));
					thisWeekBean.setAlogo(resObject.getString("a_logo"));
					thisWeekBean.setHteam(resObject.getString("h_team"));
					thisWeekBean.setAteam(resObject.getString("a_team"));
					thisWeekBean.setHgoal(resObject.getString("h_goal"));
					thisWeekBean.setAgoal(resObject.getString("a_goal"));

					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}else{
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

		@SuppressLint("UseValueOf")
		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<HasilBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HasilBean thisWeekBean = listThisWeekBean.get(i);

				View cellViewMainLayout = mInflater.inflate(
						R.layout.hasil_pertandingan_list, null);
				TextView ListDate = (TextView) cellViewMainLayout
						.findViewById(R.id.list_date);
				TextView ListTime = (TextView) cellViewMainLayout
						.findViewById(R.id.list_time);
				TextView cellnumTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_cellnum);
				TextView NameTeamA = (TextView) cellViewMainLayout
						.findViewById(R.id.name_team1);
				TextView NameTeamB = (TextView) cellViewMainLayout
						.findViewById(R.id.name_team2);
				TextView ScoreTeamA = (TextView) cellViewMainLayout
						.findViewById(R.id.scoreTeam1);
				TextView ScoreTeamB = (TextView) cellViewMainLayout
						.findViewById(R.id.scoreTeam2);
				ImageView imgTeamA = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);
				ImageView imgTeamB = (ImageView) cellViewMainLayout
						.findViewById(R.id.ImageTeam2);
				
				AppConstants.fontrobotoTextViewBold(NameTeamA, 12, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(NameTeamB, 12, "ffffff", getActivity().getApplicationContext().getAssets());
				
				AppConstants.fontrobotoTextViewBold(ScoreTeamA, 18, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(ScoreTeamB, 18, "ffffff", getActivity().getApplicationContext().getAssets());
				
				
				AppConstants.fontrobotoTextView(ListTime, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(ListDate, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				
				ListDate.setText("");
				ListTime.setText("");
				NameTeamA.setText("");
				NameTeamB.setText("");
				ScoreTeamA.setText("");
				ScoreTeamB.setText("");
				cellnumTextView.setText("");

				ListDate.setText(thisWeekBean.getDate());
				ListTime.setText(thisWeekBean.getTime() + " WIT");
				NameTeamA.setText(thisWeekBean.getHteam());
				NameTeamB.setText(thisWeekBean.getAteam());
				ScoreTeamA.setText(thisWeekBean.getHgoal());
				ScoreTeamB.setText(thisWeekBean.getAgoal());
				
				int in1 = new Integer(ScoreTeamA.getText().toString());
				int in2 = new Integer(ScoreTeamB.getText().toString());
				
				if(in1 > in2){
					ScoreTeamA.setTextColor(getResources().getColor(R.color.red));
				}else if(in1 < in2){
					ScoreTeamB.setTextColor(getResources().getColor(R.color.red));
				}
					

				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				imgTeamA.setTag(thisWeekBean.getHlogo());
				imageLoader.DisplayImage(thisWeekBean.getHlogo(),
						getActivity(), imgTeamA);
				String id = thisWeekBean.getNid();
				imgTeamB.setTag(thisWeekBean.getAlogo());
				imageLoader.DisplayImage(thisWeekBean.getAlogo(),
						getActivity(), imgTeamB);
				
				cellViewMainLayout.setTag(id);
				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {				
						
						detailPertandingan vp = new detailPertandingan();
						Bundle b = new Bundle();
						b.putString("myString",(String) v.getTag());
						vp.setArguments(b);	
						getActivity().getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.parentpager, vp, detailPertandingan.TAG)
						.commit();
					}
				});

				// BitmapFactory.Options bmOptions;
				// bmOptions = new BitmapFactory.Options();
				// bmOptions.inSampleSize = 1;
				// Bitmap bm = loadBitmap(thisWeekBean.getHlogo(), bmOptions);
				// imgTeamA.setImageBitmap(bm);
				//
				// // BitmapFactory.Options bmOptions2;
				// // bmOptions2 = new BitmapFactory.Options();
				// // bmOptions2.inSampleSize = 2;
				// Bitmap bm2 = loadBitmap(thisWeekBean.getAlogo(), bmOptions);
				// imgTeamB.setImageBitmap(bm2);

				lifePageCellContainerLayout.addView(cellViewMainLayout);
				mPullRefreshScrollView.onRefreshComplete();
			}
		}

	}

	

}
