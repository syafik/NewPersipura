package com.persipura.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.squad.DetailSquad;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.DetailNews;
import com.webileapps.navdrawer.News;
import com.webileapps.navdrawer.R;

public class Home extends SherlockFragment {
	public static final String TAG = Home.class.getSimpleName();
	List<HomeNews> listThisWeekBean;
	List<HomeSquad> listSquadBean;
	LinearLayout newsContainerLayout, squadContainerLayout;
	private LayoutInflater mInflater;
	private ProgressDialog progressDialog;
	ViewGroup newContainer;
	String squadId;
	String NewsId;

	public static Home newInstance() {
		return new Home();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		showProgressDialog();
		mInflater = getLayoutInflater(savedInstanceState);

		View rootView = inflater.inflate(R.layout.home, container, false);
		newsContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.relativeLayout3);
		squadContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.squad_home);
		newContainer = container;

		new fetchHomeLatestFromServer().execute("");
		new fetchHomeNewsFromServer().execute("");

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

	private void hideProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
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
				Toast.makeText(getActivity().getApplicationContext(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		private void createSquadHomeListView(List<HomeSquad> listThisWeekBean)
				throws IOException {
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
				BitmapFactory.Options bmOptions;
				squadId = squad.getId();

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				// Bitmap bm = loadBitmap(thisWeekBean.getimg_uri(), bmOptions);
				//
				//
				// imgNews.setImageBitmap(bm);
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				imgLoader.DisplayImage(squad.getfoto(), loader, imgNews);
				View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {

						final FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.remove(Home.this);

						newContainer.setTag(squadId);

						ft.replace(R.id.content, DetailSquad.newInstance(),
								"DetailNews");
						ft.addToBackStack(null);

						ft.commit();

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				squadContainerLayout.addView(cellViewMainLayout);
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
					"/restapi/get/home_news", "limit=4" + "&offset=0");

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
				listThisWeekBean = new ArrayList<HomeNews>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HomeNews thisWeekBean = new HomeNews();
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setimg_uri(resObject.getString("image"));
					thisWeekBean.setNid(resObject.getString("id"));

					listThisWeekBean.add(thisWeekBean);

				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createNewsHomeListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity().getApplicationContext(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		private void createNewsHomeListView(List<HomeNews> listThisWeekBean)
				throws IOException {
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
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				// Bitmap bm = loadBitmap(thisWeekBean.getimg_uri(), bmOptions);
				//
				//
				// imgNews.setImageBitmap(bm);
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getimg_uri(), loader,
						imgNews);

				NewsId = thisWeekBean.getNid();

				View.OnClickListener myhandler1 = new View.OnClickListener() {

					public void onClick(View v) {

						final FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.remove(Home.this);

						newContainer.setTag(NewsId);
						ft.replace(R.id.content, DetailNews.newInstance(),
								"DetailNews");
						ft.addToBackStack(null);

						ft.commit();

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				newsContainerLayout.addView(cellViewMainLayout);
			}
		}

	}

}
