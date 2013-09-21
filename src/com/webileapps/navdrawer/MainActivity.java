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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
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
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity {
//	DrawerLayout mDrawer;
	ListView mDrawerList;
//	ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private CustomActionBarDrawerToggle mDrawerToggle;
	private String[] menuItems;
	private String[] connectItems;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	 private EditText search;

	 
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
		getActionBar().setTitle("HOME");
		getSupportActionBar().setBackgroundDrawable(new 
				   ColorDrawable(Color.parseColor("#B61718"))); 
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

//		if (savedInstanceState == null) {
//			selectItem(0);
//		}
		
		   RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.content);
		   
	        //create a view to inflate the layout_item (the xml with the textView created before)
	        View view = getLayoutInflater().inflate(R.layout.home, mainLayout,false);
	        View view2 = getLayoutInflater().inflate(R.layout.footer, mainLayout,false);
	        
	        //add the view to the main layout
	        mainLayout.addView(view);
	        mainLayout.addView(view2);
	        
		     view2.addOnLayoutChangeListener(new OnLayoutChangeListener() {


				@Override
				public void onLayoutChange(View v, int left, int top,
						int right, int bottom, int oldLeft, int oldTop,
						int oldRight, int oldBottom) {
					RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.content);
//					LayoutParams params = mainLayout.getLayoutParams();

					FrameLayout bottom_control_bar = (FrameLayout) v.findViewById(R.id.bottom_control_bar);
					ScrollView scrollBar = (ScrollView) mainLayout.findViewById(R.id.scrollBar);
					LayoutParams params = scrollBar.getLayoutParams();
					int height = mainLayout.getHeight() - bottom_control_bar.getHeight();
					params.height = height;
//					scrollBar.setLayoutParams(new ScrollView.LayoutParams(50, 40));
					
//					
//				     params.height = height;	
					
				}
		     });
		     
	}

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
	    menu.add(0, 1, 1, "asdasd").setIcon(R.drawable.action_search).setActionView(R.layout.search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	    return super.onCreateOptionsMenu(menu);
	 }

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {
        case 1:
            search = (EditText) item.getActionView().findViewById(R.id.descrizione);
            search.addTextChangedListener(filterTextWatcher);
            search.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            

    }   
		switch (item.getItemId()) {
		case android.R.id.home: {
			if (mDrawer.isDrawerOpen(mDrawerList)) {
				mDrawer.closeDrawer(mDrawerList);
				getSupportActionBar().setTitle("HOME");
			} else {
				mDrawer.openDrawer(mDrawerList);
				getSupportActionBar().setTitle("HOME");
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
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before, int count) {
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
			getActionBar().setTitle("HOME");
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle("HOME");
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
//			if (res==1) mItem.counter=12; //it is just an example...
//			if (res==3) mItem.counter=3; //it is just an example...
			mAdapter.addItem(mItem);
			
			res++;
		}
		
		mAdapter.addHeader(R.string.ns_menu_main_header2);
			
		for (String itemConnect : connectItems) {

			int id_title_connect = getResources().getIdentifier(itemConnect, "string",
					this.getPackageName());
			int id_icon_connect = getResources().getIdentifier(menuItemsIconConnect[resConnect],
					"drawable", this.getPackageName());
			
			NsMenuItemModel mItemConnect = new NsMenuItemModel(id_title_connect, id_icon_connect);
//			if (res==1) mItem.counter=12; //it is just an example...
//			if (res==3) mItem.counter=3; //it is just an example...
			mAdapter.addItem(mItemConnect);
			
			resConnect++;
		}


		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);
		 
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}
	



}