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
import com.persipura.bean.mediaBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GaleryView extends SherlockFragment {
	// ---the images to display---
	Integer[] imageIDs = { R.drawable.saturn, R.drawable.mercury,
			R.drawable.mars };

	static String uri1 = "http://i3.ytimg.com/vi/bQaWsVQSLdY/default.jpg";
	static String uri2 = "http://i4.ytimg.com/vi/cJQCniWQdno/mqdefault.jpg";
	static String uri3 = "http://i1.ytimg.com/vi/D8dA4pE5hEY/mqdefault.jpg";
	public String[] urls={uri1,uri2};
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
		View rootView = inflater.inflate(R.layout.gridviewgalery, container,
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

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ImageView imageview = null;

			Bitmap bitmap = DownloadImage(urls[position]);

			if (cv == null) {
				imageview = new ImageView(context);
			} else {
				imageview = (ImageView) cv;
			}

			imageview.setImageBitmap(bitmap);
			return imageview;
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

				// View cellViewMainLayout = mInflater.inflate(
				// R.layout.video_list, null);
				// TextView title = (TextView) cellViewMainLayout
				// .findViewById(R.id.findzoes_list_text_name);
				// TextView created = (TextView) cellViewMainLayout
				// .findViewById(R.id.findzoes_list_text_address);
				// ImageView img = (ImageView) cellViewMainLayout
				// .findViewById(R.id.imageView1);
				//
				// title.setText("");
				// created.setText("");
				// nid = thisWeekBean.getId();
				// title.setText(thisWeekBean.gettitle());
				// created.setText(thisWeekBean.getcreated());
				//
				// Imageloader imageLoader = new
				// Imageloader(getSherlockActivity()
				// .getApplicationContext());
				// img.setTag(thisWeekBean.getvideo_image());
				// imageLoader.DisplayImage(thisWeekBean.getvideo_image(),
				// getActivity(), img);
				//
				// View.OnClickListener myhandler1 = new View.OnClickListener()
				// {
				// public void onClick(View v) {
				// // getChildFragmentManager()
				// // .beginTransaction()
				// // .replace(R.id.list_parent,
				// // ,
				// // DetailNews.TAG).commit();
				//
				// final FragmentTransaction ft = getFragmentManager()
				// .beginTransaction();
				// ft.remove(videoTerbaru.this);
				// newContainer.setTag(nid);
				// ft.replace(R.id.content, videoPlayer.newInstance(),
				// "videoPlayer");
				// // ft.replace(R.id.content, DetailNews.newInstance(),
				// // "videoPlayer");
				// ft.addToBackStack(null);
				//
				// ft.commit();
				//
				// }
				// };
				// cellViewMainLayout.setOnClickListener(myhandler1);
				//
				// lifePageCellContainerLayout.addView(cellViewMainLayout);

			}

			urls = stringArrayList.toArray(new String[stringArrayList.size()]);
			GridView gridView = (GridView) getView()
					.findViewById(R.id.gridview);
			gridView.setAdapter(new ImageAdapter(getActivity()));

			for (String s : urls)
				Log.d("9999999999999999999", s);
		}

	}
}