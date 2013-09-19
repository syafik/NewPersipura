package com.webileapps.navdrawer;



import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockFragment;

import com.persipura.bean.NewsBean;
import com.persipura.utils.*;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class News extends SherlockFragment {
//	private static String url = "http://prspura.tk/restapi/get/news?limit=20&offset=1";
	private LayoutInflater mInflater;
	List<NewsBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	private RelativeLayout mParentLayout;
	
	// JSON Node names
//	private static final String NID = "nid";
//	private static final String CREATED = "created";
//	private static final String CATEGORY = "category";
//	private static final String TITLE = "title";
//	private static final String IMG_URI = "img_uri";
//	private static final String TEASER = "teaser";
		
	public static final String TAG = News.class
	.getSimpleName();

	public static News newInstance() {
		return new News();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.news, container,
				false);
		
//		mParentLayout = (RelativeLayout) mInflater.inflate(
//				R.layout.news, null);
		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
		
//		ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();
//		 
//		JSONParser jParser = new JSONParser();
//		// getting JSON string from URL
//		JSONObject json = jParser.getJSONFromUrl(url);
//		try {
//		    // Getting Array of Contacts
////		    news = json.getJSONArray(TAG_CONTACTS);
//
//		    // looping through All Contacts
//		    for(int i = 0; i < json.length(); i++){
////		        JSONObject c = contacts.getJSONObject(i);
//
//		        // Storing each json item in variable
//		        String id = json.getString(NID);
//		        String created = json.getString(CREATED);
//		        String category = json.getString(CATEGORY);
//		        String title = json.getString(TITLE);
//		        String teaser = json.getString(TEASER);
//
//		        HashMap<String, String> map = new HashMap<String, String>();
//                
//                // adding each child node to HashMap key => value
//                map.put(NID, id);
//                map.put(CREATED, created);
//                map.put(CATEGORY, category);
//                map.put(TITLE, title);
//                map.put(TEASER, teaser);
//                newsList.add(map);
//                
//		    }
//		} catch (JSONException e) {
//		    e.printStackTrace();
//		}
		
		
//		ListAdapter adapter = new SimpleAdapter(this, newsList , R.layout.news_list, new String[] { TITLE, IMG_URI, TEASER }, new int[]{R.id.findzoes_list_text_name, R.id.imageView1, R.id.findzoes_list_text_address});
// 
//        setListAdapter(adapter);
// 
//        // selecting single ListView item
//        ListView lv = getListView();
// 
//        // Launching new screen on Selecting Single ListItem
//        lv.setOnItemClickListener(new OnItemClickListener() {
// 
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                    int position, long id) {
//                // getting values from selected ListItem
//                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
//                String cost = ((TextView) view.findViewById(R.id.email)).getText().toString();
//                String description = ((TextView) view.findViewById(R.id.mobile)).getText().toString();
//                 
//                // Starting new intent
//                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
//                in.putExtra(TAG_NAME, name);
//                in.putExtra(TAG_EMAIL, cost);
//                in.putExtra(TAG_PHONE_MOBILE, description);
//                startActivity(in);
//            }
//        });
    
	
	
//			RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
//					.inflate(R.layout.news_list, null);
//			TextView nameTextView = (TextView) cellViewMainLayout
//					.findViewById(R.id.findzoes_list_text_name);
//			TextView addressTextView = (TextView) cellViewMainLayout
//					.findViewById(R.id.findzoes_list_text_address);
//			TextView cellnumTextView = (TextView) cellViewMainLayout
//					.findViewById(R.id.findzoes_list_text_cellnum);
//		
//			nameTextView.setText("");
//			addressTextView.setText("");
//			cellnumTextView.setText("");
//			
//
//			nameTextView.setText(TITLE);
//			addressTextView.setText(TEASER);
		
	    
		return rootView;
	}
	
	
private class fetchLocationFromServer extends
AsyncTask<String, Void, String> {

@Override
protected void onPreExecute() {
	
}

@Override
protected String doInBackground(String... params) {
	String result = WebHTTPMethodClass.httpGetService("/restapi/get/news",
			"limit=20" + "&offset=1");
	
	return result;
}

@Override
protected void onProgressUpdate(Void... values) {

}

@Override
protected void onPostExecute(String result) {
	Log.d("-----------------", result);
//	if (result != null && !result.equals("")) {
		
		try {
			JSONObject resObject = new JSONObject(result);
			Log.d("-----------------", "asdfghjkl");
//				JSONArray jsonArray = resObject
//						.getJSONArray("restaurants");
				listThisWeekBean = new ArrayList<NewsBean>();
				for (int i = 0; i < resObject.length(); i++) {
					NewsBean thisWeekBean = new NewsBean();
					thisWeekBean.setNid(resObject.getString("nid"));
					thisWeekBean.settitle(resObject.getString("title"));
					thisWeekBean.setteaser(resObject.getString("teaser"));
					
					
					// String sucess1 =
					// resObject1.getString("close_at");
					
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null
						&& listThisWeekBean.size() > 0) {
					// createSeqUnderLine(lifePageCellContainerLayout);
					createSelectLocationListView(listThisWeekBean);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//	}
}

private void createSelectLocationListView(
		List<NewsBean> listThisWeekBean) {
	for (int i = 0; i < listThisWeekBean.size(); i++) {
		NewsBean thisWeekBean = listThisWeekBean.get(i);
		
			RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
					.inflate(R.layout.news_list, null);
			TextView nameTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_name);
			TextView addressTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_address);
			TextView cellnumTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_cellnum);
			
			nameTextView.setText("");
			addressTextView.setText("");
			cellnumTextView.setText("");
			

			nameTextView.setText(thisWeekBean.gettitle());
			addressTextView.setText(thisWeekBean.getteaser());

		
			lifePageCellContainerLayout.addView(cellViewMainLayout);
			
		
	}
}

}
	
}