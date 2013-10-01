package com.persipura.squad;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.NewsBean;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

public class DetailSquad extends SherlockFragment {
	public static final String TAG = DetailSquad.class.getSimpleName();

	public static DetailSquad newInstance() {
		return new DetailSquad();
	}
	
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	RelativeLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		nid = (String) container.getTag();

		Log.d("tagID", "tagID : " + container.getTag());
		
		new fetchLocationFromServer().execute("");

		View rootView = inflater.inflate(R.layout.squad_profile, container,
				false);
		
		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (RelativeLayout) rootView
				.findViewById(R.id.list_parent);

		return rootView;
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/squad", "id="+nid);    


			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("result", "result : " + result);
			
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
					thisWeekBean.setcreated(resObject.getString("created"));

					listThisWeekBean.add(thisWeekBean);
				}
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
				List<NewsBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				NewsBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(R.layout.detail_squad,
						null);
				TextView titleNews = (TextView) cellViewMainLayout
						.findViewById(R.id.title);
				TextView descNews = (TextView) cellViewMainLayout
						.findViewById(R.id.desc);
				TextView time = (TextView) cellViewMainLayout
						.findViewById(R.id.time);
				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				titleNews.setText("");
				descNews.setText("");
				time.setText("");
				cellViewMainLayout.setTag(thisWeekBean.getNid());

				titleNews.setText(thisWeekBean.gettitle());
				descNews.setText(Html.fromHtml(thisWeekBean.getteaser()));
				time.setText(thisWeekBean.getcreated());
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				Bitmap bm = loadBitmap(thisWeekBean.getimg_uri(), bmOptions);

				imgNews.setImageBitmap(bm);


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
