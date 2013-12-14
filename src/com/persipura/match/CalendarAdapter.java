package com.persipura.match;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.persipura.main.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidhive.imagefromurl.ImageLoader;
import com.persipura.bean.calenderBean;
import com.persipura.match.*;
public class CalendarAdapter extends BaseAdapter{
	private CalendarView mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	ArrayList<String> stringArrayList = new ArrayList<String>();;

	DateFormat df;

	private ArrayList<String> items;
	List<calenderBean> collections;

	public static List<String> dayString;
	private View previousView;

	public CalendarAdapter(CalendarView calendarView, GregorianCalendar monthCalendar) {
		CalendarAdapter.dayString = new ArrayList<String>();
		 Locale.setDefault( Locale.US );
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = calendarView;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		this.collections = new ArrayList<calenderBean>(); 
		
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}

	public void setItems(ArrayList<String> items) {
		stringArrayList.clear();
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				stringArrayList.add(items.get(i).split("/")[0]);
				items.set(i, "0" + items.get(i));
				
			}
		}
		Log.d("this.items", "this.items : " + this.items);
		this.items = items;
		
	}
	

	public void setCollections(List<calenderBean> listThisWeekBean) {
		// TODO Auto-generated method stub
		this.collections = listThisWeekBean;
		Log.d("this.collections", "this.collections : " + this.collections.size());
	}

	public int getCount() {
		return dayString.size();
	}

	public Object getItem(int position) {
		return dayString.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
//		Log.d("collections", "collections results : " + collections);
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		
		
		dayView = (TextView) v.findViewById(R.id.date);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			dayView.setTextColor(Color.RED);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.RED);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {
			// setting curent month's days in blue color.
			dayView.setTextColor(Color.WHITE);
			v.setTag(position);
		}

		if (dayString.get(position).equals(curentDateString)) {
			//setSelected(v);
			previousView = v;
			v.setPadding(10, 10, 10, 10);
			dayView.setPadding(10, 10, 10, 10);
			v.setBackgroundResource(R.drawable.calender_gradient_box);
		} else {
//			v.setBackgroundResource(R.drawable.list_item_background);

			v.setPadding(10, 10, 10, 10);
			dayView.setPadding(10, 10, 10, 10);
			v.setBackgroundResource(R.drawable.calender_gradient_box);
		}
		
		
		dayView.setText(gridvalue);
