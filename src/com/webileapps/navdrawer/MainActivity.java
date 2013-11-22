package com.webileapps.navdrawer;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.application.app.utility.Constants;
import com.application.app.utility.TwitterSession;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.persipura.aboutus.Store;
import com.persipura.home.Home;
import com.persipura.match.HasilPertandingan;
import com.persipura.match.PageSlidingTabStripFragment;
import com.persipura.match.detailPertandingan;
import com.persipura.media.GaleryView2;
import com.persipura.media.ListGalery;
import com.persipura.media.mediaTerbaru;
import com.persipura.media.pageSliding;
import com.persipura.media.videoPlayer;
import com.persipura.search.Search;
import com.persipura.socialize.FacebookConnectDialog;
import com.persipura.socialize.FacebookLikePage;
import com.persipura.socialize.ShareDialog;
import com.persipura.socialize.Stream;
import com.persipura.socialize.TwitterConnectDialog;
import com.persipura.socialize.TwitterFollowPage;
import com.persipura.socialize.TwitterLogoutPage;
import com.persipura.socialize.TwitterSocial;
import com.persipura.squad.DetailSquad;
import com.persipura.squad.Squad;
import com.persipura.utils.AppConstants;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

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
	ProgressDialog progressDialog;
	String squadId;
	String titleNav = "Home";
	boolean flag = false;
	Permissions[] permissions = new Permissions[] { Permissions.USER_LIKES,
			Permissions.BASIC_INFO, Permissions.EMAIL, Permissions.USER_PHOTOS,
			Permissions.USER_BIRTHDAY, Permissions.USER_LOCATION,
			Permissions.PUBLISH_ACTION, Permissions.PUBLISH_STREAM

	};

	public final static String consumerKey = "qzooqEGzPmfB5Da2qVdsw";
	public final static String consumerSecret = "bTcVQvfUWWhO8JvTLCfVirVUbEFa72QBxp5GfLpYdo";

	// Your Facebook APP ID
	private static String APP_ID = "171262573080320"; // Replace your App ID
	// here

	// Instance of Facebook Class

	SimpleFacebook mSimpleFacebook;
	private static final String TAG = "MainActivity";
	public static final int REQUEST_CODE = 100;
	SharedPreferences mSharedPreferences;

	private Twitter twitter;
	private RequestToken requestToken;
	public TwitterSession twitterSession;
	private AuthenticationTask mAuthTask;
	private RetriveAcessTokenTask mAccessTokenTask;

	static MainActivity mTabbars;

	public static MainActivity getInstance() {
		return new MainActivity();
	}

	//
	// @Override
	// public void onResume()
	// {
	// super.onResume();
	// mSimpleFacebook = SimpleFacebook.getInstance(this);
	// }

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

		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(APP_ID).setNamespace("webileapps")
				.setPermissions(permissions).build();
		SimpleFacebook.setConfiguration(configuration);
		twitterSession = new TwitterSession(mTabbars);

		getSupportActionBar().setIcon(R.drawable.logo_open);

		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(null);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		 
		
		View v = LayoutInflater.from(this).inflate(R.layout.actionbar_custom_view_home, null);

		 ActionBar actionBar = getSupportActionBar();
