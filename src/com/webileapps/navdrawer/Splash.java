package com.webileapps.navdrawer;

import java.util.Timer;
import java.util.TimerTask;

import com.persipura.utils.AppConstants;
import com.persipura.utils.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {
	private Timer my_timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		boolean isNetworkAvailable = Utility.isNetworkAvailable(Splash.this);

		if (isNetworkAvailable) {
			my_timer = new Timer();
			my_timer.schedule(new TimerTask() {
				public void run() {
					Intent i = new Intent(Splash.this, MainActivity.class);
					startActivity(i);
					finish();
				}
			}, 2000);
		} else {
			AppConstants.DIALOG_MSG = "Could not connect. Check your connection and try again later.";
			showDialog(AppConstants.DIALOG_ALERT);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (my_timer != null) {
			my_timer.cancel();
			my_timer = null;
		}
	}
}
