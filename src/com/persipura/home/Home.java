package com.persipura.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.AdsBean;
import com.persipura.bean.FooterBean;
import com.persipura.squad.DetailSquad;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.DetailNews;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.News;
import com.webileapps.navdrawer.R;

public class Home extends SherlockFragment {
	public static final String TAG = Home.class.getSimpleName();
	List<HomeNews> listThisWeekBean;
	List<HomeSquad> listSquadBean;
	List<HomeNextMatch> listNextMatchBean;
	List<FooterBean> listFooterBean;
	List<AdsBean> listAdsBean;
	LinearLayout newsContainerLayout, squadContainerLayout,
			nextMatchContainerLayout;
	TextView homeNewsTitle, squadTitle;
	FrameLayout footerLayout;
	RelativeLayout adsLayout;
	private LayoutInflater mInflater;
	private ProgressDialog progressDialog;
	ViewGroup newContainer;
	String squadId;
	String NewsId;
	int failedRetrieveCount = 0;
	String LinkId;
	MainActivity attachingActivityLock;

	public static Home newInstance() {
		return new Home();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachingActivityLock = (MainActivity) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		attachingActivityLock = null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setRetainInstance(true);
		showProgressDialog();

		mInflater = getLayoutInflater(savedInstanceState);

		View rootView = inflater.inflate(R.layout.home, container, false);

		newsContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.relativeLayout3);
		squadContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.squad_home);
		nextMatchContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.linearNextMatch);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		adsLayout = (RelativeLayout) rootView.findViewById(R.id.eksklusive_ads);
		newContainer = container;
		
		squadTitle = (TextView) rootView.findViewById(R.id.squadTitle);
		TextView squadTitle = (TextView) rootView.findViewById(R.id.squadTitle);
		
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
				attachingActivityLock.getApplicationContext().getAssets());
		AppConstants.fontrobotoTextView(squadTitle, 15, "A6A5A2",
				attachingActivityLock.getApplicationContext().getAssets());
		new fetchHomeLatestFromServer().execute("");
		new fetchHomeNewsFromServer().execute("");
		new fetchNextMatchFromServer().execute("");
		new fetchAdsFromServer().execute("");
		new fetchFooterFromServer().execute("");

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
				h.postDelayed(r2, 10000);
			}
		};

		h.postDelayed(r1, 1000);

		progressDialog.show();
	}

	private class fetchHomeLatestFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass
					.httpGetServiceWithoutparam("/restapi/get/home_squad");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("result", "result home squad : " + result);
			try {
				JSONArray jsonArray = new JSONArray(result);
				listSquadBean = new ArrayList<HomeSquad>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HomeSquad homeSquad = new HomeSquad();
					homeSquad.setNamaLengkap(resObject
							.getString("nama_lengkap"));
					homeSquad.setId(resObject.getString("id"));
					homeSquad.setposisi(resObject.getString("posisi"));
					homeSquad.setage(resObject.getString("age"));
					homeSquad
							.setwarganegara(resObject.getString("warganegara"));
					homeSquad.setfoto(resObject.getString("foto"));
					homeSquad
							.setno_punggung(resObject.getString("no_punggung"));

					listSquadBean.add(homeSquad);

				}
				if (listSquadBean != null && listSquadBean.size() > 0) {
					createSquadHomeListView(listSquadBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}

		}

		private void createSquadHomeListView(List<HomeSquad> listThisWeekBean)
				throws IOException {
			squadTitle.setVisibility(View.VISIBLE);
			squadContainerLayout.setVisibility(View.VISIBLE);
			squadContainerLayout.removeAllViews();
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HomeSquad squad = listSquadBean.get(i);
				View cellViewMainLayout = mInflater.inflate(
						R.layout.squad_list, null);
				TextView nama = (TextView) cellViewMainLayout
						.findViewById(R.id.textViewList2);
				TextView detail = (TextView) cellViewMainLayout
						.findViewById(R.id.textView1);
				TextView no_punggung = (TextView) cellViewMainLayout
						.findViewById(R.id.no_punggung);

				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageViewList2);

				nama.setText("");
				detail.setText("");
				no_punggung.setText("");

				no_punggung.setText(squad.getno_punggung());
				nama.setText(squad.getNamaLengkap());
				detail.setText(squad.getposisi() + "\n" + squad.getage()
						+ " tahun" + ", " + squad.getwarganegara());
				AppConstants.fontrobotoTextViewBold(nama, 11, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextView(no_punggung, 15, "A6A5A2",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextView(detail, 11, "A6A5A2",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				BitmapFactory.Options bmOptions;
				squadId = squad.getId();

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				imgLoader.DisplayImage(squad.getfoto(), loader, imgNews);
				View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {

						Bundle data = new Bundle();
						data.putString("squadId", (String) squadId);
						data.putString("FragmentTag", Home.TAG);
						
						FragmentTransaction t = attachingActivityLock
								.getSupportFragmentManager().beginTransaction();
						DetailSquad mFrag = new DetailSquad();
						mFrag.setArguments(data);
						t.add(R.id.content, mFrag, DetailSquad.TAG).commit();

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				squadContainerLayout.addView(cellViewMainLayout);
			}
		}

	}

	private class fetchNextMatchFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass
					.httpGetServiceWithoutparam("/restapi/get/home_match");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("result", "result home match: " + result);
			try {
				JSONArray jsonArray = new JSONArray(result);

				listNextMatchBean = new ArrayList<HomeNextMatch>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HomeNextMatch thisWeekBean = new HomeNextMatch();

					thisWeekBean.setTeam1(resObject.getString("a_team"));
					thisWeekBean.setTeam2(resObject.getString("h_team"));
					thisWeekBean.setId(resObject.getString("id"));
					thisWeekBean.setTeam1Logo(resObject.getString("a_logo"));
					thisWeekBean.setTeam2Logo(resObject.getString("h_logo"));
					thisWeekBean.setdate(resObject.getString("date"));
					thisWeekBean.setleague(resObject.getString("league"));
					thisWeekBean.settime(resObject.getString("time") + " " + resObject.getString("timezone"));
					

					listNextMatchBean.add(thisWeekBean);

				}
				if (listNextMatchBean != null && listNextMatchBean.size() > 0) {
					createNextMatchView(listNextMatchBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}

		}

		private void createNextMatchView(List<HomeNextMatch> listNextMatchBean)
				throws IOException {
			nextMatchContainerLayout.removeAllViews();
			for (int i = 0; i < listNextMatchBean.size(); i++) {
				HomeNextMatch thisWeekBean = listNextMatchBean.get(i);
				
				
				View cellViewMainLayout = mInflater.inflate(
						R.layout.home_next_match, null);

				TextView team1 = (TextView) cellViewMainLayout
						.findViewById(R.id.team1);
				TextView team2 = (TextView) cellViewMainLayout
						.findViewById(R.id.team2);
				
				TextView date = (TextView) cellViewMainLayout
						.findViewById(R.id.dateMatch);
				TextView time = (TextView) cellViewMainLayout
						.findViewById(R.id.timeMatch);
				TextView league = (TextView) cellViewMainLayout
						.findViewById(R.id.leagueMatch);

				team1.setText("");
				team2.setText("");
				date.setText("");
				time.setText("");
				league.setText("");

				team1.setText(thisWeekBean.getTeam1());
				team2.setText(thisWeekBean.getTeam2());
				date.setText(thisWeekBean.getdate());
				time.setText(thisWeekBean.gettime());
				league.setText(thisWeekBean.getleague());

				AppConstants.fontrobotoTextViewBold(date, 14, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextViewBold(time, 14, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextViewBold(league, 14, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextViewBold(team1, 13, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				AppConstants.fontrobotoTextViewBold(team2, 13, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				nextMatchContainerLayout.addView(cellViewMainLayout);

				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());
				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);
				ImageView imgNews2 = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView2);

				imgLoader.DisplayImage(thisWeekBean.getTeam1Logo(), loader,
						imgNews);
				imgLoader.DisplayImage(thisWeekBean.getTeam2Logo(), loader,
						imgNews2);
			}
		}

	}

	private class fetchHomeNewsFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/home_news", "limit=3" + "&offset=0");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("result", "result news : " + result);
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.d("test1", "test1 : " + jsonArray);
				listThisWeekBean = new ArrayList<HomeNews>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HomeNews thisWeekBean = new HomeNews();
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setimg_uri(resObject.getString("img_uri"));
					thisWeekBean.setNid(resObject.getString("nid"));

					listThisWeekBean.add(thisWeekBean);

				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createNewsHomeListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}

		}

		private void createNewsHomeListView(List<HomeNews> listThisWeekBean)
				throws IOException {
			newsContainerLayout.setVisibility(View.VISIBLE);

			newsContainerLayout.removeAllViews();
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HomeNews thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(R.layout.news_list,
						null);
				TextView titleNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView timeNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);

				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				titleNews.setText("");
				timeNews.setText("");

				titleNews.setText(thisWeekBean.gettitle());
				timeNews.setText(thisWeekBean.getcreated());
				AppConstants.fontrobotoTextViewBold(titleNews, 11, "ffffff",
						attachingActivityLock.getApplicationContext()
								.getAssets());
				
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getimg_uri(), loader,
						imgNews);

				NewsId = null;
				NewsId = thisWeekBean.getNid();

				cellViewMainLayout.setTag(NewsId);
				Log.d("NewsId", "NewsId : " + cellViewMainLayout.getTag());
				View.OnClickListener myhandler1 = new View.OnClickListener() {

					public void onClick(View v) {
						SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(attachingActivityLock);
						Editor editor = mPrefs.edit();
						editor.putString("currentFragment", Home.TAG);
						editor.putString("prevFragment", Home.TAG);
						editor.commit();
						
						Bundle data = new Bundle();
						data.putString("NewsId", (String) v.getTag());

						FragmentTransaction t = attachingActivityLock
								.getSupportFragmentManager().beginTransaction();
						DetailNews mFrag = new DetailNews();
						mFrag.setArguments(data);
						t.add(R.id.content, mFrag, DetailNews.TAG).commit();

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				newsContainerLayout.addView(cellViewMainLayout);
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

				if (progressDialog != null) {
					progressDialog.dismiss();
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;

			}

			if (failedRetrieveCount > 0) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					Toast.makeText(
							attachingActivityLock.getApplicationContext(),
							"Failed to retrieve data from server",
							Toast.LENGTH_LONG).show();
				}
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

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				if (!thisWeekBean.getfooter_logo().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getfooter_logo(),
							loader, imgNews);

					LinkId = null;
					LinkId = thisWeekBean.getlink();
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

	private class fetchAdsFromServer extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/ads", "ad_location=home");

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
				listAdsBean = new ArrayList<AdsBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					AdsBean thisWeekBean = new AdsBean();
					thisWeekBean.setclickable(resObject.getString("clickable"));
					thisWeekBean.setad_rank(resObject.getString("ad_rank"));
					thisWeekBean.setlink(resObject.getString("ad_link"));

					int screenSize = attachingActivityLock
							.getApplicationContext().getResources()
							.getConfiguration().screenLayout
							& Configuration.SCREENLAYOUT_SIZE_MASK;

					switch (screenSize) {
					case Configuration.SCREENLAYOUT_SIZE_LARGE:
						thisWeekBean.setimage(resObject
								.getString("ad_image_ldpi"));
						break;
					case Configuration.SCREENLAYOUT_SIZE_NORMAL:
						thisWeekBean.setimage(resObject
								.getString("ad_image_hdpi"));
						break;
					case Configuration.SCREENLAYOUT_SIZE_SMALL:
						thisWeekBean.setimage(resObject
								.getString("ad_image_mdpi"));
						break;
					default:
						thisWeekBean.setimage(resObject
								.getString("ad_image_xhdpi"));
					}

					thisWeekBean.setclickcounter(resObject
							.getString("clickcounter"));

					//
					listAdsBean.add(thisWeekBean);

				}
				if (listAdsBean != null && listAdsBean.size() > 0) {
					createAdsView(listAdsBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}

		}

		private void createAdsView(List<AdsBean> listAdsBean)
				throws IOException {
			for (int i = 0; i < listAdsBean.size(); i++) {
				AdsBean thisWeekBean = listAdsBean.get(i);

				ImageView imgNews = (ImageView) adsLayout
						.findViewById(R.id.ads_img);
				imgNews.setVisibility(View.VISIBLE);
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(
						attachingActivityLock.getApplicationContext());

				if (!thisWeekBean.getimage().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getimage(), loader,
							imgNews);

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
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "onDestroyCalled");
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("onPause", "onPauseCalled");
		if (progressDialog != null) {
			progressDialog.dismiss();

		}
	}
//	
//	@Override
//	public void onResume(){
//		super.onResume();
//		Log.d("onResume", "onResumeCalled");
//		if(progressDialog !=null){
//			progressDialog.dismiss();
//		}
//	}
}
