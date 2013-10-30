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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.HasilBean;
import com.persipura.bean.SejarahBean;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

public class Sejarah extends SherlockFragment {

	private LayoutInflater mInflater;
	List<SejarahBean> listThisWeekBean;
	RelativeLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;

	public static final String TAG = Sejarah.class.getSimpleName();

	public static Sejarah newInstance() {
		return new Sejarah();
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
					"/restapi/get/page", "slug=sejarah");
			
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
//				WebView webview = (WebView) lifePageCellContainerLayout.findViewById(R.id.webview);
//
//				 // OR, you can also load from an HTML string:
				 String summary = thisWeekBean.getdesc();
//				 String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"cssSejarah.css\" />" + summary;
//				 webview.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
				 
				 
				 
				 
				 // new code here
				 ImageView imageView1 = (ImageView) lifePageCellContainerLayout.findViewById(R.id.imageView1);
				 	int loader = R.drawable.loader;
				 TextView nama = (TextView) lifePageCellContainerLayout.findViewById(R.id.nama);
				 TextView berdiri = (TextView) lifePageCellContainerLayout.findViewById(R.id.berdiri);
				 TextView alamat = (TextView) lifePageCellContainerLayout.findViewById(R.id.alamat);
				 TextView telepon = (TextView) lifePageCellContainerLayout.findViewById(R.id.telepon);
				 TextView julukan = (TextView) lifePageCellContainerLayout.findViewById(R.id.julukan);
				 TextView detail = (TextView) lifePageCellContainerLayout.findViewById(R.id.textViewList2);
				 LinearLayout detailLinear = (LinearLayout) lifePageCellContainerLayout.findViewById(R.id.detail);
				 RelativeLayout relDetail = (RelativeLayout) lifePageCellContainerLayout.findViewById(R.id.relDetail);
				 TableLayout tableLayout1 = (TableLayout) lifePageCellContainerLayout.findViewById(R.id.tableLayout1);
				 
				 // unhide content
				 imageView1.setVisibility(View.VISIBLE);
				 nama.setVisibility(View.VISIBLE);
				 berdiri.setVisibility(View.VISIBLE);
				 alamat.setVisibility(View.VISIBLE);
				 telepon.setVisibility(View.VISIBLE);
				 julukan.setVisibility(View.VISIBLE);
				 detail.setVisibility(View.VISIBLE);
				 detailLinear.setVisibility(View.VISIBLE);	
				 relDetail.setVisibility(View.VISIBLE);
				 tableLayout1.setVisibility(View.VISIBLE);
				 
				 
				 // parsing html from string
				 Document doc = Jsoup.parse(summary);
				 Element logo = doc.select("img").first();
				 Element p1 = doc.select("p").first();
				 Element table_html = doc.select("table").first();
				 
				 
				 // table element
				 String nama_html = doc.select("td").get(2).text();
				 String berdiri_html = doc.select("td").get(5).text();
				 String alamat_html = doc.select("td").get(8).text();
				 String telepon_html = doc.select("td").get(11).text();
				 String julukan_html = doc.select("td").get(14).text();
				 p1.remove();
				 table_html.remove();
				 
				 nama.setText(nama_html);
				 berdiri.setText(berdiri_html);
				 alamat.setText(alamat_html);
				 telepon.setText(telepon_html);
				 julukan.setText(julukan_html);

				 for(int a = 1; a <= 15; a++){ 
					 Log.d("avalue", "A value : " + a);
				     TextView tv = (TextView) lifePageCellContainerLayout.findViewWithTag("textView" + a);
				     AppConstants.fontrobotoTextViewBold((TextView) tv, 11, "ffffff",
							getActivity().getApplicationContext().getAssets());
				 }
				 
				 AppConstants.fontrobotoTextView((TextView)detail, 11, "ffffff",
							getActivity().getApplicationContext().getAssets());
			
				  
				 
				 
				 
  				 ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());
				 imgLoader.DisplayImage(logo.attr("src"), loader, imageView1);
				 detail.setText(Html.fromHtml(doc.outerHtml()));

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
