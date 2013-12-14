package com.persipura.socialize;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.application.app.utility.Constants;
import com.application.app.utility.TwitterSession;
import com.facebook.android.Facebook;
import com.persipura.main.MainActivity;
import com.persipura.main.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class TwitterLogoutPage extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes, no;
	public TwitterSession twitterSession;
	public Twitter twitter;

	public TwitterLogoutPage(Activity a, TwitterSession twitterSession, Twitter twitter) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.twitterSession = twitterSession;
		this.twitter = twitter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twitter_logout_page);
		LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
		try {
			Log.d("twitterName", "TwitterName : " + twitter.getScreenName());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yes = (Button) findViewById(R.id.btn_yes);
		yes.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			((MainActivity) this.c).logoutTwitter();
			break;
		default:
			break;
		}
		dismiss();
	}
}