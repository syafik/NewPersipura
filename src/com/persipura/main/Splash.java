package com.persipura.main;

import java.util.Timer;
import java.util.TimerTask;

import com.persipura.utils.AppConstants;
import com.persipura.utils.Utility;
import com.persipura.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Splash extends Activity {
	private Timer my_timer;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	
		boolean isNetworkAvailable = Utility.isNetworkAvailable(Splash.this);
		Log.d("isTablet?", "isTablet? " + isTablet(getApplicationContext()));
		if (isNetworkAvailable) {
			my_timer = new Timer();
			my_timer.schedule(new TimerTask() {
				public void run() {
					Intent i;
					if(isTablet(getApplicationContext())){
						i = new Intent(Splash.this, MainActivity.class);	
					}else{
						i = new Intent(Splash.this, MainActivity.class);
					}
					
					startActivity(i);
					finish();
				}
			}, 2000);
		} else {
			String msg = "Could not connect. Check your connection and try again later.";
			try {

				AlertDialog alertDialog1 = new AlertDialog.Builder(Splash.this)
						.create();
				alertDialog1.setTitle("Info");
				alertDialog1.setCancelable(false);
				alertDialog1.setMessage(msg);

				alertDialog1.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								moveTaskToBack(true);
							}
						});

				alertDialog1.show();
			} catch (Exception e) {
				Log.d("ConnectionProblem", "Show Dialog: " + e.getMessage());
			}
		}
	}

	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
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