//		 actionBar.setDisplayHomeAsUpEnabled(true);
		 actionBar.setDisplayShowCustomEnabled(true);


		 actionBar.setCustomView(v);
			
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#B61718")));
		_initMenu();
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);

		Log.d("masukin", "ini ngeload lagi loh mas");
		if (savedInstanceState == null) {
			selectItem(0);

		}
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
			Log.d("wewe", "wewgombel");
		}

		// Check if twitter keys are set
		if (TextUtils.isEmpty(Constants.TWITTER_CONSUMER_KEY)
				|| TextUtils.isEmpty(Constants.TWITTER_CONSUMER_SECRET)) {
			// Internet Connection is not present

			Toast.makeText(MainActivity.this, "Twitter oAuth tokens",
					Toast.LENGTH_LONG).show();
			// stop executing code by return
			return;
		}

	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("Logged", "Logged in...");

		} else if (state.isClosed()) {
			Log.i("Logged", "Logged out...");
		}
	}

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// create search menu
		menu.add(0, 1, 1, "search")
				.setIcon(R.drawable.action_search)
				.setActionView(R.layout.search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		MenuItem menuItem = menu.findItem(1);
		menuItem.setVisible(true);
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

		// create share menu
		if (mTabbars.flag) {
			menu.add(Menu.NONE, 2, 2, "share")
					.setIcon(R.drawable.ic_action_share)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menuItem.setVisible(false);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		Log.d("item.getItemId()", "item.getItemId() : " + item.getItemId());
		switch (item.getItemId()) {
		case 1:
			search = (EditText) item.getActionView().findViewById(
					R.id.descrizione);
			search.requestFocus();

			scelta1 = (Button) item.getActionView().findViewById(R.id.scelta1);
			scelta1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (search.length() > 0) {
						selectItem(11);
					}

				}
			});
			break;
		case 2:
			ShareDialog sd = new ShareDialog(MainActivity.this);
			sd.show();

			break;
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
			break;
		}

		case R.id.action_contact:

		}

		return super.onOptionsItemSelected(item);
	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("logging", "hit item menu : " + position);
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
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

			Log.d("On Config Change", "LANDSCAPE");
			return;
		} else {

			Log.d("On Config Change", "PORTRAIT");
		}

	}

	public void changeItemSearchToShare(Boolean show) {
		mTabbars.flag = show;
		mTabbars.supportInvalidateOptionsMenu();
	}

	public void HideOtherActivities() {

		changeItemSearchToShare(false);

		PageSlidingNews newsFragment = (PageSlidingNews) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingNews.TAG);
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
		ListGalery listgaleryFragment = (ListGalery) getSupportFragmentManager()
				.findFragmentByTag(ListGalery.TAG);
		HasilPertandingan hasilpertandinganFragment = (HasilPertandingan) getSupportFragmentManager()
				.findFragmentByTag(HasilPertandingan.TAG);
		mediaTerbaru mediaterbaruFragment = (mediaTerbaru) getSupportFragmentManager()
				.findFragmentByTag(mediaTerbaru.TAG);
		videoPlayer videoplayerFragment = (videoPlayer) getSupportFragmentManager()
				.findFragmentByTag(videoPlayer.TAG);
		GaleryView2 galeryViewFragment = (GaleryView2) getSupportFragmentManager()
				.findFragmentByTag(GaleryView2.TAG);
		Stream stream = (Stream) getSupportFragmentManager().findFragmentByTag(
				Stream.TAG);
		
		Store storeFragment = (Store) getSupportFragmentManager()
				.findFragmentByTag(Store.TAG);
		
		if (storeFragment != null) {
			storeFragment.getView().setVisibility(View.GONE);
		}
		
		if (galeryViewFragment != null) {
			galeryViewFragment.getView().setVisibility(View.GONE);
		}
		if (stream != null) {
			stream.getView().setVisibility(View.GONE);
		}

		if (twitterFragment != null) {
			twitterFragment.getView().setVisibility(View.GONE);
		}

		if (videoplayerFragment != null) {
			videoplayerFragment.getView().setVisibility(View.GONE);
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
		if (mediaterbaruFragment != null) {
			mediaterbaruFragment.getView().setVisibility(View.GONE);
		}

	}

	public void selectItem(int position) {
		PageSlidingNews newsFragment = (PageSlidingNews) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingNews.TAG);
		Home homeFragment = (Home) getSupportFragmentManager()
				.findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager()
				.findFragmentByTag(pageSliding.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager()
				.findFragmentByTag(Squad.TAG);
		Store storeFragment = (Store) getSupportFragmentManager()
				.findFragmentByTag(Store.TAG);
		
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(mTabbars);
		String prevFragment = mPrefs.getString("currentFragment", Home.TAG);
		Editor editor = mPrefs.edit();

		Log.d("position", "current position : " + position);
		Bundle args = new Bundle();
		switch (position) {
		case 0:
			editor.putString("currentFragment", Home.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();

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
			editor.putString("currentFragment", PageSlidingNews.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
			if (newsFragment != null) {
				HideOtherActivities();
				newsFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, PageSlidingNews.newInstance(),
								PageSlidingNews.TAG).commit();
			}

			titleNav = "News";
			break;
		case 2:
			editor.putString("currentFragment", pageSliding.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
			if (pageSlidingFragment != null) {
				HideOtherActivities();
				pageSlidingFragment.getView().setVisibility(View.VISIBLE);
				// if (mediaterbaruFragment != null) {
				// HideOtherActivities();
				// mediaterbaruFragment.getView().setVisibility(View.VISIBLE);
				// }
				// mediaterbaruFragment.getView().setVisibility(View.VISIBLE);
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
			editor.putString("currentFragment", PageSlidingTabStripFragment.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
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
			editor.putString("currentFragment", Squad.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
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
			editor.putString("currentFragment", Store.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
			if (storeFragment != null) {
				HideOtherActivities();
				storeFragment.getView().setVisibility(View.VISIBLE);
			} else {
				HideOtherActivities();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content, Store.newInstance(), Store.TAG)
						.commit();

			}
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			Session session = Session.getActiveSession();
			if (session == null || session.getState().isClosed()) {
				FacebookConnectDialog cdd = new FacebookConnectDialog(
						MainActivity.this);
				cdd.show();

			} else {
				Log.d("valid", "validSession");
				FacebookLikePage cdd = new FacebookLikePage(MainActivity.this);
				cdd.show();

			}
			titleNav = "Facebook";
			break;
		case 9:
			HideOtherActivities();
			if (twitterSession.isTwitterLoggedInAlready()) {
				ProgressDialog pd = new ProgressDialog(mTabbars);
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.show();
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
				// Access Token
				String access_token = twitterSession.getDefaultAccessToaken();
				// Access Token Secret
				String access_token_secret = twitterSession.getDefaultSecret();
				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);
				// Update status
				Relationship relationship;
				Relationship relationship2;
				boolean followBoth = false;
				try {
					relationship = twitter.showFriendship(
							twitter.getScreenName(), "IDPersipura");
					followBoth = relationship.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDPersipura is =>"
							+ relationship.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					relationship2 = twitter.showFriendship(
							twitter.getScreenName(), "IDFreeport");
					followBoth = relationship2.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDFreeport is =>"
							+ relationship2.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (followBoth) {
					HideOtherActivities();
					getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.content, TwitterSocial.newInstance(),
									TwitterSocial.TAG).commit();
					// TwitterLogoutPage tlp2 = new TwitterLogoutPage(
					// MainActivity.this, twitterSession, twitter);
					// tlp2.show();

				} else {
					TwitterFollowPage tlp = new TwitterFollowPage(
							MainActivity.this);
					tlp.show();
				}
				pd.dismiss();

			} else {
				TwitterConnectDialog cdd = new TwitterConnectDialog(
						MainActivity.this);
				cdd.show();
			}

			titleNav = "Twitter";
			break;
		case 10:
			if (twitterSession.isTwitterLoggedInAlready()) {
				ProgressDialog pd = new ProgressDialog(mTabbars);
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.show();
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
				// Access Token
				String access_token = twitterSession.getDefaultAccessToaken();
				// Access Token Secret
				String access_token_secret = twitterSession.getDefaultSecret();
				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);
				// Update status
				Relationship relationship;
				Relationship relationship2;
				boolean followBoth = false;
				try {
					relationship = twitter.showFriendship(
							twitter.getScreenName(), "IDPersipura");
					followBoth = relationship.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDPersipura is =>"
							+ relationship.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					relationship2 = twitter.showFriendship(
							twitter.getScreenName(), "IDFreeport");
					followBoth = relationship2.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDFreeport is =>"
							+ relationship2.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (followBoth) {
					HideOtherActivities();
					getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.content, Stream.newInstance(), Stream.TAG)
							.commit();

				} else {
					TwitterFollowPage tlp = new TwitterFollowPage(
							MainActivity.this);
					tlp.show();
				}
				pd.dismiss();

			} else {
				TwitterConnectDialog cdd = new TwitterConnectDialog(
						MainActivity.this);
				cdd.show();
			}

			break;
		case 11:

			HideOtherActivities();

			args.putString("q", search.getText().toString());
			Search searchFragment = new Search();
			searchFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, searchFragment, Search.TAG).commit();
			titleNav = "Search";
			break;

		default:
			Log.d("default123", "goto default");

		}

		mDrawer.closeDrawer(mDrawerList);
	}

	public void logoutTwitter() {
		twitterSession.logout();
		TwitterSocial twitter_social = (TwitterSocial) getSupportFragmentManager()
				.findFragmentByTag(TwitterSocial.TAG);
		FragmentTransaction trans = getSupportFragmentManager()
				.beginTransaction();
		trans.remove(twitter_social);
		trans.commit();
		selectItem(0);

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
			mAdapter.addItem(mItemConnect);

			resConnect++;
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	public void likePersipuraPage(){
		String yourFBpageId = "536852216398619";
		String uriMobile = "http://m.facebook.com/536852216398619";
		try {
		      //try to open page in facebook native app.
			  
		      String uri = "fb://page/" + yourFBpageId;    //Cutsom URL
		      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		      startActivity(intent);   
		}catch (ActivityNotFoundException ex){
		      //facebook native app isn't available, use browser.
		      String uri = "http://touch.facebook.com/pages/x/" + yourFBpageId;  //Normal URL  
		      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMobile));    
		      startActivity(i); 
		}
	}
	
	public void likeFreeportPage(){
//		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/536852216398619")));
		String yourFBpageId = "572779142753263";
		String uriMobile = "http://m.facebook.com/572779142753263";
		try {
		      //try to open page in facebook native app.
			  
		      String uri = "fb://page/" + yourFBpageId;    //Cutsom URL
		      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		      startActivity(intent);   
		}catch (ActivityNotFoundException ex){
		      //facebook native app isn't available, use browser.
		      String uri = "http://touch.facebook.com/pages/x/" + yourFBpageId;  //Normal URL  
		      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMobile));    
		      startActivity(i); 
		}
	}

	public void loginToFacebook() {
		// OnLoginListener onLoginListener = new
		// SimpleFacebook.OnLoginListener() {
		//
		// private String TAG;
		//
		// @Override
		// public void onFail(String reason) {
		// Log.w(TAG, reason);
		// }
		//
		// @Override
		// public void onException(Throwable throwable) {
		// Log.e(TAG, "Bad thing happened", throwable);
		// }
		//
		// @Override
		// public void onThinking() {
		// // show progress bar or something to the user while login is
		// // happening
		// Log.i(TAG, "In progress");
		// }
		//
		// @Override
		// public void onLogin() {
		// // change the state of the button or do whatever you want
		// Log.i(TAG, "Logged in");
		// selectItem(8);
		// Toast.makeText(getApplicationContext(), "Login SuccessFully",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// @Override
		// public void onNotAcceptingPermissions() {
		// Log.w(TAG, "User didn't accept read permissions");
		// }
		//
		// };
		//
		// mSimpleFacebook.login(onLoginListener);
		Intent intent = new Intent(MainActivity.this,
				com.persipura.socialize.MainFacebook.class);
		startActivity(intent);

	}

	public void loginToTwitter() {
		onAuth();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (twitterSession != null) {
			Log.d("masuk", "masuk resume loh mas");
			if (twitterSession.isTwitterLoggedInAlready())
				start();
		}

		mSimpleFacebook = SimpleFacebook.getInstance(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void likeFacebookPage() {
		Session session = Session.getActiveSession();
		if (session == null || session.getState().isClosed()) {
			loginToFacebook();
		} else {

		}

	}

	public void setFacebookContentShare(String title, String shared_url) {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(mTabbars);

		Editor editor = mPrefs.edit();
		editor.putString("title", title);
		editor.putString("shared_url", shared_url);
		editor.commit();
	}

	public void shareToTwitter() {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(mTabbars);
		String name = mPrefs.getString("title", "");
		String title = mPrefs.getString("shared_url", "");

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
		// Access Token
		String access_token = twitterSession.getDefaultAccessToaken();
		// Access Token Secret
		String access_token_secret = twitterSession.getDefaultSecret();
		AccessToken accessToken = new AccessToken(access_token,
				access_token_secret);
		Twitter twitter = new TwitterFactory(builder.build())
				.getInstance(accessToken);
		// Update status
		twitter4j.Status response;
		try {
			response = twitter.updateStatus(name + " " + title);
			Log.d(TAG, "Response Text=>" + response.getText());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void shareToFacebook() {

		Session session = Session.getActiveSession();
		if (session == null || session.getState().isClosed()) {
			loginToFacebook();
		} else {
			SharedPreferences mPrefs = PreferenceManager
					.getDefaultSharedPreferences(mTabbars);
			Bundle params = new Bundle();
			params.putString("name", mPrefs.getString("title", ""));
			params.putString("link", mPrefs.getString("shared_url", ""));

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					
					String postId = null;
					try {
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.d("facebookError", "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(
								getApplicationContext().getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(
								getApplicationContext().getApplicationContext(),
								"Successfully share to facebook", Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", params,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();

		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("keyUp", "KeyupBack");
			SharedPreferences mPrefs = PreferenceManager
					.getDefaultSharedPreferences(mTabbars);
			String backstack = mPrefs.getString("prevFragment", "");
			Log.d("videoPlayer", "backstackTag : " + backstack);
			if (!backstack.isEmpty()) {
				HideOtherActivities();
				if (getSupportFragmentManager().findFragmentByTag(backstack) != null) {
					View view = getSupportFragmentManager().findFragmentByTag(
							backstack).getView();
					GaleryView2 fragment = (GaleryView2) getSupportFragmentManager()
							.findFragmentByTag(GaleryView2.TAG);
					if(backstack == pageSliding.TAG){
						videoPlayer videoFragment = (videoPlayer) getSupportFragmentManager().findFragmentByTag(videoPlayer.TAG);
						if(videoFragment != null){
							getSupportFragmentManager()
							.beginTransaction()
							.remove(videoFragment)
							.commit();	
						}
						
						
					}
					
					if (fragment != null
							&& fragment.getView() != null
							&& fragment.getView().findViewById(
									R.id.detailGaleryImg) != null) {
						if (fragment.getView()
								.findViewById(R.id.detailGaleryImg)
								.getVisibility() == View.VISIBLE) {
							View fragmentView = fragment.getView();
							fragmentView.setVisibility(View.VISIBLE);
							fragmentView.findViewById(R.id.detailGaleryImg)
									.setVisibility(View.GONE);
							fragmentView.findViewById(R.id.gridview)
									.setVisibility(View.VISIBLE);
							fragmentView.findViewById(R.id.galleryDesc)
									.setVisibility(View.VISIBLE);

						} else {
							view.setVisibility(View.VISIBLE);
						}

					} else {
						view.setVisibility(View.VISIBLE);
					}

				} else {
					getSupportFragmentManager().findFragmentByTag(Home.TAG)
							.getView().setVisibility(View.VISIBLE);
				}

			}
			// do your stuff and Return true to prevent this event from being
			// propagated further

			return true;
		}

		return false;
	}

	// twitter

	@Override
	public void onPause() {
		super.onPause();
		if (twitterSession != null)
			if (twitterSession.isTwitterLoggedInAlready())
				finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
		Log.d(TAG, "Method calldes=>");
		Uri uri = intent.getData();
		if (mAccessTokenTask != null)
			return;
		if (!twitterSession.isTwitterLoggedInAlready()) {
			if (uri != null
					&& uri.getScheme().equals(
							AppConstants.OAUTH_CALLBACK_SCHEME)) {
				// oAuth verifier
				Log.d(TAG, "callback: " + uri.getScheme().toString());
				String verifier = uri
						.getQueryParameter(AppConstants.URL_TWITTER_OAUTH_VERIFIER);
				mAccessTokenTask = new RetriveAcessTokenTask();
				mAccessTokenTask.execute(verifier);

			}
		} else {
			Log.d("masukin", "ini new intent loh mas");
		}
	}

	public void onAuth() {
		// Check if already logged in
		if (mAuthTask != null)
			return;
		/**
		 * Here we could also check for Internet connection
		 */
		if (!twitterSession.isTwitterLoggedInAlready()) {
			mAuthTask = new AuthenticationTask();
			mAuthTask.execute((Void) null);

		} else {
			// user already logged into twitter
			Toast.makeText(this, "Already Logged into twitter",
					Toast.LENGTH_LONG).show();
		}
	}

	public class RetriveAcessTokenTask extends
			AsyncTask<String, Integer, Boolean> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please Wait Retriving Acess.......");
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, params[0]);
				if (accessToken != null) {
					twitterSession.saveSaveSession(accessToken, twitter);
					String username = twitterSession.getUserName();
					Log.d(TAG, "Twitter username=>" + username);
					return true;
				} else {
					twitterSession.logout();
					return false;
				}

			} catch (TwitterException e) {
				//
				e.printStackTrace();
				Log.e(TAG, "TwitterException=>" + e.getMessage());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			mAccessTokenTask = null;
			pDialog.cancel();

			if (result) {
				selectItem(0);
				// start();
			} else {
				Toast.makeText(MainActivity.this, "Please Retry Again",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			mAccessTokenTask = null;
			pDialog.cancel();
		}

	}

	private void start() {
		Log.d("masukin", "onStartCalled()");

	}

	public class AuthenticationTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Please Wait...");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(AppConstants.CONSUMER_KEY);
			builder.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET);
			twitter4j.conf.Configuration config = builder.build();
			TwitterFactory factory = new TwitterFactory(config);
			twitter = factory.getInstance();

			try {
				requestToken = twitter
						.getOAuthRequestToken(AppConstants.OAUTH_CALLBACK_URL);
				Log.d(TAG, "requestToken=>" + requestToken.toString());
				if (requestToken != null) {
					return true;
				}

			} catch (TwitterException e) {
				e.printStackTrace();
				Log.e(TAG, "TwitterException=>" + e.getMessage());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mAuthTask = null;
			dialog.cancel();
			if (!result)
				Toast.makeText(MainActivity.this, "Retry", Toast.LENGTH_SHORT)
						.show();
			else
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(requestToken.getAuthenticationURL())));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mAuthTask = null;
			dialog.cancel();
		}

	}

	public void followTwitter() {
		ProgressDialog pd = new ProgressDialog(mTabbars);
		pd.setMessage("Loading...");
		pd.setCancelable(false);
		pd.show();
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
		// Access Token
		String access_token = twitterSession.getDefaultAccessToaken();
		// Access Token Secret
		String access_token_secret = twitterSession.getDefaultSecret();
		AccessToken accessToken = new AccessToken(access_token,
				access_token_secret);
		Twitter twitter = new TwitterFactory(builder.build())
				.getInstance(accessToken);
		// Update status
		User response;
		try {
			response = twitter.createFriendship("IDFreeport", true);
			Log.d(TAG, "Response Text=>" + response);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User response2;
		try {
			response2 = twitter.createFriendship("IDPersipura", true);
			Log.d(TAG, "Response Text=>" + response2);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pd.dismiss();

		// List<NameValuePair> nameparams2 = new ArrayList<NameValuePair>();
		// nameparams2.add(new BasicNameValuePair("access_token", facebook
		// .getAccessToken()));
		// nameparams2.add(new BasicNameValuePair("object", uri2.toString()));
		//
		// String result2 = WebHTTPMethodClass.executeHttPost(
		// "https://graph.facebook.com/me/og.likes/" + page2, nameparams);
		// Log.d("resultLike", "resultLike2 :" + result2);
	}

}