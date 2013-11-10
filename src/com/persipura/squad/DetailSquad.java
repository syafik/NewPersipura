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
import com.persipura.home.Home;
import com.persipura.home.HomeSquad;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

public class DetailSquad extends SherlockFragment {
	public static final String TAG = DetailSquad.class.getSimpleName();

	public static DetailSquad newInstance() {
		return new DetailSquad();
	}

	private LayoutInflater mInflater;
	List<HomeSquad> listThisWeekBean;
	List<FooterBean> listFooterBean;
	FrameLayout footerLayout;
	String LinkId;

	LinearLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		nid = (String) container.getTag();
		if (nid == null) {
			nid = getArguments().getString("squadId");

		}
		showProgressDialog();

		new fetchLocationFromServer().execute("");
		new fetchFooterFromServer().execute("");

		View rootView = inflater.inflate(R.layout.squad_profile, container,
				false);

		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.list_parent);
		footerLayout = (FrameLayout) rootView.findViewById(R.id.bottom_control_bar);

		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff", getActivity()
				.getApplicationContext().getAssets());
		MainActivity.getInstance().HideOtherActivities();
		getActivity().getSupportFragmentManager().findFragmentByTag(Home.TAG).getView().setVisibility(View.GONE);

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
					"/restapi/get/squad", "id=" + nid);

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

				listThisWeekBean = new ArrayList<HomeSquad>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HomeSquad thisWeekBean = new HomeSquad();
					thisWeekBean.setNamaLengkap(resObject
							.getString("nama_lengkap"));
					thisWeekBean.setNama(resObject.getString("nama"));
					thisWeekBean.setno_punggung(resObject
							.getString("no_punggung"));
					thisWeekBean.setberat_badan(resObject
							.getString("berat_badan"));
					thisWeekBean.settinggi_badan(resObject
							.getString("tinggi_badan"));
					thisWeekBean.setage(resObject.getString("age"));
					thisWeekBean.settanggal_lahir(resObject
							.getString("tanggal_lahir"));
					thisWeekBean.setwarganegara(resObject
							.getString("kewarganegaraan"));
					thisWeekBean.setposisi(resObject.getString("posisi"));
					thisWeekBean.setfoto(resObject.getString("foto"));

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
				List<HomeSquad> listThisWeekBean) {
			lifePageCellContainerLayout.removeAllViews();
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HomeSquad thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(
						R.layout.detail_squad, null);
				TextView nama_lengkap = (TextView) cellViewMainLayout
						.findViewById(R.id.nama);
				TextView nama = (TextView) cellViewMainLayout
						.findViewById(R.id.textViewList2);
				TextView no_punggung = (TextView) cellViewMainLayout
						.findViewById(R.id.no_punggung);

				TextView detailheader = (TextView) cellViewMainLayout
						.findViewById(R.id.textView1);
				TextView tanggal_lahir = (TextView) cellViewMainLayout
						.findViewById(R.id.tanggal_lahir);
				TextView kewarganegaraan = (TextView) cellViewMainLayout
						.findViewById(R.id.kewarganegaraan);
				TextView tinggi_badan = (TextView) cellViewMainLayout
						.findViewById(R.id.tinggi_badan);
				TextView berat_badan = (TextView) cellViewMainLayout
						.findViewById(R.id.berat_badan);
				ImageView imgNews = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageViewList2);

				nama_lengkap.setText("");
				tanggal_lahir.setText("");
				kewarganegaraan.setText("");
				tinggi_badan.setText("");
				berat_badan.setText("");

				nama_lengkap.setText(thisWeekBean.getNamaLengkap());
				tanggal_lahir.setText(thisWeekBean.gettanggal_lahir());
				kewarganegaraan.setText(thisWeekBean.getwarganegara());
				tinggi_badan.setText(thisWeekBean.gettinggi_badan() + " cm");
				berat_badan.setText(thisWeekBean.getberat_badan() + " kg");
				no_punggung.setText(thisWeekBean.getno_punggung());
				nama.setText(thisWeekBean.getNama());
				detailheader.setText(thisWeekBean.getposisi() + "\n"
						+ thisWeekBean.getage() + ", "
						+ thisWeekBean.getwarganegara());

				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				Bitmap bm = loadBitmap(thisWeekBean.getfoto(), bmOptions);

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
	
	private class fetchFooterFromServer extends
	AsyncTask<String, Void, String> {

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
			thisWeekBean.setfooter_logo(resObject.getString("footer_logo"));
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

		if(!thisWeekBean.getfooter_logo().isEmpty()){
			imgLoader.DisplayImage(thisWeekBean.getfooter_logo(), loader,
					imgNews);

			LinkId = null;
			LinkId = thisWeekBean.getlink();
			Log.d("clickable", "clickable : " + thisWeekBean.getclickable());
			if(thisWeekBean.getclickable().equals("1")){
				
			 imgNews.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {

                	 Uri uri = Uri.parse(LinkId);
                	 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                	 startActivity(intent);

                 }
             });
	
			}
	
		}
		

	}
}

}

}
