package com.webileapps.navdrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.FooterBean;
import com.persipura.bean.NewsBean;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;

@SuppressLint("NewApi")
public class DetailNews extends SherlockFragment {
	public static final String TAG = DetailNews.class.getSimpleName();
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	List<FooterBean> listFooterBean;
	FrameLayout footerLayout;
	String LinkId;
	RelativeLayout lifePageCellContainerLayout;
	ViewGroup newContainer;
	String nid;
	ProgressDialog progressDialog;
	int failedRetrieveCount = 0;
	MainActivity attachingActivityLock;
	
	public static DetailNews newInstance() {
		return new DetailNews();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachingActivityLock = (MainActivity) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		attachingActivityLock = null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// nid = (String) container.getTag();
		Bundle extras = getArguments();
		nid = extras.getString("NewsId");
		showProgressDialog();

		new fetchLocationFromServer().execute("");
		new fetchFooterFromServer().execute("");

		View rootView = inflater.inflate(R.layout.detail_news2, container,
				false);

		mInflater = getLayoutInflater(savedInstanceState);
		newContainer = container;
		lifePageCellContainerLayout = (RelativeLayout) rootView
				.findViewById(R.id.list_parent);
		footerLayout = (FrameLayout) rootView
				.findViewById(R.id.bottom_control_bar);
		TextView footerTitle = (TextView) rootView
				.findViewById(R.id.footerText);
		AppConstants.fontrobotoTextView(footerTitle, 16, "ffffff",
				getActivity().getApplicationContext().getAssets());
		MainActivity.getInstance().HideOtherActivities();
		MainActivity.getInstance().changeItemSearchToShare(true);
		
		
		return rootView;
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(attachingActivityLock);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
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
					"/restapi/get/news", "id=" + nid);

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
					thisWeekBean.setNid(resObject.getString("nid"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setteaser(resObject.getString("body"));
					String img = resObject.getString("big_img");
					
					if(img != null && !img.isEmpty() && img != "null"){
						img = resObject.getString("big_img");
					}else{
						img = resObject.getString("img_uri");
					}
					thisWeekBean.setimg_uri(img);
					thisWeekBean.setcreated(resObject.getString("created"));
					thisWeekBean.setshared_url(resObject.getString("share_url"));

					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;

			}
		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<NewsBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				NewsBean thisWeekBean = listThisWeekBean.get(i);
				View cellViewMainLayout = mInflater.inflate(
						R.layout.detail_news2, null);
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
				
				AppConstants.fontrobotoTextViewBold(titleNews, 15, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(descNews, 12, "ffffff",
						getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(time, 11, "cccccc",
						getActivity().getApplicationContext().getAssets());
				
				BitmapFactory.Options bmOptions;

				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				int loader = R.drawable.loader;
				ImageLoader imgLoader = new ImageLoader(getActivity()
						.getApplicationContext());

				imgLoader.DisplayImage(thisWeekBean.getimg_uri(), loader,
						imgNews);
				MainActivity.getInstance().setFacebookContentShare(thisWeekBean.gettitle(), thisWeekBean.getshared_url());
				lifePageCellContainerLayout.addView(cellViewMainLayout);

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
				
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

			} catch (Exception e) {
				e.printStackTrace();
				failedRetrieveCount++;
			}
			
			if (failedRetrieveCount > 0) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					Toast.makeText(
							attachingActivityLock.getApplicationContext(),
							"Failed to retrieve data from server",
							Toast.LENGTH_LONG).show();
				}
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "onDestroyCalled");
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("onPause", "onPauseCalled");
		if (progressDialog != null) {
			progressDialog.dismiss();

		}
	}

}