//		for(int b =1;b < this.items.size();b++){
//			if (items.get(b).length() == 1) {
//				stringArrayList.add(items.get(b).split("-")[1]);
//			}
//		}
//		
//		if(stringArrayList.contains(position)){
			for(int a = 0; a < this.collections.size(); a++){
				calenderBean thisWeekBean = this.collections.get(a);
				String x = thisWeekBean.getDatetime();
				String date = x.substring(0,10).split("/")[0];
				
					if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
						
					} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
						
					} else {
						Log.d("compare", "compare A : " + dayView.getText() + " compare B : " + date);
						int day_view_selected = Integer.parseInt(dayView.getText().toString());
						int date_selected = Integer.parseInt(date);
						if(day_view_selected == date_selected){
							final String textClub1 = thisWeekBean.getHTeam();
							final String textClub2 = thisWeekBean.getATeam();
							final String textScore1 = thisWeekBean.getHGoal();
							final String textScore2 = thisWeekBean.getAGoal();
							final String dateDialog = thisWeekBean.getDatetime();
							final String h_logo_src = thisWeekBean.geth_logo();
							final String a_logo_src = thisWeekBean.geta_logo();
							
							if(thisWeekBean.getLeague().toString().equals("ISL")){
								v.setBackgroundResource(R.drawable.calender_gradient_box_yellow);
							}else if(thisWeekBean.getLeague().toString().equals("AFC")){
								v.setBackgroundResource(R.drawable.calender_gradient_box_blue);
							}else if(thisWeekBean.getLeague().toString().equals("Friendly Match")){
								v.setBackgroundResource(R.drawable.calender_gradient_box_red);
							}	
								v.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										final Dialog dialog = new Dialog(mContext.getActivity());
										dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
										dialog.setContentView(R.layout.calendar_dialog);

				                		TextView club1 = (TextView) dialog.findViewById(R.id.club1);
				                		TextView club2 = (TextView) dialog.findViewById(R.id.club2);
				                		TextView score1 = (TextView) dialog.findViewById(R.id.score1);
				                		TextView score2 = (TextView) dialog.findViewById(R.id.score2);
				                		TextView date = (TextView) dialog.findViewById(R.id.txt_dia);
				                		ImageView h_logo = (ImageView) dialog.findViewById(R.id.h_logo);
				                		ImageView a_logo = (ImageView) dialog.findViewById(R.id.a_logo);
				                		
				                		club1.setText(textClub1);
				                		club2.setText(textClub2);
				                		score1.setText(textScore1);
				                		score2.setText(textScore2);
				                		date.setText(dateDialog);
				                		
				                		final Bitmap bitmap = DownloadImage(h_logo_src);
				                		final Bitmap bitmap2 = DownloadImage(a_logo_src);
//										imgLoader.DisplayImage(v.getTag().toString(), loader,
//												bigImg);
										Display display = mContext.getActivity().getWindowManager().getDefaultDisplay();
				                		Point size = new Point();
				                		display.getSize(size);
				                		int screenWidth = size.x;
				                		int screenHeight = size.y;

				                		// Get target image size
//				                		Bitmap bitmap = BitmapFactory.decodeFile(drawable.presence_offline);
				                		int bitmapHeight = bitmap.getHeight();
				                		int bitmapWidth = bitmap.getWidth();
				                		int bitmapHeight2 = bitmap2.getHeight();
				                		int bitmapWidth2 = bitmap2.getWidth();
				                		
				                		if(bitmapHeight > screenHeight){
				                			bitmapHeight = screenHeight - 30;
				                		}
				                		
				                		if(bitmapWidth > screenWidth){
				                			bitmapWidth = screenWidth;
				                		}

				                		if(bitmapHeight2 > screenHeight){
				                			bitmapHeight2 = screenHeight - 30;
				                		}
				                		
				                		if(bitmapWidth2 > screenWidth){
				                			bitmapWidth2 = screenWidth;
				                		}

				                		// Create resized bitmap image
				                		BitmapDrawable resizedBitmap = new BitmapDrawable(mContext.getActivity().getResources(), Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false));
				                		BitmapDrawable resizedBitmap2 = new BitmapDrawable(mContext.getActivity().getResources(), Bitmap.createScaledBitmap(bitmap2, bitmapWidth, bitmapHeight, false));

				                		// Create dialog
										h_logo.setImageBitmap(resizedBitmap.getBitmap());
										a_logo.setImageBitmap(resizedBitmap2.getBitmap());
				                		
				                		Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
				                		btnClose.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												dialog.dismiss();
												
											}
										});
				                		// Show the dialog
				                		dialog.show();
										
									}
								});
							
								
						}
						
					
				}
				
//			}		
		}
	
		// create date string for comparison
		String date = dayString.get(position);

		if (date.length() == 1) {
			date = "0" + date;
		}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}
		
		

		// show icon if date is not empty and it exists in the items array
		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
		if (date.length() > 0 && items != null && items.contains(date)) {
			iw.setVisibility(View.VISIBLE);
		} else {
			iw.setVisibility(View.INVISIBLE);
		}
		return v;
	}

	private Bitmap DownloadImage(String URL) {
		String URL1 = URL;
		Bitmap bitmap = null;

		InputStream in = null;
		Message msg = Message.obtain();
		msg.what = 1;
		try {
			in = OpenHttpConnection(URL1);
			bitmap = BitmapFactory.decodeStream(in);
			Bundle b = new Bundle();
			b.putParcelable("bitmap", bitmap);
			msg.setData(b);
			in.close();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		return bitmap;
	}
	
	private InputStream OpenHttpConnection(String urlString)
			throws IOException {

		InputStream in = null;
		int response = -1;
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");
		try {

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);

			httpConn.connect();
			response = httpConn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {

				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}
	public View setSelected(View view) {
		if (previousView != null) {
			previousView.setBackgroundResource(R.drawable.list_item_background);
		}
		previousView = view;
		view.setBackgroundResource(R.drawable.calendar_cel_selectl);
		return view;
	}

	public void refreshDays() {
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault( Locale.US );
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
		
		
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}