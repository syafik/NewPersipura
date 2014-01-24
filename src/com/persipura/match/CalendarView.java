package com.persipura.match;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.calenderBean;
import com.persipura.utils.WebHTTPMethodClass;
import com.persipura.main.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView extends SherlockFragment {

	public GregorianCalendar month, itemmonth;// calendar instances.
	private LayoutInflater mInflater;
	String date;
	LinearLayout lifePageCellContainerLayout;
	List<calenderBean> listThisWeekBean;
	ArrayList<String> stringArrayList = new ArrayList<String>();
	ExpandableHeightGridView gridview;
	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker

	public static final String TAG = CalendarView.class.getSimpleName();
    public ProgressDialog progressDialog;
	public static CalendarView newInstance() {
		return new CalendarView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		new fetchEventFromServer().execute("");
//		showProgressDialog();
		View rootView = inflater.inflate(R.layout.calendar, container, false);
		mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);


		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		gridview = (ExpandableHeightGridView) rootView.findViewById(R.id.gridview);
		gridview.setExpanded(true);

		gridview.setAdapter(adapter);

		GridView gridviewtitle = (GridView) rootView.findViewById(R.id.gridviewTitle);
		gridviewtitle.setAdapter(new TitleAdapter(getActivity()));

		// add days
		stringArrayList.add("S");
		stringArrayList.add("S");
		stringArrayList.add("R");
		stringArrayList.add("K");
		stringArrayList.add("J");
		stringArrayList.add("S");
		stringArrayList.add("M");
		
		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) rootView
				.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showProgressDialog();
				setPreviousMonth();
//				new fetchEventFromServer().execute("");
				refreshCalendar();
				
			}
		});

		RelativeLayout next = (RelativeLayout) rootView.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgressDialog();
				setNextMonth();
//				new fetchEventFromServer().execute("");
				refreshCalendar();
				
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

//				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					new fetchEventFromServer().execute("");
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					new fetchEventFromServer().execute("");
					refreshCalendar();
				}
//				((CalendarAdapter) parent.getAdapter()).setSelected(v);

//				showToast(selectedGridDate);

			}
		});
	

		return rootView;
	}
	

	public class TitleAdapter extends BaseAdapter {
	    private Context mContext;

	    public TitleAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return 7;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }
	    

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        TextView textView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	textView = new TextView(mContext);
	        	String item = stringArrayList.get(position);
	        	textView.setText(item);
	        	textView.setGravity(Gravity.CENTER);
	        	
	        	
	        	if(position == (stringArrayList.size() - 1)){
	        	textView.setTextColor(Color.RED);	
	        	}
	           
	        } else {
	        	textView = (TextView) convertView;
	        }

			
			
	        return textView;
	    }

	}

	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.calendar);
	// Locale.setDefault( Locale.US );
	// month = (GregorianCalendar) GregorianCalendar.getInstance();
	// itemmonth = (GregorianCalendar) month.clone();
	//
	// items = new ArrayList<String>();
	// adapter = new CalendarAdapter(this, month);
	//
	// GridView gridview = (GridView) findViewById(R.id.gridview);
	// gridview.setAdapter(adapter);
	//
	// handler = new Handler();
	// handler.post(calendarUpdater);
	//
	// TextView title = (TextView) findViewById(R.id.title);
	// title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	//
	// RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);
	//
	// previous.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// setPreviousMonth();
	// refreshCalendar();
	// }
	// });
	//
	// RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
	// next.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// setNextMonth();
	// refreshCalendar();
	//
	// }
	// });
	//
	// gridview.setOnItemClickListener(new OnItemClickListener() {
	// public void onItemClick(AdapterView<?> parent, View v,
	// int position, long id) {
	//
	// ((CalendarAdapter) parent.getAdapter()).setSelected(v);
	// String selectedGridDate = CalendarAdapter.dayString
	// .get(position);
	// String[] separatedTime = selectedGridDate.split("-");
	// String gridvalueString = separatedTime[2].replaceFirst("^0*",
	// "");// taking last part of date. ie; 2 from 2012-12-02.
	// int gridvalue = Integer.parseInt(gridvalueString);
	// // navigate to next or previous month on clicking offdays.
	// if ((gridvalue > 10) && (position < 8)) {
	// setPreviousMonth();
	// refreshCalendar();
	// } else if ((gridvalue < 7) && (position > 28)) {
	// setNextMonth();
	// refreshCalendar();
	// }
	// ((CalendarAdapter) parent.getAdapter()).setSelected(v);
	//
	// showToast(selectedGridDate);
	//
	// }
	// });
	// }

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
//			month.set((month.get(GregorianCalendar.YEAR) + 1),
//					month.getActualMaximum(GregorianCalendar.MONTH), 1);
//			month.set(GregorianCalendar.YEAR, month.get(GregorianCalendar.YEAR) + 1);
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
//			new fetchEventFromServer().execute("");
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
//			new fetchEventFromServer().execute("");
		}
	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
