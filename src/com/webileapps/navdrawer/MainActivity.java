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
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity {
//	DrawerLayout mDrawer;
	ListView mDrawerList;
//	ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private CustomActionBarDrawerToggle mDrawerToggle;
	private String[] menuItems;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
//		mDrawer.setDrawerListener(mDrawerToggle);
//
//        getActionBar().setDisplayHomeAsUpEnabled(false);
        _initMenu();
        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);
//		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
//		mDrawer, /* DrawerLayout object */
//		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
//		R.string.drawer_open, /* "open drawer" description for accessibility */
//		R.string.drawer_close /* "close drawer" description for accessibility */
//		) {
//			public void onDrawerClosed(View view) {
//				getActionBar().setDisplayHomeAsUpEnabled(false);
////				getActionBar().setIcon(R.drawable.home_up_drawable);
//				getSupportActionBar().setTitle(mTitle);
//				invalidateOptionsMenu(); // creates call to
//											// onPrepareOptionsMenu()
//			}
//
//			public void onDrawerOpened(View drawerView) {
//				getActionBar().setDisplayHomeAsUpEnabled(true);
//				getSupportActionBar().setTitle(mDrawerTitle);
//				invalidateOptionsMenu(); // creates call to
//											// onPrepareOptionsMenu()
//			}
//		};
//		mDrawer.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home: {
			if (mDrawer.isDrawerOpen(mDrawerList)) {
				mDrawer.closeDrawer(mDrawerList);
			} else {
				mDrawer.openDrawer(mDrawerList);
			}
			break;
		}

		case R.id.action_contact:
			// QuickContactFragment dialog = new QuickContactFragment();
			// dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			// return true;

		}

		return super.onOptionsItemSelected(item);
	}

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

	private void selectItem(int position) {

		switch (position) {
		case 0:
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content,
							PageSlidingTabStripFragment.newInstance(),
							PageSlidingTabStripFragment.TAG).commit();
			break;
		case 1:
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content,
					News.newInstance(),
					News.TAG).commit();
			break;
		case 2:
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content,
					Media.newInstance(),
					Media.TAG).commit();
	        break;
		case 3:

			break;
//		default:
//
//			SherlockFragment fragment = new PlanetFragment();
//			Bundle args = new Bundle();
//			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//			fragment.setArguments(args);
//
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.content, fragment).commit();
//			break;
		}

		mDrawer.closeDrawer(mDrawerList);
	}
	
	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){
			super(
			    mActivity,
			    mDrawerLayout, 
			    R.drawable.ic_drawer,
			    R.string.ns_menu_open, 
			    R.string.ns_menu_close);
		}

		@Override
		public void onDrawerClosed(View view) {
			getActionBar().setTitle(getString(R.string.ns_menu_close));
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(getString(R.string.ns_menu_open));
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}
	}
	
	private void _initMenu() {
		NsMenuAdapter mAdapter = new NsMenuAdapter(this);

		// Add Header
//		mAdapter.addHeader(R.string.ns_menu_main_header);

		// Add first block
		menuItems = getResources().getStringArray(
				R.array.ns_menu_items);
		String[] menuItemsIcon = getResources().getStringArray(
				R.array.ns_menu_items_icon);

		int res = 0;
		for (String item : menuItems) {

			int id_title = getResources().getIdentifier(item, "string",
					this.getPackageName());
			int id_icon = getResources().getIdentifier(menuItemsIcon[res],
					"drawable", this.getPackageName());
			
			NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
//			if (res==1) mItem.counter=12; //it is just an example...
//			if (res==3) mItem.counter=3; //it is just an example...
			mAdapter.addItem(mItem);
			
			res++;
		}
		
		mAdapter.addHeader(R.string.ns_menu_main_header2);
		


		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);
		 
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}
	



}