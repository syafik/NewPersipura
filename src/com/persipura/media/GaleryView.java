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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;


public class GaleryView extends SherlockFragment {
	// ---the images to display---


	public String[] urls=null;
	ArrayList<String> stringArrayList = new ArrayList<String>();
	ArrayList<String> stringTitleList = new ArrayList<String>();

	private LayoutInflater mInflater;
	List<imageBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;
	private ProgressDialog progressDialog;
	FrameLayout footerLayout;
	List<FooterBean> listFooterBean;
	String LinkId;

	public static final String TAG = GaleryView.class.getSimpleName();

	public static GaleryView newInstance() {
		return new GaleryView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bundle b = getArguments();
		nid = b.getString("myString");
		
		Log.d("tagID +++++++++++++++++++++++++++++++", "tagID : " + nid);
		
		new fetchImage().execute("");
		new fetchFooterFromServer().execute("");
		showProgressDialog();
		newContainer = container;
		View rootView = inflater.inflate(R.layout.gridview, container,
				false);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		mInflater = getLayoutInflater(savedInstanceState);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				getActivity().getApplicationContext().getAssets());
		MainActivity.newInstance().HideOtherActivities();
		return rootView;
	}

	// @Override
	// public void onCreate(Bundle savedInstanceState)
	// {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.displayview);
	//
	// GridView gridView = (GridView) findViewById(R.id.gridview);
	// gridView.setAdapter(new ImageAdapter(this));
	//
	// gridView.setOnItemClickListener(new OnItemClickListener()
	// {
	// public void onItemClick(AdapterView parent,
	// View v, int position, long id)
	// {
	// Toast.makeText(getBaseContext(),
	// "pic" + (position + 1) + " selected",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// }

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		
		ImageAdapter(Context c) {
			context = c;
		}

		public int getCount() {

			return urls.length;
		}

		public Object getItem(int pos) {
			return pos;
		}

		public long getItemId(int pos) {
			return pos;
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

//		@Override
//		public View getView(int position, View cv, ViewGroup parent) {
//			ImageView imageview = null;
//
//			Bitmap bitmap = DownloadImage(urls[position]);
//
//			if (cv == null) {
//				imageview = new ImageView(context);
//			} else {
//				imageview = (ImageView) cv;
//			}
//
//			imageview.setImageBitmap(bitmap);
//			return imageview;
//		}
		
		
		@Override
	    public View getView(int position, View cv, ViewGroup parent) {
	        View v = cv;
	        ImageView picture;
	        final Bitmap bitmap = DownloadImage(urls[position]);
	        final String imagURls = urls[position];
	        
	        if(v == null) {
	            v = mInflater.inflate(R.layout.gridcrop, parent, false);
	            v.setTag(R.id.picture, v.findViewById(R.id.picture));
//	            v.setTag(R.id.text, v.findViewById(R.id.text));
	        }
	        
	        picture = (ImageView)v.getTag(R.id.picture);   
	        
//	        Imageloader imageLoader = new Imageloader(getSherlockActivity()
//					.getApplicationContext());
//			picture.setTag(urls);
//			imageLoader.DisplayImage(imagURls,
//					getActivity(), picture);
	     
	        picture.setImageBitmap(bitmap);
	        
	        picture.setOnClickListener(new OnClickListener()
	        {
	            @SuppressLint("NewApi")
				public void onClick(View v)
	            {
	              
	                		Log.d("adsafdfsa", imagURls);
	                		// Get screen size
	                		Display display = getActivity().getWindowManager().getDefaultDisplay();
	                		Point size = new Point();
	                		display.getSize(size);
	                		int screenWidth = size.x;
	                		int screenHeight = size.y;

	                		// Get target image size
//	                		Bitmap bitmap = BitmapFactory.decodeFile(drawable.presence_offline);
	                		int bitmapHeight = bitmap.getHeight();
	                		int bitmapWidth = bitmap.getWidth();

	                		// Scale the image down to fit perfectly into the screen
	                		// The value (250 in this case) must be adjusted for phone/tables displays
	                		while(bitmapHeight > (screenHeight - 250) || bitmapWidth > (screenWidth - 250)) {
	                		    bitmapHeight = bitmapHeight / 2;
	                		    bitmapWidth = bitmapWidth / 2;
	                		}

	                		// Create resized bitmap image
	                		BitmapDrawable resizedBitmap = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false));

	                		// Create dialog
	                		Dialog dialog = new Dialog(context);
	                		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	                		dialog.setContentView(R.layout.thumbnail);

	                		ImageView image = (ImageView) dialog.findViewById(R.id.imageview);

	                		// !!! Do here setBackground() instead of setImageDrawable() !!! //
//	                		image.setBackground(resizedBitmap);
	                		image.setImageBitmap(bitmap);

	                		// Without this line there is a very small border around the image (1px)
	                		// In my opinion it looks much better without it, so the choice is up to you.
	                		dialog.getWindow().setBackgroundDrawable(null);

	                		// Show the dialog
	                		dialog.show();

	                }
//	            }
	        });
	        
	        return v;
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

	private class fetchImage extends AsyncTask<String, Void, String> {
		ProgressDialog pd = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute() {
			 
						
		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass.httpGetService("/restapi/get/pictures","id=" + nid);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);
				listThisWeekBean = new ArrayList<imageBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					imageBean thisWeekBean = new imageBean();
					thisWeekBean.setNid(resObject.getString("id"));
					thisWeekBean.setimg_uri(resObject.getString("picture_url"));
					thisWeekBean.setteaser(resObject.getString("picture_title"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setcreated(resObject.getString("created"));
					listThisWeekBean.add(thisWeekBean);					
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<imageBean> listThisWeekBean) {
			
			
			
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				imageBean thisWeekBean = listThisWeekBean.get(i);

				TextView title = (TextView) getView()
						.findViewById(R.id.title_text);
				TextView created = (TextView) getView()
						.findViewById(R.id.date_text);
	
				AppConstants.fontrobotoTextView(created, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(title, 18, "ffffff", getActivity().getApplicationContext().getAssets());
				
				title.setText("");
				created.setText("");
				title.setText(thisWeekBean.gettitle());
				created.setText(thisWeekBean.getcreated());
				
				String[] parts = thisWeekBean.getpictureUrl().split("\\|");
				String[] picturetitle = thisWeekBean.getPictureTitle().split("\\|");
				
				for (int x = 0; x < parts.length; x++) {
					stringArrayList.add(parts[x]);
//					stringTitleList.add(picturetitle);
				}

			}

			urls = stringArrayList.toArray(new String[stringArrayList.size()]);
			GridView gridView = (GridView) getView()
					.findViewById(R.id.gridview);
			gridView.setAdapter(new ImageAdapter(getActivity()));

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