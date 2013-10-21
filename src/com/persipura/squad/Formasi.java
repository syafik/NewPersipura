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

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.HasilBean;
import com.persipura.bean.SejarahBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

public class Formasi extends SherlockFragment {

	private LayoutInflater mInflater;
	List<SejarahBean> listThisWeekBean;
	RelativeLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;

	public static final String TAG = Formasi.class.getSimpleName();

	public static Formasi newInstance() {
		return new Formasi();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		showProgressDialog();
		 new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.sejarah, container, false);
		mInflater = getLayoutInflater(savedInstanceState);
		
		lifePageCellContainerLayout = (RelativeLayout) rootView.findViewById(R.id.lifePageCellContainerLayout);
		

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
			// ProgressDialog pd = new ProgressDialog(getActivity());
			// pd.setMessage("loading");
			// pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/page", "slug=formasi");
			
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {

				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<SejarahBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					SejarahBean thisWeekBean = new SejarahBean();
					thisWeekBean.setdesc(resObject.getString("body"));
					// ProgressDialog pd = new ProgressDialog(getActivity());
					// pd.dismiss();
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
				List<SejarahBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				SejarahBean thisWeekBean = listThisWeekBean.get(i);

//				TextView desc = (TextView) lifePageCellContainerLayout.findViewById(R.id.textViewList2);

//				desc.setText(Html.fromHtml(thisWeekBean.getdesc()));
				WebView webview = (WebView) lifePageCellContainerLayout.findViewById(R.id.webview);

				 // OR, you can also load from an HTML string:
				 String summary = thisWeekBean.getdesc();
				 webview.loadData(summary, "text/html", null);


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