//			month.set(GregorianCalendar.YEAR, month.get(GregorianCalendar.YEAR) - 1);
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
//			new fetchEventFromServer().execute("");
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
//			new fetchEventFromServer().execute("");
		}

	}

	protected void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		Log.d("---------------------", "jjjjjjjjjjjjjjjjjjjjj");
		TextView title = (TextView) getView().findViewById(R.id.title);
		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		new fetchEventFromServer().execute("");
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			 items.clear();
			//
			// // Print dates of the current week
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			 String itemvalue;
			 for (int i = 0; i < 7; i++) {
			 itemvalue = df.format(itemmonth.getTime());
			 itemmonth.add(GregorianCalendar.DATE, 1);
			 items.add("2013-10-10");
			 items.add("2012-10-07");
			 items.add("2012-10-15");
			 items.add("2012-10-20");
			 items.add("2012-11-30");
			 items.add("2013-11-07");
			 }
			
//			 for (int i = 0; i < listThisWeekBean.size(); i++) {
//			 calenderBean thisWeekBean = listThisWeekBean.get(i);
//			 itemvalue = df.format(itemmonth.getTime());
//			 itemmonth.add(GregorianCalendar.DATE, 1);
//			
//			 items.add("2013-11-10");
//			 }

			 adapter.setItems(items);
			 adapter.notifyDataSetChanged();
		}
	};

	private class fetchEventFromServer extends AsyncTask<String, Void, String> {

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPreExecute() {
			String value2;
			String str1;
			java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("MM");
			int value1 = month.get(Calendar.YEAR);
			value2 = df1.format(month.getTime());	
			str1 = Integer.toString(value1);
			date = str1 + "-" + value2;
			
			Log.d("dateTime", "dateTime : " + date);
		}

		@Override
		protected String doInBackground(String... params) {

//			String result = WebHTTPMethodClass.httpGetServiceWithoutparam(
////					"/restapi/get/match_event", "yearmonth=" + date);
//					"/restapi/get/match_event");
			
			String result = WebHTTPMethodClass.httpGetService(
				"/restapi/get/match_event", "yearmonth[value][date]=" + date);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<calenderBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					calenderBean thisWeekBean = new calenderBean();
					thisWeekBean.setId(resObject.getString("id"));
					thisWeekBean.setTitle(resObject.getString("title"));
					thisWeekBean.setType(resObject.getString("type"));
					thisWeekBean.setDatetime(resObject.getString("datetime"));
					thisWeekBean.setLeague(resObject.getString("league"));
					thisWeekBean.setHTeam(resObject.getString("h_team"));
					thisWeekBean.setHGoal(resObject.getString("h_goal"));
					thisWeekBean.setATeam(resObject.getString("a_team"));
					thisWeekBean.setAGoal(resObject.getString("a_goal"));
					thisWeekBean.seth_logo(resObject.getString("h_logo"));
					thisWeekBean.seta_logo(resObject.getString("a_logo"));
					listThisWeekBean.add(thisWeekBean);
				}
				
				createSelectLocationListView(listThisWeekBean);
					
				
				
				if(progressDialog != null){
					progressDialog.dismiss();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<calenderBean> listThisWeekBean) {
			items.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;

			for (int i = 0; i < listThisWeekBean.size(); i++) {
				calenderBean thisWeekBean = listThisWeekBean.get(i);
				
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				
				String x = thisWeekBean.getDatetime();
				Log.d("------------------", x.substring(0,10));
				items.add(x.substring(0,10));
				Log.d("date", "date Event : " + x.substring(0,10));
				
			}

			adapter.setItems(items);
			adapter.setCollections(listThisWeekBean);
			adapter.notifyDataSetChanged();
			

		}
	}

}
