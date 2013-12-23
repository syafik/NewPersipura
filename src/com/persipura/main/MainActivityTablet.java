package com.persipura.main;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.persipura.home.Home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityTablet extends SherlockFragmentActivity{
	ListView mDrawerList;
	public TextView titleTextView;
	private String[] menuItems;
	private String[] connectItems;
	private String[] mPlanetTitles;
	
	static MainActivityTablet mTabbars;

	public static MainActivityTablet getInstance() {
		return new MainActivityTablet();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main_tablet);
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B61718")));
	getSupportActionBar().setIcon(R.drawable.logo_open);
	
	mPlanetTitles = getResources().getStringArray(R.array.planets_array);
	mDrawerList = (ListView) findViewById(R.id.left_drawer);
	mDrawerList.setAdapter(new ArrayAdapter<String>(this,
			R.layout.drawer_list_item, mPlanetTitles));
	LayoutInflater inflater = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v = inflater.inflate(R.layout.actionbar_custom_view_home, null);
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayShowCustomEnabled(true);
	actionBar.setCustomView(v, new ActionBar.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT));
	// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

	actionBar.setCustomView(v);
	titleTextView = (TextView) v.findViewById(R.id.title_bar_eaa);
	_initMenu();
	
	if (savedInstanceState == null) {
		selectItem(0);

	}
	}
	
	public void selectItem(int position) {
		switch (position) {
		case 0:
			titleTextView.setText("HOME");
		
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, Home.newInstance(), Home.TAG)
					.commit();
			
			break;
		
		default:
			Log.d("default123", "goto default");

		}

	}

	private void _initMenu() {
		NsMenuAdapter mAdapter = new NsMenuAdapter(this);
		menuItems = getResources().getStringArray(R.array.ns_menu_items);
		connectItems = getResources().getStringArray(
				R.array.ns_menu_items_connect);
		String[] menuItemsIcon = getResources().getStringArray(
				R.array.ns_menu_items_icon);
		String[] menuItemsIconConnect = getResources().getStringArray(
				R.array.ns_menu_items_icon_connect);

		int res = 0;
		int resConnect = 0;

		for (String item : menuItems) {

			int id_title = getResources().getIdentifier(item, "string",
					this.getPackageName());

			int id_icon = getResources().getIdentifier(menuItemsIcon[res],
					"drawable", this.getPackageName());

			NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
			mAdapter.addItem(mItem);

			res++;
		}

		mAdapter.addHeader(R.string.ns_menu_main_header2);

		for (String itemConnect : connectItems) {

			int id_title_connect = getResources().getIdentifier(itemConnect,
					"string", this.getPackageName());
			int id_icon_connect = getResources().getIdentifier(
					menuItemsIconConnect[resConnect], "drawable",
					this.getPackageName());

			NsMenuItemModel mItemConnect = new NsMenuItemModel(
					id_title_connect, id_icon_connect);
			mAdapter.addItem(mItemConnect);

			resConnect++;
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);
	}
	

}
