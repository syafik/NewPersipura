package com.persipura.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.bean.NewsBean;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;

@SuppressLint("NewApi")
public class DetailNews extends SherlockFragment {
	public static final String TAG = DetailNews.class.getSimpleName();
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	List<FooterBean> listFooterBean;
	LinearLayout footerLayout;
	String LinkId;
	RelativeLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;
	ProgressDialog progressDialog;
	int failedRetrieveCount = 0;
	MainActivity attachingActivityLock;
	
	public static DetailNews newInstance() {
		return new DetailNews();
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
	}

	


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// nid = (String) container.getTag();
		
		TextView titleTextView = (TextView) attachingActivityLock.getSupportActionBar().getCustomView().findViewById(R.id.title_bar_eaa);
		titleTextView.setText("NEWS");
		
		Bundle extras = getArguments();
		nid = extras.getString("NewsId");
		showProgressDialog();

		new fetchLocationFromServer().execute("");
		new fetchFooterFromServer().execute("");

		View rootView = inflater.inflate(R.layout.detail_news2, container,
				false);

		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (RelativeLayout) rootView
				.findViewById(R.id.list_parent);
		footerLayout = (LinearLayout) rootView
				.findViewById(R.id.outer);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				getActivity().getApplicationContext().getAssets());
		MainActivity.getInstance().HideOtherActivities();
		MainActivity.getInstance().changeItemSearchToShare(true);
		
		return rootView;
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(attachingActivityLock);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
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
					"/restapi/get/news", "id=" + nid);

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			Log.d("result", "result : " + result);

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<NewsBean>();
				if(jsonArray.length() < 1){
					String msg = "Content Not Found. Please Try to Refresh.";
					Log.d("message", msg);
//					try {

						AlertDialog alertDialog1 = new AlertDialog.Builder(attachingActivityLock).create();
						alertDialog1.setTitle("Info");
						alertDialog1.setCancelable(false);
						alertDialog1.setMessage(msg);

						alertDialog1.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										attachingActivityLock.selectItem(0);

									}
								});

						alertDialog1.show();
				}else{
					for (int i = 0; i < jsonArray.length(); i++) {
						try{
							JSONObject resObject = jsonArray.getJSONObject(i);
							NewsBean thisWeekBean = new NewsBean();
							thisWeekBean.setNid(resObject.getString("nid"));
							thisWeekBean.settitle(resObject.getString("title"));
							thisWeekBean.setteaser(resObject.getString("body"));
							String img = resObject.getString("big_img");
							Log.d("bigImg", "equal null : " + (img == null));
							Log.d("bigImg", "equal empty : " + (img.isEmpty()));
							Log.d("bigImg", "equal str null : " + (img == "null"));
							
							if(img == null || img.isEmpty() || img == "null"){
								img = resObject.getString("img_uri");							
							}else{
								img = resObject.getString("big_img");

							}
							thisWeekBean.setimg_uri(img);
							thisWeekBean.setcreated(resObject.getString("created"));
							thisWeekBean.setshared_url(resObject.getString("share_url"));

							listThisWeekBean.add(thisWeekBean);
						}catch (Exception e) {
							e.printStackTrace();
							failedRetrieveCount++;

						}
						
					}	
				}
				
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
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

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<NewsBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				NewsBean thisWeekBean = listThisWeekBean.get(i);
				
				TextView titleNews = (TextView) lifePageCellContainerLayout
						.findViewById(R.id.title);
				TextView descNews = (TextView) lifePageCellContainerLayout
						.findViewById(R.id.desc);
				TextView time = (TextView) lifePageCellContainerLayout
						.findViewById(R.id.time);
				ImageView imgNews = (ImageView) lifePageCellContainerLayout
						.findViewById(R.id.imageView1);

				
				
				lifePageCellContainerLayout.setTag(thisWeekBean.getNid());

				titleNews.setText(Html.fromHtml(thisWeekBean.gettitle()));
				String str = thisWeekBean.getteaser().replace("<br />", "");
				descNews.setText(Html.fromHtml(str));
				time.setText(thisWeekBean.getcreated());
				
				AppConstants.fontrobotoTextViewBold(titleNews, 15, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(descNews, 12, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(time, 11, "cccccc",
						getActivity().getApplicationContext().getAssets());
				
				Bitmap bitmap = DownloadImage(thisWeekBean.getimg_uri());
				Display display = getActivity().getWindowManager().getDefaultDisplay();
        		Point size = new Point();
        		display.getSize(size);
        		int screenWidth = size.x;
        		int screenHeight = size.y;

        		// Get target image size
//        		Bitmap bitmap = BitmapFactory.decodeFile(drawable.presence_offline);
        		int bitmapHeight = bitmap.getHeight();
        		int bitmapWidth = bitmap.getWidth();

        		// Scale the image down to fit perfectly into the screen
        		// The value (250 in this case) must be adjusted for phone/tables displays
//        		while(bitmapHeight > (screenHeight - 250) || bitmapWidth > (screenWidth - 250)) {
//        		    bitmapHeight = bitmapHeight / 2;
//        		    bitmapWidth = bitmapWidth / 2;
//        		}
        		if(bitmapHeight > screenHeight){
        			bitmapHeight = screenHeight - 30;
        		}
        		
        		if(bitmapWidth > screenWidth){
        			bitmapWidth = screenWidth;
        		}

        		// Create resized bitmap image
        		BitmapDrawable resizedBitmap = new BitmapDrawable(getActivity().getResources(), Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false));
                imgNews.setImageBitmap(resizedBitmap.getBitmap());
				MainActivity.getInstance().setFacebookContentShare(thisWeekBean.gettitle(), thisWeekBean.getshared_url());
				
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
				failedRetrieveCount++;
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
				int loader = R.drawable.staff_placeholder2x;

				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				if (!thisWeekBean.getfooter_logo().isEmpty()) {
					imgLoader.DisplayImage(thisWeekBean.getfooter_logo(),
							loader, imgNews);

					LinkId = null;
					LinkId = thisWeekBean.getlink();
					TextView footerTitle = (TextView) footerLayout
							.findViewById(R.id.footerText);
					footerTitle.setText("Proudly Sponsored by");
					AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
							attachingActivityLock.getApplicationContext().getAssets());
					
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
	
	 private Bitmap DownloadImage(String URL) {
			String URL1 = URL;
			Bitmap bitmap = null;

			InputStream in = null;
			Message msg = Message.obtain();
			msg.what = 1;
			try {
				in = OpenHttpConnection(URL1);
				bitmap = BitmapFactory.decodeStream(in);
				Bundle b = new Bundle();
				b.putParcelable("bitmap", bitmap);
				msg.setData(b);
				in.close();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

			return bitmap;
		}

		private InputStream OpenHttpConnection(String urlString)
				throws IOException {

			InputStream in = null;
			int response = -1;
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection))
				throw new IOException("Not an HTTP connection");
			try {

				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setAllowUserInteraction(false);
				httpConn.setInstanceFollowRedirects(true);

				httpConn.connect();
				response = httpConn.getResponseCode();

				if (response == HttpURLConnection.HTTP_OK) {

					in = httpConn.getInputStream();
				}
			} catch (Exception ex) {
				throw new IOException("Error connecting");
			}
			return in;
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

}
