package com.persipura.match;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.bean.HasilBean;
import com.persipura.main.MainActivity;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class JadwalPertandingan extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HasilBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	int hitung = 10;
	int offset = 10;
	FrameLayout footerLayout;
	List<FooterBean> listFooterBean;
	String LinkId;
	
	public static final String TAG = JadwalPertandingan.class.getSimpleName();

	public static JadwalPertandingan newInstance() {
		return new JadwalPertandingan();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		View rootView = inflater.inflate(R.layout.jadwal_pertandingan,
				container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		Integer[] param = new Integer[] { hitung, 0 };
		new fetchLocationFromServer().execute(param);

		mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(refreshView.getheaderScroll() < 0){
					lifePageCellContainerLayout.removeAllViews();

					Integer[] param = new Integer[] { hitung, 0 };
					new fetchLocationFromServer().execute(param);
				}else{

					Integer[] param = new Integer[] { hitung, offset };
					new fetchLocationFromServer().execute(param);
					offset = offset + 10;
				}				
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
//		
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		MainActivity.getInstance().HideOtherActivities();
		return rootView;
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		
//		final Handler h = new Handler();
//		final Runnable r2 = new Runnable() {
//
//			@Override
//			public void run() {
//				progressDialog.dismiss();
//			}
//		};
//
//		Runnable r1 = new Runnable() {
//
//			@Override
//			public void run() {
//				progressDialog.show();
//				h.postDelayed(r2, 10000);
//			}
//		};
//
//		h.postDelayed(r1, 500);

		progressDialog.show();
	}

	private class fetchLocationFromServer extends
			AsyncTask<Integer, Void, String> {

		@Override
		protected void onPreExecute() {
			// ProgressDialog pd = new ProgressDialog(getActivity());
			// pd.setMessage("loading");
			// pd.show();
		}

		@Override
		protected String doInBackground(Integer... param) {
			String result = WebHTTPMethodClass
					.httpGetService("/restapi/get/match_schedules", "limit=" + param[0] + "&offset=" + param[1]);
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
					thisWeekBean.setPlace(resObject.getString("place"));
					thisWeekBean.setleague(resObject.getString("league"));
					
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}else{
					View cellViewMainLayout = mInflater.inflate(
							R.layout.no_next_match, null);

					TextView noNextMatch = (TextView) cellViewMainLayout
							.findViewById(R.id.noNextMatch);
					View liner = (View) cellViewMainLayout.findViewById(R.id.liner);
					liner.setVisibility(View.GONE);
					AppConstants.fontrobotoTextView(noNextMatch, 14, "cdcdcd",
							getActivity().getApplicationContext()
									.getAssets());
					lifePageCellContainerLayout.addView(cellViewMainLayout);
					
					offset = offset - 10;
					mPullRefreshScrollView.onRefreshComplete();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}
			
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<HasilBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HasilBean thisWeekBean = listThisWeekBean.get(i);

				View cellViewMainLayout = mInflater.inflate(
						R.layout.jadwal_pertandingan_list, null);
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
				TextView Place = (TextView) cellViewMainLayout
						.findViewById(R.id.text_stadiun);
				ImageView imgTeamA = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);
				ImageView imgTeamB = (ImageView) cellViewMainLayout
						.findViewById(R.id.ImageTeam2);
				TextView leagueName = (TextView) cellViewMainLayout.findViewById(R.id.TextView01);

				AppConstants.fontrobotoTextViewBold(NameTeamA, 12, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(NameTeamB, 12, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(ListTime, 11, "A6A5A2",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(ListDate, 11, "A6A5A2",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(Place, 11, "A6A5A2",
						getActivity().getApplicationContext().getAssets());

				ListDate.setText("");
				ListTime.setText("");
				NameTeamA.setText("");
				NameTeamB.setText("");
				Place.setText("");
				cellnumTextView.setText("");

				leagueName.setText(thisWeekBean.getleague());
				ListDate.setText(thisWeekBean.getDate());
				ListTime.setText(thisWeekBean.getTime() + " WIT");
				NameTeamA.setText(thisWeekBean.getHteam());
				NameTeamB.setText(thisWeekBean.getAteam());
				Place.setText(thisWeekBean.getPlace());

				// BitmapFactory.Options bmOptions;
				// bmOptions = new BitmapFactory.Options();
				// bmOptions.inSampleSize = 1;
				// Bitmap bm = loadBitmap(thisWeekBean.getHlogo(), bmOptions);
				// imgTeamA.setImageBitmap(bm);
				//
				// Bitmap bm2 = loadBitmap(thisWeekBean.getAlogo(), bmOptions);
				// imgTeamB.setImageBitmap(bm2);

				int loader = R.drawable.staff_placeholder2x;
				ImageLoader imageLoader = new ImageLoader(getActivity()
						.getApplicationContext());
				
				imgTeamA.setTag(thisWeekBean.getHlogo());
				imageLoader.DisplayImage(thisWeekBean.getHlogo(),
						loader, imgTeamA);

				imgTeamB.setTag(thisWeekBean.getAlogo());
				imageLoader.DisplayImage(thisWeekBean.getAlogo(),
						loader, imgTeamB);

				lifePageCellContainerLayout.addView(cellViewMainLayout);
				mPullRefreshScrollView.onRefreshComplete();
			}
		}

	}

}
