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
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;


public class GaleryView2 extends SherlockFragment {
	// ---the images to display---


	public String[] urls=null;
	private String[] imgDescriptions=null;
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
	ImageView bigImg;
	Button backBtn;
	GridView gridView;
	TextView galleryDesc;


	public static final String TAG = GaleryView2.class.getSimpleName();

	public static GaleryView2 newInstance() {
		return new GaleryView2();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bundle b = getArguments();
		nid = b.getString("myString");
		new fetchImage().execute("");
		new fetchFooterFromServer().execute("");
		showProgressDialog();
		newContainer = container;
		View rootView = inflater.inflate(R.layout.gridview, container,
				false);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		bigImg = (ImageView) rootView.findViewById(R.id.detailGaleryImg);
		mInflater = getLayoutInflater(savedInstanceState);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		backBtn = (Button) rootView.findViewById(R.id.back);
		galleryDesc = (TextView) rootView.findViewById(R.id.galleryDesc);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				bigImg.setVisibility(View.GONE);
				galleryDesc.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
				
			}
		});
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				getActivity().getApplicationContext().getAssets());
		MainActivity.getInstance().HideOtherActivities();
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

	private class fetchImage extends AsyncTask<String, Void, String> {

		
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
				
				Log.d("parts", "parts length : " + parts.length);
				int part_length = 12;
				if(part_length >= parts.length){
					part_length = parts.length;
				}
				String[] picturetitle = thisWeekBean.getPictureTitle().split("\\|");
				Log.d("parts", "parts : " + parts);
				for (int x = 0; x < part_length; x++) {
					stringArrayList.add(parts[x]);
					stringTitleList.add(picturetitle[x]);
				}
				

			}

			
			urls = stringArrayList.toArray(new String[stringArrayList.size()]);
			imgDescriptions = stringTitleList.toArray(new String[stringTitleList.size()]);
			gridView = (GridView) getView()
					.findViewById(R.id.gridview);
		    gridView.setAdapter(new ImageAdapter(getActivity()));

//			for(int a = 0; a < urls.length; a++){
//				String url = urls[0];
//				ImageView img = new ImageView(getActivity());
//				int loader = R.drawable.image_thumb_4;
//				ImageLoader imgLoader = new ImageLoader(getActivity()
//						.getApplicationContext());
//
//				imgLoader.DisplayImage(url, loader,
//						img);
//				gridView.addView(img);
//			}
			

		}
		
		public class ImageAdapter extends BaseAdapter {
		    private Context mContext;
			private ImageLoader imgLoader;

		    public ImageAdapter(Context c) {
		        mContext = c;
		    }

		    public int getCount() {
		        return urls.length;
		    }

		    public Object getItem(int position) {
		        return null;
		    }

		    public long getItemId(int position) {
		        return 0;
		    }

		    // create a new ImageView for each item referenced by the Adapter
		    public View getView(int position, View convertView, ViewGroup parent) {
		        ImageView imageView;
		        if (convertView == null) {  // if it's not recycled, initialize some attributes
		            imageView = new ImageView(mContext);
		            imageView.setLayoutParams(new GridView.LayoutParams(120, 110));
		            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		            imageView.setPadding(8, 8, 8, 8);
		            imageView.setTag(urls[position]);
		            imageView.setContentDescription(imgDescriptions[position]);
		           
		        } else {
		            imageView = (ImageView) convertView;
		        }

				final int loader = R.drawable.image_thumb_4;
				imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());
				
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						imgLoader.DisplayImage(v.getTag().toString(), loader,
								bigImg);
						backBtn.setVisibility(View.VISIBLE);
						bigImg.setVisibility(View.VISIBLE);
						galleryDesc.setText(v.getContentDescription().toString());
						galleryDesc.setVisibility(View.VISIBLE);
						gridView.setVisibility(View.GONE);
						
					}
				});
				imgLoader.DisplayImage(urls[position], loader,
						imageView);
		        return imageView;
		    }

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