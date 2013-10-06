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

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.imageBean;

import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GaleryView extends SherlockFragment {
	// ---the images to display---


	public String[] urls=null;
	ArrayList<String> stringArrayList = new ArrayList<String>();

	private LayoutInflater mInflater;
	List<imageBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;

	public static final String TAG = GaleryView.class.getSimpleName();

	public static GaleryView newInstance() {
		return new GaleryView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new fetchImage().execute("");

		newContainer = container;
		View rootView = inflater.inflate(R.layout.gridview, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);

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
	        Bitmap bitmap = DownloadImage(urls[position]);
	        
	        if(v == null) {
	            v = mInflater.inflate(R.layout.gridcrop, parent, false);
	            v.setTag(R.id.picture, v.findViewById(R.id.picture));
	            v.setTag(R.id.text, v.findViewById(R.id.text));
	        }
	        
	        picture = (ImageView)v.getTag(R.id.picture);   
	        picture.setImageBitmap(bitmap);
	    
	        return v;
	    }
	}

	private class fetchImage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass
					.httpGetServiceWithoutparam("/restapi/get/pictures");
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

				String[] parts = thisWeekBean.getpictureUrl().split(" | ");
				Log.d("---------------------", parts[0]);

				stringArrayList.add(parts[0]);

			}

			urls = stringArrayList.toArray(new String[stringArrayList.size()]);
			GridView gridView = (GridView) getView()
					.findViewById(R.id.gridview);
			gridView.setAdapter(new ImageAdapter(getActivity()));

		}

	}
}