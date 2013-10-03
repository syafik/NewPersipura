package com.persipura.search;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;

import com.persipura.bean.NewsBean;
import com.persipura.bean.SearchBean;
import com.persipura.utils.*;
import com.webileapps.navdrawer.DetailNews;
import com.webileapps.navdrawer.News;
import com.webileapps.navdrawer.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Search extends SherlockFragment {
	// private static String url =
	// "http://prspura.tk/restapi/get/news?limit=20&offset=1";
	private LayoutInflater mInflater;
	List<SearchBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private RelativeLayout mParentLayout;
	ViewGroup newContainer;
	String nid;
	String q;
	private ProgressDialog progressDialog;
	ArrayList<String> arrayList;
	String result_type;
	public static final String TAG = Search.class.getSimpleName();

	public static Search newInstance() {
		return new Search();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		if (q == null) {
			q = getArguments().getString("q");
			Log.d("search ", "search is : " + q);

		}
		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.result, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		TextView resultlabel = (TextView) rootView
				.findViewById(R.id.resultlabel);
		resultlabel.setText(Html.fromHtml(resultlabel.getText() + "&quot;" + q
				+ "&quot;"));
		rootView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {

				View footerView = mInflater.inflate(R.layout.footer,
						newContainer, false);
				FrameLayout bottom_control_bar = (FrameLayout) footerView
						.findViewById(R.id.bottom_control_bar);
				ScrollView scrollBar = (ScrollView) v
						.findViewById(R.id.list_news);
				LayoutParams params = scrollBar.getLayoutParams();
				int height = v.getHeight() - bottom_control_bar.getHeight();
				params.height = height;

			}
		});

		return rootView;
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

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/find_content", "title=" + q + "s&body=" + q);

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.d("result", "result search json : " + jsonArray);
				arrayList = new ArrayList<String>();

				listThisWeekBean = new ArrayList<SearchBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					if (!arrayList.contains(resObject.getString("type"))) {
						arrayList.add(resObject.getString("type"));
					}

					SearchBean thisWeekBean = new SearchBean();
					thisWeekBean.setid(resObject.getString("id"));
					thisWeekBean.setnode_title(resObject
							.getString("node_title"));
					thisWeekBean.settype(resObject.getString("type"));
					thisWeekBean.settimeago(resObject.getString("timeago"));
					 thisWeekBean.setfoto(resObject.getString(resObject.getString("type") + "_img"));

					listThisWeekBean.add(thisWeekBean);
				}
				Log.d("array", "array list : " + arrayList);

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
				List<SearchBean> listThisWeekBean) throws IOException {

			for (int i = 0; i < listThisWeekBean.size(); i++) {
				SearchBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(
						R.layout.result_list, null);
				TextView titleNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView descNews = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);
				TextView cellnumTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_cellnum);
				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				titleNews.setText("");
				descNews.setText("");
				cellnumTextView.setText("");
				// cellViewMainLayout.setTag(thisWeekBean.getNid());
				nid = thisWeekBean.getid();

				titleNews.setText(thisWeekBean.getnode_title());
				descNews.setText(thisWeekBean.gettimeago());
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

				 imgLoader.DisplayImage(thisWeekBean.getfoto(), loader,
				 imgNews);
				LinearLayout rel = null;

				for (int a = 0; a < arrayList.size(); a++) {

					if (arrayList.get(a).equals(thisWeekBean.gettype())) {
						if (lifePageCellContainerLayout
								.findViewWithTag(arrayList.get(a)) == null) {
							rel = new LinearLayout(getActivity());
							rel.setTag(arrayList.get(a));
							rel.setLayoutParams(new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							rel.setOrientation(LinearLayout.VERTICAL);
							View view = new View(getActivity());
							view.setLayoutParams(new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT, (int) 2));
							view.setBackgroundColor(Color.parseColor("#1E1D1F"));
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT); // or
																// wrap_content
							layoutParams
									.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
							layoutParams
									.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
							rel.addView(view, layoutParams);

							TextView tx = new TextView(getActivity());

							tx.setText("from " + arrayList.get(a).toString());

							rel.addView(tx);

						} else {
							rel = (LinearLayout) lifePageCellContainerLayout
									.findViewWithTag(arrayList.get(a));
						}
						result_type = thisWeekBean.gettype();
						View.OnClickListener myhandler1 = new View.OnClickListener() {
							public void onClick(View v) {
								Log.d("result_type", "result_type :"
										+ result_type);
								if (result_type.equals("news")) {
									final FragmentTransaction ft = getFragmentManager()
											.beginTransaction();
									ft.remove(Search.this);

									newContainer.setTag(nid);

									ft.replace(R.id.content,
											DetailNews.newInstance(),
											"DetailNews");
									ft.addToBackStack(null);

									ft.commit();
								}
							}
						};
						cellViewMainLayout.setOnClickListener(myhandler1);
						rel.addView(cellViewMainLayout);
					}
				}

				lifePageCellContainerLayout.addView(rel);

			}
		}

	}

	public static Bitmap loadBitmap(String imgurl, BitmapFactory.Options options) {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			URL url = new URL(imgurl);
			InputStream in = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.close();
			bis.close();
			byte[] data = out.toByteArray();
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

}