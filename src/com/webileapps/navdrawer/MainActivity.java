/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webileapps.navdrawer;

import android.annotation.SuppressLint;

import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.Window;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.persipura.home.Home;
import com.persipura.match.PageSlidingTabStripFragment;
import com.persipura.media.pageSliding;
import com.persipura.search.Search;
import com.persipura.socialize.Facebook;
import com.persipura.socialize.Twitter;
import com.persipura.squad.DetailSquad;
import com.persipura.squad.Squad;

@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity {
	private static final String TAG = "TAG";
	// DrawerLayout mDrawer;
	ListView mDrawerList;
	// ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private CustomActionBarDrawerToggle mDrawerToggle;
	private String[] menuItems;
	private String[] connectItems;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	private EditText search;
	private Button scelta1;
	private ProgressDialog progressDialog;
	String squadId;
	String titleNav = "Home";

	public static MainActivity newInstance() {
		return new MainActivity();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		getSupportActionBar().setIcon(R.drawable.logo_open);
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getSupportActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle(null);
		getActionBar().setHomeButtonEnabled(true);
//	    getActionBar().setDisplayHomeAsUpEnabled(true);


		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#B61718")));
		_initMenu();
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);

	

		if (savedInstanceState == null) {

			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				
				selectItem(0);
			} else {
				squadId = extras.getString("squadId");
				selectItem(5);
			}

		}

	}
	
	

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		menu.add(0, 1, 1, "search")
				.setIcon(R.drawable.action_search)
				.setActionView(R.layout.search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		 MenuItem menuItem = menu.findItem(1);
	        menuItem.setOnActionExpandListener(new OnActionExpandListener() {
	            @Override
	            public boolean onMenuItemActionCollapse(MenuItem item) {
	                // Do something when collapsed
	            	getSupportActionBar().setIcon(R.drawable.logo_open);
	            	Log.d("collapsed", "collapsed");
	                return true; // Return true to collapse action view
	            }

	            @Override
	            public boolean onMenuItemActionExpand(MenuItem item) {
	            	getSupportActionBar().setIcon(R.drawable.logo_persipura);
	            	Log.d("expanded", "expanded");
	                return true; // Return true to expand action view
	            }
	        });
	    
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		
		switch (item.getItemId()) {
		case 1:
			search = (EditText) item.getActionView().findViewById(
					R.id.descrizione);
			search.addTextChangedListener(filterTextWatcher);
			search.requestFocus();
			
			scelta1 = (Button) item.getActionView().findViewById(R.id.scelta1);
			scelta1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (search.length() > 0) {
						selectItem(8);
					}

				}
			});

		}
		switch (item.getItemId()) {
		case android.R.id.home: {
			if (mDrawer.isDrawerOpen(mDrawerList)) {
				getSupportActionBar().setIcon(R.drawable.logo_open);
				mDrawer.closeDrawer(mDrawerList);
			} else {
				getSupportActionBar().setIcon(R.drawable.logo_close);
				mDrawer.openDrawer(mDrawerList);

			}

//			getSupportActionBar().setTitle(titleNav);
			break;
		}

		case R.id.action_contact:
			// QuickContactFragment dialog = new QuickContactFragment();
			// dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			// return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// your search logic here
		}

	};

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void HideOtherActivities(){
		News newsFragment = (News) getSupportFragmentManager().findFragmentByTag(News.TAG);
		Home homeFragment = (Home) getSupportFragmentManager().findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager().findFragmentByTag(pageSliding.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager().findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager().findFragmentByTag(Squad.TAG);
		DetailNews detailNewsFragment = (DetailNews) getSupportFragmentManager().findFragmentByTag(DetailNews.TAG);
		DetailSquad detailSquadFragment = (DetailSquad) getSupportFragmentManager().findFragmentByTag(DetailSquad.TAG);
		
		if(newsFragment != null){
			newsFragment.getView().setVisibility(View.GONE);
		}
		
		if(homeFragment != null){
			homeFragment.getView().setVisibility(View.GONE);
		}

		if(pageSlidingFragment != null){
			pageSlidingFragment.getView().setVisibility(View.GONE);
		}
		
		if(pageSlidingTabStripFragment != null){
			pageSlidingTabStripFragment.getView().setVisibility(View.GONE);
		}
		
		if(squadFragment != null){
			squadFragment.getView().setVisibility(View.GONE);
		}

		if(detailNewsFragment != null){
			detailNewsFragment.getView().setVisibility(View.GONE);
		}
		
		if(detailSquadFragment != null){
			detailSquadFragment.getView().setVisibility(View.GONE);
		}
		
	}

	private void selectItem(int position) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		News newsFragment = (News) getSupportFragmentManager().findFragmentByTag(News.TAG);
		Home homeFragment = (Home) getSupportFragmentManager().findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager().findFragmentByTag(pageSliding.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager().findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager().findFragmentByTag(Squad.TAG);
		DetailNews detailNewsFragment = (DetailNews) getSupportFragmentManager().findFragmentByTag(DetailNews.TAG);
		
		Bundle args = new Bundle();
		Log.d("position", "position : " + position);
		switch (position) {
		case 0:
			if(homeFragment != null){
				HideOtherActivities();
				homeFragment.getView().setVisibility(View.VISIBLE);
			}else{
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
				.add(R.id.content, Home.newInstance(), Home.TAG).commit();
			}
			
			titleNav = "Home";
			
			break;
		case 1:
			if(newsFragment != null){
//				ft.attach(newsFragment);
//				FragmentManager fragmentManager = getSupportFragmentManager();
//				
//				fragmentManager.beginTransaction()
////				    .remove(fragment1)
////				    .add(R.id.fragment_container, fragment2)
//				    .show(newsFragment)
//				    .hide(homeFragment)
//				    .commit();
				HideOtherActivities();
				newsFragment.getView().setVisibility(View.VISIBLE);
			}else{
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
				.add(R.id.content, News.newInstance(), News.TAG).commit();
			}
			
			titleNav = "News";
			break;
		case 2:
			if(pageSlidingFragment != null){
				HideOtherActivities();
				pageSlidingFragment.getView().setVisibility(View.VISIBLE);
			}else{
				HideOtherActivities();
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content, pageSliding.newInstance(),
						pageSliding.TAG).commit();
			}
			
			titleNav = "Media";
			break;
		case 3:
			if(pageSlidingTabStripFragment != null){
				HideOtherActivities();
				pageSlidingTabStripFragment.getView().setVisibility(View.VISIBLE);
			}else{
				HideOtherActivities();
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content,
						PageSlidingTabStripFragment.newInstance(),
						PageSlidingTabStripFragment.TAG).commit();
			}
			
			titleNav = "Match";
			break;
		case 4:
			if(squadFragment != null){
				HideOtherActivities();
				squadFragment.getView().setVisibility(View.VISIBLE);
			}else{
				HideOtherActivities();
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content, Squad.newInstance(), Squad.TAG)
				.commit();
				
			}
			
			titleNav = "Squad";
			break;
		case 5:
			args.putString("squadId", squadId);
			DetailSquad fragment = new DetailSquad();
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			titleNav = "Squad";
			break;
		case 6:
			getSupportFragmentManager().beginTransaction()
			.add(R.id.content, Facebook.newInstance(), Facebook.TAG).commit();
			titleNav = "Facebook";
			break;	
		case 7:
			getSupportFragmentManager().beginTransaction()
			.add(R.id.content, Twitter.newInstance(), Twitter.TAG).commit();
			titleNav = "Twitter";
			break;	
		case 8:
			args.putString("q", search.getText().toString());
			Search searchFragment = new Search();
			searchFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, searchFragment).commit();
			titleNav = "Search";
			break;
		
		}

		
