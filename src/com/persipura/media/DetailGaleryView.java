package com.persipura.media;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.bean.imageBean;
import com.persipura.main.MainActivity;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;


public class DetailGaleryView extends SherlockFragment {
	LinearLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;
	FrameLayout footerLayout;
	List<FooterBean> listFooterBean;
	String LinkId;
	ImageView bigImg;
	GridView gridView;
	TextView galleryDesc;
	private TextView title;
	private TextView created;


	public static final String TAG = DetailGaleryView.class.getSimpleName();

	public static DetailGaleryView newInstance() {
		return new DetailGaleryView();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Editor editor = mPrefs.edit();
		editor.putBoolean("isMediaImage", true);
		editor.commit();
		
		Bundle b = getArguments();
		String imgUri = b.getString("myString");
		String imgDesc = b.getString("myDesc");
		String imgTitle = b.getString("titleImg");
		String createdImg = b.getString("createdImg");
		
		new fetchFooterFromServer().execute("");
		showProgressDialog();
		View rootView = inflater.inflate(R.layout.detail_gridview, container,
				false);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		bigImg = (ImageView) rootView.findViewById(R.id.detailGaleryImg);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		galleryDesc = (TextView) rootView.findViewById(R.id.galleryDesc);
		
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				getActivity().getApplicationContext().getAssets());
		
		final Bitmap bitmap = DownloadImage(imgUri);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		
		if(bitmapHeight > screenHeight){
			bitmapHeight = screenHeight - 60;
		}else{
			bitmapHeight = bitmapHeight - 40;
		}
		
		if(bitmapWidth > screenWidth){
			bitmapWidth = screenWidth;
		}
		
		BitmapDrawable  resizedBitmap = new BitmapDrawable(getActivity().getResources(), Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false));

		bigImg.setImageBitmap(resizedBitmap.getBitmap());
		galleryDesc.setText(imgDesc);
		bitmap.recycle();
		title = (TextView) rootView
				.findViewById(R.id.title_text);
		created = (TextView) rootView
				.findViewById(R.id.date_text);
		AppConstants.fontrobotoTextView(created, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
		AppConstants.fontrobotoTextViewBold(title, 18, "ffffff", getActivity().getApplicationContext().getAssets());
		
		title.setText(imgTitle);
		created.setText(createdImg);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		MainActivity.getInstance().HideOtherActivities();
		return rootView;
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
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
				int loader = R.drawable.staff_placeholder2x;

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
					TextView footerTitle = (TextView) footerLayout
							.findViewById(R.id.footerText);
					footerTitle.setText("Proudly Sponsored by");
					AppConstants.fontrobotoTextViewBold(footerTitle, 13, "ffffff",
							getActivity().getApplicationContext().getAssets());
				}

			}
		}

	}
}