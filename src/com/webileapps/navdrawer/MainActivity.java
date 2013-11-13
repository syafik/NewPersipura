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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.application.app.utility.Constants;
import com.application.app.utility.TwitterSession;
import com.dg.android.facebook.BaseRequestListener;
import com.dg.android.facebook.SessionEvents;
import com.dg.android.facebook.SessionEvents.AuthListener;
import com.dg.android.facebook.SessionEvents.LogoutListener;
import com.dg.android.facebook.SessionStore;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
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
import com.persipura.media.mediaTerbaru;
import com.persipura.media.pageSliding;
import com.persipura.media.videoPlayer;
import com.persipura.search.Search;
import com.persipura.socialize.FacebookConnectDialog;
import com.persipura.socialize.FacebookLikePage;
import com.persipura.socialize.ShareDialog;
import com.persipura.socialize.TwitterConnectDialog;
import com.persipura.socialize.TwitterFollowPage;
import com.persipura.socialize.TwitterLogoutPage;
import com.persipura.socialize.TwitterSocial;
import com.persipura.squad.DetailSquad;
import com.persipura.squad.Squad;
import com.persipura.utils.AppConstants;
import com.persipura.utils.WebHTTPMethodClass;
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
	private ProgressDialog progressDialog;
	String squadId;
	String titleNav = "Home";
	boolean flag = false;
	Permissions[] permissions = new Permissions[] { Permissions.USER_LIKES,
			Permissions.BASIC_INFO, Permissions.EMAIL, Permissions.USER_PHOTOS,
			Permissions.USER_BIRTHDAY, Permissions.USER_LOCATION,
			Permissions.PUBLISH_ACTION, Permissions.PUBLISH_STREAM

	};

	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private OAuthProvider httpOauthprovider;
	public final static String consumerKey = "qzooqEGzPmfB5Da2qVdsw";
	public final static String consumerSecret = "bTcVQvfUWWhO8JvTLCfVirVUbEFa72QBxp5GfLpYdo";
	private final String CALLBACKURL = "app://twitter";

	// Your Facebook APP ID
	private static String APP_ID = "171262573080320"; // Replace your App ID
	private SessionListener mSessionListener = new SessionListener();
	// here

	// Instance of Facebook Class
	public Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	private ShareActionProvider mShareActionProvider;

	String FILENAME = "AndroidSSO_data";
	SimpleFacebook mSimpleFacebook;
	private static final String TAG = "MainActivity";
	public static final int REQUEST_CODE = 100;
	private SharedPreferences mSharedPreferences;

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

		Log.d("masukin", "ini ngeload lagi loh mas");
		if (savedInstanceState == null) {

			Bundle extras = getIntent().getExtras();
			if (extras == null) {

				selectItem(0);
			} else {
				squadId = extras.getString("squadId");
				if (squadId != null) {
					selectItem(5);
				} else {
					selectItem(0);

				}

			}

		}
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
			Log.d("wewe", "wewgombel");
		}

		Log.d("session",
				"session : " + twitterSession.isTwitterLoggedInAlready());
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
			search.addTextChangedListener(filterTextWatcher);
			search.requestFocus();

			scelta1 = (Button) item.getActionView().findViewById(R.id.scelta1);
			scelta1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (search.length() > 0) {
						selectItem(9);
					}

				}
			});
			break;
		case 2:
			ShareDialog sd = new ShareDialog(MainActivity.this, facebook);
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

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

	};

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("logging", "logging item menu : " + view.getId());
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
		ListGalery listgaleryFragment = (ListGalery) getSupportFragmentManager()
				.findFragmentByTag(ListGalery.TAG);
		HasilPertandingan hasilpertandinganFragment = (HasilPertandingan) getSupportFragmentManager()
				.findFragmentByTag(HasilPertandingan.TAG);
		mediaTerbaru mediaterbaruFragment = (mediaTerbaru) getSupportFragmentManager()
				.findFragmentByTag(mediaTerbaru.TAG);
		videoPlayer videoplayerFragment = (videoPlayer) getSupportFragmentManager()
				.findFragmentByTag(videoPlayer.TAG);
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

			Log.d("homeFragment", "homeFragment : " + homeFragment);

			homeFragment.getView().setVisibility(View.GONE);
		}
		if (pageSlidingFragment != null) {
			Log.d("00000000000", "999999999999999992");
			pageSlidingFragment.getView().setVisibility(View.GONE);
			// // if (mediaterbaruFragment != null) {
			// mediaterbaruFragment.getView().setVisibility(View.GONE);
			// // }
		}

		if (pageSlidingTabStripFragment != null) {
			pageSlidingTabStripFragment.getView().setVisibility(View.GONE);
		}

		if (squadFragment != null) {
			Log.d("squadFragment", "squadFragment : " + squadFragment);
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
			Log.d("00000000000", "999999999999999993");
		}

	}

	private void selectItem(int position) {
		News newsFragment = (News) getSupportFragmentManager()
				.findFragmentByTag(News.TAG);
		Home homeFragment = (Home) getSupportFragmentManager()
				.findFragmentByTag(Home.TAG);
		pageSliding pageSlidingFragment = (pageSliding) getSupportFragmentManager()
				.findFragmentByTag(pageSliding.TAG);
		mediaTerbaru mediaterbaruFragment = (mediaTerbaru) getSupportFragmentManager()
				.findFragmentByTag(mediaTerbaru.TAG);
		PageSlidingTabStripFragment pageSlidingTabStripFragment = (PageSlidingTabStripFragment) getSupportFragmentManager()
				.findFragmentByTag(PageSlidingTabStripFragment.TAG);
		Squad squadFragment = (Squad) getSupportFragmentManager()
				.findFragmentByTag(Squad.TAG);
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(mTabbars);
		String prevFragment = mPrefs.getString("currentFragment", Home.TAG);
		Editor editor = mPrefs.edit();

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
			editor.putString("currentFragment", News.TAG);
			editor.putString("prevFragment", prevFragment);
			editor.commit();
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
			args.putString("squadId", squadId);
			DetailSquad fragment = new DetailSquad();
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			titleNav = "Squad";
			break;
		case 6:
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
		case 7:

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
				boolean followBoth=false;
				try {
					relationship = twitter.showFriendship(twitter.getScreenName(),"IDPersipura");
					followBoth = relationship.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDPersipura is =>" + relationship.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					relationship2 = twitter.showFriendship(twitter.getScreenName(),"IDFreeport");
					followBoth = relationship2.isSourceFollowingTarget();
					Log.d(TAG, "User followingg IDFreeport is =>" + relationship2.isSourceFollowingTarget());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				if(followBoth){
					HideOtherActivities();
//					getSupportFragmentManager().beginTransaction()
//							.add(R.id.content, TwitterSocial.newInstance(), TwitterSocial.TAG)
//							.commit();
					TwitterLogoutPage tlp2 = new TwitterLogoutPage(MainActivity.this, twitterSession, twitter);
					tlp2.show();	
					
				}else{
					TwitterFollowPage tlp = new TwitterFollowPage(MainActivity.this);
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
		case 8:
			final ProgressDialog pd = new ProgressDialog(mTabbars);
			pd.setMessage("Sorry, We are underconstruction");
			pd.setCancelable(false);

			final Handler h = new Handler();
			final Runnable r2 = new Runnable() {

				@Override
				public void run() {
					pd.dismiss();
				}
			};

			Runnable r1 = new Runnable() {

				@Override
				public void run() {
					pd.show();
					h.postDelayed(r2, 5000);
				}
			};

			h.postDelayed(r1, 500);

			pd.show();
			break;
		case 9:

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

	public void logoutTwitter(){
		twitterSession.logout();
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
			mAdapter.addItem(mItemConnect);

			resConnect++;
		}

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	public void loginToFacebook() {
		OnLoginListener onLoginListener = new SimpleFacebook.OnLoginListener() {

			private String TAG;

			@Override
			public void onFail(String reason) {
				Log.w(TAG, reason);
			}

			@Override
			public void onException(Throwable throwable) {
				Log.e(TAG, "Bad thing happened", throwable);
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while login is
				// happening
				Log.i(TAG, "In progress");
			}

			@Override
			public void onLogin() {
				// change the state of the button or do whatever you want
				Log.i(TAG, "Logged in");
				Toast.makeText(getApplicationContext(), "Login SuccessFully",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNotAcceptingPermissions() {
				Log.w(TAG, "User didn't accept read permissions");
			}

		};

		mSimpleFacebook.login(onLoginListener);

	}

	public void loginToTwitter() {
		// final ProgressDialog pd = new ProgressDialog(mTabbars);
		// pd.setMessage("Sorry, We are underconstruction");
		// pd.setCancelable(false);
		//
		// final Handler h = new Handler();
		// final Runnable r2 = new Runnable() {
		//
		// @Override
		// public void run() {
		// pd.dismiss();
		// }
		// };
		//
		// Runnable r1 = new Runnable() {
		//
		// @Override
		// public void run() {
		// pd.show();
		// h.postDelayed(r2, 5000);
		// }
		// };
		//
		// h.postDelayed(r1, 500);
		//
		// pd.show();

		onAuth();

	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// facebook.authorizeCallback(requestCode, resultCode, data);
	//
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// }

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
			// String page1 = "572779142753263";
			// String page2 = "536852216398619";
			// Uri uri1 = Uri.parse("https://facebook.com" + page1);
			// Bundle params = new Bundle();
			// params.putString("object", uri1.toString());
			//
			// Request request = new Request(
			// Session.getActiveSession(),
			// "me/og.likes",
			// params,
			// HttpMethod.POST
			// );
			// Response response = request.executeAndWait();
			// Log.d("response", "response :" + response);
			// Uri uri1 = Uri.parse("https://facebook.com" + page1);
			// Uri uri2 = Uri.parse("https://facebook.com" + page2);
			//
			// List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			// nameparams.add(new BasicNameValuePair("access_token",
			// Session.getActiveSession().getAccessToken()));
			// nameparams.add(new BasicNameValuePair("object",
			// uri1.toString()));
			//
			// String result = WebHTTPMethodClass.executeHttPost(
			// "https://graph.facebook.com/me/og.likes/" + page1, nameparams);
			//
			// try {
			// JSONArray jsonArray = new JSONArray(result);
			// JSONObject resObject = jsonArray.getJSONObject(0);
			// Log.d("errorCode", "errorCode : " + resObject);
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Log.d("resultLike", "resultLike1 :" + result);
			//
			// List<NameValuePair> nameparams2 = new ArrayList<NameValuePair>();
			// nameparams2.add(new BasicNameValuePair("access_token",
			// Session.getActiveSession().getAccessToken()));
			// nameparams2.add(new BasicNameValuePair("object",
			// uri2.toString()));
			//
			// String result2 = WebHTTPMethodClass.executeHttPost(
			// "https://graph.facebook.com/me/og.likes/" + page2, nameparams);
			// Log.d("resultLike", "resultLike2 :" + result2);

		}
		// if (facebook.getAccessToken() != null) {
		// Bundle params = new Bundle();
		// params.putString("access_token", facebook.getAccessToken());
		// Uri uri = Uri.parse("https://facebook.com" + og_object_url);
		// params.putString("og_object_url", uri.toString());
		//
		// mAsyncRunner.request("me/og.likes", params, "POST",
		// new BaseRequestListener() {
		// @Override
		// public void onComplete(final String response,
		// final Object state) {
		// // handle success
		// if(response.contains("Error validating access token")){
		// loginToFacebook();
		// }
		//
		// }
		// }, null);
		// }

		// Dictionary<string, object> parms = new Dictionary<string, object>();
		// parms.Add( "method", "pages.isFan" );
		// parms.Add( "page_id", YourAppId );
		// parms.Add( "uid", UsersFbId );
		//
		// var isFan = fb.Api( parms );
		// if( (bool)isFan )
		// {
		// //this user is a fan
		// }

		// String page1 = "572779142753263";
		// String page2 = "536852216398619";
		//
		// Uri uri1 = Uri.parse("https://facebook.com" + page1);
		// Uri uri2 = Uri.parse("https://facebook.com" + page2);
		//
		// List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
		// nameparams.add(new BasicNameValuePair("access_token", facebook
		// .getAccessToken()));
		// nameparams.add(new BasicNameValuePair("object", uri1.toString()));
		//
		// String result = WebHTTPMethodClass.executeHttPost(
		// "https://graph.facebook.com/me/og.likes/" + page1, nameparams);
		//
		// try {
		// JSONArray jsonArray = new JSONArray(result);
		// JSONObject resObject = jsonArray.getJSONObject(0);
		// Log.d("errorCode", "errorCode : " + resObject);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// Log.d("resultLike", "resultLike1 :" + result);
		//
		// List<NameValuePair> nameparams2 = new ArrayList<NameValuePair>();
		// nameparams2.add(new BasicNameValuePair("access_token", facebook
		// .getAccessToken()));
		// nameparams2.add(new BasicNameValuePair("object", uri2.toString()));
		//
		// String result2 = WebHTTPMethodClass.executeHttPost(
		// "https://graph.facebook.com/me/og.likes/" + page2, nameparams);
		// Log.d("resultLike", "resultLike2 :" + result2);

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
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
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
								postId, Toast.LENGTH_LONG).show();
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
			if (!backstack.isEmpty()) {
				HideOtherActivities();
				getSupportFragmentManager().findFragmentByTag(backstack)
						.getView().setVisibility(View.VISIBLE);
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
				start();
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
		Log.d("masukin", "ini start loh mas");

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
			Log.d("masukin", "ini cancel loh mas");
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