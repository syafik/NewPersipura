package com.webileapps.navdrawer;

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
import com.persipura.utils.*;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class News extends SherlockFragment {
	// private static String url =
	// "http://prspura.tk/restapi/get/news?limit=20&offset=1";
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private RelativeLayout mParentLayout;
	ViewGroup newContainer;
	String nid;
	private ProgressDialog progressDialog;

	public static final String TAG = News.class.getSimpleName();

	public static News newInstance() {
		return new News();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		new fetchLocationFromServer().execute("");	
		View rootView = inflater.inflate(R.layout.news, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);

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
					"/restapi/get/news", "limit=20" + "&offset=0");

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<NewsBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					NewsBean thisWeekBean = new NewsBean();
					thisWeekBean.setNid(resObject.getString("id"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setteaser(resObject.getString("teaser"));
					thisWeekBean.setimg_uri(resObject.getString("img_uri"));

					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Failed to retrieve data from server", Toast.LENGTH_LONG).show();
			}
		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<NewsBean> listThisWeekBean) throws IOException {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				NewsBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(R.layout.news_list,
						null);
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
//				cellViewMainLayout.setTag(thisWeekBean.getNid());
				nid = thisWeekBean.getNid();

				titleNews.setText(thisWeekBean.gettitle());
				descNews.setText(Html.fromHtml(thisWeekBean.getteaser()));
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
//				Bitmap bm = loadBitmap(thisWeekBean.getimg_uri(), bmOptions);
//				
//				
//				imgNews.setImageBitmap(bm);
				int loader = R.drawable.loader;

				ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getimg_uri(), loader, imgNews);
				
				View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {
//						getChildFragmentManager()
//						.beginTransaction()
//						.replace(R.id.list_parent,
//								,
//								DetailNews.TAG).commit();
						

						final FragmentTransaction ft = getFragmentManager().beginTransaction();
						ft.remove(News.this);
				        
						newContainer.setTag(nid);
						ft.replace(R.id.content, DetailNews.newInstance(), "DetailNews"); 
						ft.addToBackStack(null);

						ft.commit(); 

					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);

				lifePageCellContainerLayout.addView(cellViewMainLayout);

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