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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.HasilBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;



public class Formasi extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HasilBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private ProgressDialog progressDialog;
	
	public static final String TAG = Formasi.class
	.getSimpleName();

	public static Formasi newInstance() {
		return new Formasi();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showProgressDialog();
//		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.sejarah, container,
				false);
		 mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
	
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
		String result = WebHTTPMethodClass.httpGetServiceWithoutparam("/restapi/get/match_results");
		return result;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		
	}

	@Override
	protected void onPostExecute(String result) {
		
			try {

	            JSONArray jsonArray = new JSONArray(result);
	            
	            listThisWeekBean = new ArrayList<HasilBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
	                JSONObject resObject = jsonArray.getJSONObject(i);
	                HasilBean thisWeekBean = new HasilBean();
	                thisWeekBean.setId(resObject.getString("id"));
	                thisWeekBean.setTime(resObject.getString("time"));
	                thisWeekBean.setDate(resObject.getString("date"));
	                thisWeekBean.setHlogo(resObject.getString("h_logo"));
	                thisWeekBean.setAlogo(resObject.getString("a_logo"));
	                thisWeekBean.setHteam(resObject.getString("h_team"));
	                thisWeekBean.setAteam(resObject.getString("a_team"));
	                thisWeekBean.setPlace(resObject.getString("place"));
//	                ProgressDialog pd = new ProgressDialog(getActivity());
//	                pd.dismiss();
					listThisWeekBean.add(thisWeekBean);
	            }
				if (listThisWeekBean != null
						&& listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@SuppressWarnings("deprecation")
	private void createSelectLocationListView(
			List<HasilBean> listThisWeekBean) {
		for (int i = 0; i < listThisWeekBean.size(); i++) {
			HasilBean thisWeekBean = listThisWeekBean.get(i);
			
				View cellViewMainLayout = mInflater
					.inflate(R.layout.jadwal_pertandingan_list, null);
				TextView ListDate = (TextView) cellViewMainLayout
						.findViewById(R.id.list_date);
				TextView ListTime = (TextView) cellViewMainLayout
						.findViewById(R.id.list_time);
				TextView cellnumTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_cellnum);
				TextView NameTeamA = (TextView) cellViewMainLayout
						.findViewById(R.id.name_team1);
				TextView NameTeamB = (TextView) cellViewMainLayout
						.findViewById(R.id.name_team2);
				TextView Place = (TextView) cellViewMainLayout
						.findViewById(R.id.text_stadiun);
				ImageView imgTeamA = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);
				ImageView imgTeamB = (ImageView) cellViewMainLayout
						.findViewById(R.id.ImageTeam2);
				
				ListDate.setText("");
				ListTime.setText("");
				NameTeamA.setText("");
				NameTeamB.setText("");
				Place.setText("");
				cellnumTextView.setText("");
				
				ListDate.setText(thisWeekBean.getDate());
				ListTime.setText(thisWeekBean.getTime());
				NameTeamA.setText(thisWeekBean.getHteam());
				NameTeamB.setText(thisWeekBean.getAteam());
				Place.setText(thisWeekBean.getPlace());
			
//		        BitmapFactory.Options bmOptions;
//					bmOptions = new BitmapFactory.Options();
//				        bmOptions.inSampleSize = 1;
//					Bitmap bm = loadBitmap(thisWeekBean.getHlogo(), bmOptions);
//					imgTeamA.setImageBitmap(bm);
//				
//					Bitmap bm2 = loadBitmap(thisWeekBean.getAlogo(), bmOptions);
//					imgTeamB.setImageBitmap(bm2);
				
				Imageloader imageLoader = new Imageloader(getSherlockActivity().getApplicationContext());
				imgTeamA.setTag(thisWeekBean.getHlogo());
				imageLoader.DisplayImage(thisWeekBean.getHlogo(),getActivity(),imgTeamA);
				
				imgTeamB.setTag(thisWeekBean.getAlogo());
				imageLoader.DisplayImage(thisWeekBean.getAlogo(),getActivity(),imgTeamB);
						
				lifePageCellContainerLayout.addView(cellViewMainLayout);
				
			
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
