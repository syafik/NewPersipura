package com.webileapps.navdrawer;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection.Request;

import android.annotation.SuppressLint;

import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.Window;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.service.textservice.SpellCheckerService.Session;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dg.android.facebook.BaseRequestListener;
import com.dg.android.facebook.SessionEvents;
import com.dg.android.facebook.SessionStore;
import com.dg.android.facebook.SessionEvents.AuthListener;
import com.dg.android.facebook.SessionEvents.LogoutListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.persipura.home.Home;
import com.persipura.match.HasilPertandingan;
import com.persipura.match.PageSlidingTabStripFragment;
import com.persipura.match.detailPertandingan;
import com.persipura.media.ListGalery;
import com.persipura.media.pageSliding;
import com.persipura.media.videoPlayer;
import com.persipura.media.videoTerbaru;
import com.persipura.search.Search;

import com.persipura.socialize.FacebookConnectDialog;
import com.persipura.socialize.FacebookLikePage;
import com.persipura.socialize.Twitter;
import com.persipura.socialize.TwitterConnectDialog;
import com.persipura.socialize.TwitterSocial;
import com.persipura.squad.DetailSquad;
import com.persipura.squad.Squad;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;

@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity {
	ListView mDrawerList;
	// ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private CustomActionBarDrawerToggle mDrawerToggle;
	private String[] menuItems;
	private String[] connectItems;
	private String[] mPlanetTitles;
	private EditText search;
	private Button scelta1;
	private ProgressDialog progressDialog;
	String squadId;
	String titleNav = "Home";
	// Your Facebook APP ID
	private static String APP_ID = "457180554390902"; // Replace your App ID
	private SessionListener mSessionListener = new SessionListener();
										// here

	// Instance of Facebook Class
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	SharedPreferences mPrefs;

	static MainActivity mTabbars;

	public static MainActivity getInstance() {
		
		return mTabbars;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabbars = this;
		
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		SessionStore.restore(facebook, this);
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		
		getSupportActionBar().setIcon(R.drawable.logo_open);

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
		// getActionBar().setDisplayHomeAsUpEnabled(true);

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
		Log.d("fbToken", "fbToken : " + facebook.getAccessToken());

	}
	
	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore.save(facebook, mTabbars);
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.cancel();
		}

		public void onAuthFail(String error) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.cancel();
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(mTabbars);
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.cancel();
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

			// getSupportActionBar().setTitle(titleNav);
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

	public void HideOtherActivities() {
		News newsFragment = (News) getSupportFragmentManager()
				.findFragmentByTag(News.TAG);
		Home homeFragment = (Home) getSupportFragmentManager()
				.findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager()
				.findFragmentByTag(pageSliding.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager()
				.findFragmentByTag(Squad.TAG);
		DetailNews detailNewsFragment = (DetailNews) getSupportFragmentManager()
				.findFragmentByTag(DetailNews.TAG);
		DetailSquad detailSquadFragment = (DetailSquad) getSupportFragmentManager()
				.findFragmentByTag(DetailSquad.TAG);
		Search searhFragment = (Search) getSupportFragmentManager()
				.findFragmentByTag(Search.TAG);
		detailPertandingan detailPertandinganFragment = (detailPertandingan) getSupportFragmentManager()
				.findFragmentByTag(detailPertandingan.TAG);
		TwitterSocial twitterFragment = (TwitterSocial) getSupportFragmentManager()
				.findFragmentByTag(TwitterSocial.TAG);
		videoTerbaru videoTerbaruFragment = (videoTerbaru) getSupportFragmentManager()
				.findFragmentByTag(videoPlayer.TAG);
		ListGalery listgaleryFragment = (ListGalery) getSupportFragmentManager()
				.findFragmentByTag(ListGalery.TAG);
		HasilPertandingan hasilpertandinganFragment = (HasilPertandingan) getSupportFragmentManager()
				.findFragmentByTag(HasilPertandingan.TAG);

		if (twitterFragment != null) {
			twitterFragment.getView().setVisibility(View.GONE);
		}
		if (detailPertandinganFragment != null) {
			detailPertandinganFragment.getView().setVisibility(View.GONE);
		}

		if (searhFragment != null) {
			searhFragment.getView().setVisibility(View.GONE);
		}

		if (newsFragment != null) {
			newsFragment.getView().setVisibility(View.GONE);
		}

		if (homeFragment != null) {
			homeFragment.getView().setVisibility(View.GONE);
		}

		if (pageSlidingFragment != null) {
			pageSlidingFragment.getView().setVisibility(View.GONE);
		}

		if (pageSlidingTabStripFragment != null) {
			pageSlidingTabStripFragment.getView().setVisibility(View.GONE);
		}

		if (squadFragment != null) {
			squadFragment.getView().setVisibility(View.GONE);
		}

		if (detailNewsFragment != null) {
			detailNewsFragment.getView().setVisibility(View.GONE);
		}

		if (detailSquadFragment != null) {
			detailSquadFragment.getView().setVisibility(View.GONE);
		}
		if (listgaleryFragment != null) {
			listgaleryFragment.getView().setVisibility(View.GONE);
		}
		if (hasilpertandinganFragment != null) {
			hasilpertandinganFragment.getView().setVisibility(View.GONE);
		}
		if (videoTerbaruFragment != null) {
			videoTerbaruFragment.getView().setVisibility(View.GONE);
		}

	}

	private void selectItem(int position) {
		News newsFragment = (News) getSupportFragmentManager()
				.findFragmentByTag(News.TAG);
		Home homeFragment = (Home) getSupportFragmentManager()
				.findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager()
				.findFragmentByTag(pageSliding.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager()
				.findFragmentByTag(Squad.TAG);
		

		Bundle args = new Bundle();
		Log.d("position", "position : " + position);
		switch (position) {
		case 0:
			if (homeFragment != null) {
				HideOtherActivities();
				homeFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content, Home.newInstance(), Home.TAG)
						.commit();
			}

			titleNav = "Home";

			break;
		case 1:
			if (newsFragment != null) {
				HideOtherActivities();
				newsFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content, News.newInstance(), News.TAG)
						.commit();
			}

			titleNav = "News";
			break;
		case 2:
			if (pageSlidingFragment != null) {
				HideOtherActivities();
				pageSlidingFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, pageSliding.newInstance(),
								pageSliding.TAG).commit();
			}

			titleNav = "Media";
			break;
		case 3:
			if (pageSlidingTabStripFragment != null) {
				HideOtherActivities();
				pageSlidingTabStripFragment.getView().setVisibility(
						View.VISIBLE);
			} else {
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
			if (squadFragment != null) {
				HideOtherActivities();
				squadFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
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
			if (facebook.isSessionValid()) {
				Log.d("valid", "validSession");
				FacebookLikePage cdd = new FacebookLikePage(MainActivity.this);
				cdd.show();
			} else {
				FacebookConnectDialog cdd = new FacebookConnectDialog(
						MainActivity.this);
				cdd.show();

			}
			titleNav = "Facebook";
			break;
		case 7:
			// getSupportFragmentManager().beginTransaction()
			// .add(R.id.content, Twitter.newInstance(), Twitter.TAG).commit();

			// if(squadFragment != null){
			// HideOtherActivities();
			// twitterFragment.getView().setVisibility(View.VISIBLE);
			// }else{
			// HideOtherActivities();
			// getSupportFragmentManager()
			// .beginTransaction()
			// .add(R.id.content, TwitterSocial.newInstance(),
			// TwitterSocial.TAG)
			// .commit();
			//
			// }
			TwitterConnectDialog cdd = new TwitterConnectDialog(
					MainActivity.this);
			cdd.show();
			titleNav = "Twitter";
			break;
		case 8:

			HideOtherActivities();

			args.putString("q", search.getText().toString());
			Search searchFragment = new Search();
			searchFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, searchFragment, Search.TAG).commit();
			titleNav = "Search";
			break;

		}

		mDrawer.closeDrawer(mDrawerList);
	}

	private final class LoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {
			Log.i("inside>>", "LoginDialogListener calles......");
			SessionEvents.onLoginSuccess();
			FacebookLikePage cdd = new FacebookLikePage(MainActivity.this);
			cdd.show();

		}

		public void onFacebookError(FacebookError error) {
			Log.i("inside>>",
					"onFacebookError calles......" + error.getMessage());
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			Log.i("inside>>", "onError calles......");
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			Log.i("inside>>", "onCancel calles......");
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,
				DrawerLayout mDrawerLayout) {
			super(mActivity, mDrawerLayout,
					R.drawable.abs__ic_ab_back_holo_dark,
					R.string.ns_menu_open, R.string.ns_menu_close);
		}

		@Override
		public void onDrawerClosed(View view) {
			// getActionBar().setTitle(titleNav);
			// getSupportActionBar().setIcon(R.drawable.logo_persipura_close);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			// getActionBar().setTitle(titleNav);
			// getSupportActionBar().setIcon(R.drawable.logo_persipura_open);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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

	public void loginToFacebook() {
		facebook.authorize(MainActivity.this,
				AppConstants.FACEBOOK_PERMISSIONARR, new LoginDialogListener());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void likeFacebookPage() {

//		if (facebook.getAccessToken() != null) {
//			Bundle params = new Bundle();
//			params.putString("access_token", facebook.getAccessToken());
//			Uri uri = Uri.parse("https://facebook.com" + og_object_url);
//			params.putString("og_object_url", uri.toString());
//
//			mAsyncRunner.request("me/og.likes", params, "POST",
//					new BaseRequestListener() {
//						@Override
//						public void onComplete(final String response,
//								final Object state) {
//							// handle success
//							if(response.contains("Error validating access token")){
//							loginToFacebook();
//							}
//							
//						}
//					}, null);
//		}
		String page1 = "572779142753263";
		String page2 = "536852216398619";
		
		Uri uri1 = Uri.parse("https://facebook.com" + page1);
		Uri uri2 = Uri.parse("https://facebook.com" + page2);
		
		List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
		nameparams.add(new BasicNameValuePair("access_token", facebook.getAccessToken()));
		nameparams.add(new BasicNameValuePair("object", uri1.toString()));
		
		String result = WebHTTPMethodClass
				.executeHttPost("https://graph.facebook.com/me/og.likes/" + page1, nameparams);
		Log.d("resultLike", "resultLike1 :" + result);
		
		List<NameValuePair> nameparams2 = new ArrayList<NameValuePair>();
		nameparams2.add(new BasicNameValuePair("access_token", facebook.getAccessToken()));
		nameparams2.add(new BasicNameValuePair("object", uri2.toString()));
		
		String result2 = WebHTTPMethodClass
				.executeHttPost("https://graph.facebook.com/me/og.likes/" + page2, nameparams);
		Log.d("resultLike", "resultLike2 :" + result2);
		
		


	}

}