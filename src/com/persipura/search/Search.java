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

import com.persipura.bean.FooterBean;
import com.persipura.bean.NewsBean;
import com.persipura.bean.SearchBean;
import com.persipura.media.videoPlayer;
import com.persipura.utils.*;
import com.webileapps.navdrawer.DetailNews;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.News;
import com.webileapps.navdrawer.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
	LinearLayout eventContainerLayout, newsContainerLayout, videoContainerLayout;
	ViewGroup newContainer;
	String nid;
	String q;
	private ProgressDialog progressDialog;
	ArrayList<String> arrayList;
	String result_type;
	List<FooterBean> listFooterBean;
	FrameLayout footerLayout;
	String LinkId;
	
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
		new fetchFooterFromServer().execute("");
		View rootView = inflater.inflate(R.layout.result, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		eventContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.relativeLayout2);
		newsContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.relativeLayout3);
		videoContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.relativeLayout4);
		TextView resultlabel = (TextView) rootView
				.findViewById(R.id.resultlabel);
		resultlabel.setText(Html.fromHtml(resultlabel.getText() + "&quot;" + q
				+ "&quot;"));
		MainActivity.getInstance().HideOtherActivities();

		footerLayout = (FrameLayout) rootView.findViewById(R.id.bottom_control_bar);

		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff", getActivity()
				.getApplicationContext().getAssets());
		
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
				h.postDelayed(r2, 10000);
			}
		};

		h.postDelayed(r1, 1000);

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
					"/restapi/get/find_content", "title=" + q + "&body=" + q);

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
					String imgUri = null;
					if(resObject.getString("type").equals("News")){
						imgUri = resObject.getString("news_teaser");
					}else if(resObject.getString("type").equals("Video")){
						imgUri = resObject.getString("video_teaser");
					}else{
						imgUri = resObject.getString("picture_url");
					}
					thisWeekBean.setfoto(imgUri);
//					 thisWeekBean.setfoto(resObject.getString(resObject.getString("type") + "_img"));

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
				if(arrayList.contains("Event")){
					View parent = (View) eventContainerLayout.getParent();
					parent.setVisibility(View.VISIBLE);
				}
				if(arrayList.contains("News")){
					View parent = (View) newsContainerLayout.getParent();
					parent.setVisibility(View.VISIBLE);
				}
				if(arrayList.contains("Video")){
					View parent = (View) videoContainerLayout.getParent();
					parent.setVisibility(View.VISIBLE);
				}
				

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
				 cellViewMainLayout.setTag(thisWeekBean.getid());
				nid = thisWeekBean.getid();

				titleNews.setText(thisWeekBean.getnode_title());
				descNews.setText(thisWeekBean.gettimeago());
				BitmapFactory.Options bmOptions;
				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;
				
				try{
					if(!thisWeekBean.getfoto().isEmpty()){
						ImageLoader imgLoader = new ImageLoader(getActivity()
								.getApplicationContext());

						 imgLoader.DisplayImage(thisWeekBean.getfoto(), loader,
						 imgNews);	
					}
					
	
				}catch(Exception e){
					e.printStackTrace();	
				}
				if(thisWeekBean.gettype().equals("Event")){
					eventContainerLayout.addView(cellViewMainLayout);	
				}else if(thisWeekBean.gettype().equals("News")){
					View.OnClickListener myhandler1 = new View.OnClickListener() {
					public void onClick(View v) {
						
						Bundle data = new Bundle();
				        data.putString("NewsId", (String) v.getTag());

				        FragmentTransaction t = getActivity().getSupportFragmentManager()
				                .beginTransaction();
				        DetailNews mFrag = new DetailNews();
				        mFrag.setArguments(data);
				        t.replace(R.id.content, mFrag);
				        t.commit();
					}
				};
				cellViewMainLayout.setOnClickListener(myhandler1);
					newsContainerLayout.addView(cellViewMainLayout);
				}else if(thisWeekBean.gettype().equals("Video")){
					
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
					videoContainerLayout.addView(cellViewMainLayout);	
				}	
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
	
	private class fetchFooterFromServer extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/footer", "");

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

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity().getApplicationContext(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
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

				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				if (!thisWeekBean.getfooter_logo().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getfooter_logo(),
							loader, imgNews);

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

}