//		 View view2 = getLayoutInflater().inflate(R.layout.footer, mainLayout,
//		 false);
//		
//		 mainLayout.addView(view2);

		
		mDrawer.closeDrawer(mDrawerList);
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,
				DrawerLayout mDrawerLayout) {
			super(mActivity, mDrawerLayout, R.drawable.abs__ic_ab_back_holo_dark,
					R.string.ns_menu_open, R.string.ns_menu_close);
		}

		@Override
		public void onDrawerClosed(View view) {
//			getActionBar().setTitle(titleNav);
//			getSupportActionBar().setIcon(R.drawable.logo_persipura_close);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
//			getActionBar().setTitle(titleNav);
//			getSupportActionBar().setIcon(R.drawable.logo_persipura_open);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}
	}

	private void _initMenu() {
		NsMenuAdapter mAdapter = new NsMenuAdapter(this);

		// Add Header
		// mAdapter.addHeader(R.string.ns_menu_main_header);

		// Add first block
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
			// if (res==1) mItem.counter=12; //it is just an example...
			// if (res==3) mItem.counter=3; //it is just an example...
			
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
			// if (res==1) mItem.counter=12; //it is just an example...
			// if (res==3) mItem.counter=3; //it is just an example...
			mAdapter.addItem(mItemConnect);

			resConnect++;
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}

}