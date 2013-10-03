package com.persipura.match;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragment;
import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.R.id;
import com.webileapps.navdrawer.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView extends SherlockFragment {

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which

	// needs showing the event marker

	public static final String TAG = CalendarView.class.getSimpleName();

	public static CalendarView newInstance() {
		return new CalendarView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.calendar, container, false);

		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) rootView
				.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) rootView.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				showToast(selectedGridDate);

			}
		});

		return rootView;
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
	// GridView gridview = (GridView) getView().findViewById(R.id.gridview);
	// gridview.setAdapter(adapter);
	//
	// handler = new Handler();
	// handler.post(calendarUpdater);
	//
	// TextView title = (TextView) getView().findViewById(R.id.title);
	// title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	//
	// RelativeLayout previous = (RelativeLayout)
	// getView().findViewById(R.id.previous);
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
	// RelativeLayout next = (RelativeLayout) getView().findViewById(R.id.next);
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

	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		// Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {

		// TextView title = (TextView) findViewById(R.id.title);

		// adapter.refreshDays();
		// adapter.notifyDataSetChanged();
		// handler.post(calendarUpdater); // generate some calendar items
		//
		// title.setText(android.text.format.DateFormat.format("MMMM yyyy",
		// month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add("2012-09-12");
				items.add("2012-10-07");
				items.add("2012-10-15");
				items.add("2012-10-20");
				items.add("2012-11-30");
				items.add("2012-11-28");
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	public LayoutInflater getSystemService(String layoutInflaterService) {
		// TODO Auto-generated method stub
		return null;
	}
}
