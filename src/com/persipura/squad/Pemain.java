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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.HasilBean;
import com.persipura.home.HomeSquad;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;



public class Pemain extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HomeSquad> listSquadBean;
	LinearLayout squadContainerLayout;
	private ProgressDialog progressDialog;
	
	public static final String TAG = Pemain.class
	.getSimpleName();

	public static Pemain newInstance() {
		return new Pemain();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.pemain, container,
				false);
		 mInflater = getLayoutInflater(savedInstanceState);

		 squadContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.squad_home);
	
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
//		ProgressDialog pd = new ProgressDialog(getActivity());
//		pd.setMessage("loading");
//		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String result = WebHTTPMethodClass.httpGetServiceWithoutparam("/restapi/get/squad?tim_staf=tim");
		return result;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		
	}

	@Override
	protected void onPostExecute(String result) {
			
			try {

	            JSONArray jsonArray = new JSONArray(result);
				squadContainerLayout.removeAllViews();

	            listSquadBean = new ArrayList<HomeSquad>();
				for (int i = 0; i < jsonArray.length(); i++) {
	                JSONObject resObject = jsonArray.getJSONObject(i);
//	                HomeSquad thisWeekBean = new HomeSquad();
	                
//	                thisWeekBean.setNama(resObject.getString("nama"));
//	                thisWeekBean.setNamaLengkap(resObject.getString("nama_lengkap"));
//	                thisWeekBean.setno_punggung(resObject.getString("no_punggung"));
//	                thisWeekBean.setwarganegara(resObject.getString("kewarganegaraan"));
//	                thisWeekBean.setberat_badan(resObject.getString("berat_badan"));
//	                thisWeekBean.settinggi_badan(resObject.getString("tinggi_badan"));
//	                thisWeekBean.setposisi(resObject.getString("posisi"));
//	                thisWeekBean.settanggal_lahir(resObject.getString("tanggal_lahir"));
//	                thisWeekBean.settahun_aktif(resObject.getString("tahun_aktif"));
//	                thisWeekBean.settim_staf(resObject.getString("tim_staf"));
//	                thisWeekBean.setinfo(resObject.getString("info"));
//	                thisWeekBean.setfoto(resObject.getString("foto"));
	                HomeSquad homeSquad = new HomeSquad();
	                homeSquad.setId(resObject.getString("id"));
					homeSquad.setNamaLengkap(resObject.getString("nama_lengkap"));
					homeSquad.setposisi(resObject.getString("posisi"));
					homeSquad.setage(resObject.getString("age"));
					homeSquad.setwarganegara(resObject.getString("kewarganegaraan"));
					homeSquad.setfoto(resObject.getString("foto"));
					homeSquad.setno_punggung(resObject.getString("no_punggung"));
//	                ProgressDialog pd = new ProgressDialog(getActivity());
//	                pd.dismiss();
	                listSquadBean.add(homeSquad);
	            }
				if (listSquadBean != null
						&& listSquadBean.size() > 0) {

					createSelectLocationListView(listSquadBean);
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void createSelectLocationListView(
			List<HomeSquad> listThisWeekBean) {
		for (int i = 0; i < listThisWeekBean.size(); i++) {
			HomeSquad squad = listSquadBean.get(i);
			View cellViewMainLayout = mInflater.inflate(R.layout.squad_list,
					null);
			RelativeLayout wrapper = (RelativeLayout) cellViewMainLayout.findViewById(R.id.wrapper);
			wrapper.setBackground(getResources().getDrawable(R.drawable.gradient_box));
			TextView nama = (TextView) cellViewMainLayout
					.findViewById(R.id.textViewList2);
			TextView detail = (TextView) cellViewMainLayout
					.findViewById(R.id.textView1);
			TextView no_punggung = (TextView) cellViewMainLayout
					.findViewById(R.id.no_punggung);
			
			ImageView imgNews = (ImageView) cellViewMainLayout
					.findViewById(R.id.imageViewList2);

			nama.setText("");
			detail.setText("");
			no_punggung.setText("");

			no_punggung.setText(squad.getno_punggung());
			nama.setText(squad.getNamaLengkap());
			detail.setText(squad.getposisi() + "\n" + squad.getage() + " tahun" + ", " + squad.getwarganegara());
			BitmapFactory.Options bmOptions;

			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;
//			Bitmap bm = loadBitmap(thisWeekBean.getimg_uri(), bmOptions);
//			
//			
//			imgNews.setImageBitmap(bm);
			int loader = R.drawable.loader;

			ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

			imgLoader.DisplayImage(squad.getfoto(), loader, imgNews);
			
			squadContainerLayout.addView(cellViewMainLayout);
				
			
		}
	}

	}
	
	public static Bitmap loadBitmap(String imgurl, BitmapFactory.Options options) {
		try {
		    if (android.os.Build.VERSION.SDK_INT > 9) {
		        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		        StrictMode.setThreadPolicy(policy);
		      }
		    
	        URL url = new URL(imgurl);
			InputStream in = url.openConnection().getInputStream(); 
	        BufferedInputStream bis = new BufferedInputStream(in,1024*8);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        int len=0;
	        byte[] buffer = new byte[1024];
	        while((len = bis.read(buffer)) != -1){
	            out.write(buffer, 0, len);
	        }
	        out.close();
	        bis.close();

	        byte[] data = out.toByteArray();
	        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	        return bitmap;
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	        return null;

	    }
	}
	
}
