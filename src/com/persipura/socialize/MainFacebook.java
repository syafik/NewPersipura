package com.persipura.socialize;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.*;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

public class MainFacebook extends Activity {
	String APP_ID = "171262573080320"; // Replace your App ID

	protected static final String TAG = MainFacebook.class.getName();

	SimpleFacebook mSimpleFacebook;

	Permissions[] permissions = new Permissions[] { Permissions.USER_PHOTOS,
			Permissions.EMAIL, Permissions.PUBLISH_ACTION };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// test local language

		setContentView(R.layout.activity_main);

		// 1. Login example
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(APP_ID).setNamespace("webileapps")
				.setPermissions(permissions).build();
		SimpleFacebook.setConfiguration(configuration);
		
		Session session = Session.getActiveSession();
		
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
			Log.d("wewe", "wewgombel");
		}

		
		  Session.openActiveSession(this, true, new Session.StatusCallback() {

			    // callback when session changes state
			    @Override
			    public void call(Session session, SessionState state, Exception exception) {
//			    	 if (session.isOpened()) {
			    		 Intent intent = new Intent(MainFacebook.this, MainActivity.class);
			    		 startActivity(intent);
//			    	 }
			    }
			  });
	}
	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("Logged", "Logged in...");
			
		} else if (state.isClosed()) {
			Log.i("Logged", "Logged out...");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

}
