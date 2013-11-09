package com.persipura.socialize;

import com.facebook.android.Facebook;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	
	public Facebook facebook;

	public ShareDialog(Activity a, Facebook facebook) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.facebook = facebook;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share);
		LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        Boolean validFb = this.facebook.isSessionValid();
        Log.d("validFb", "validFb : " + validFb);
		RelativeLayout facebookshare = (RelativeLayout) findViewById(R.id.facebookShare);
		RelativeLayout twittershare = (RelativeLayout) findViewById(R.id.twitterShare);
		
		facebookshare.setOnClickListener(this);
		twittershare.setOnClickListener(this);
        
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.twitterShare:
			
			break;
		case R.id.facebookShare:
			((MainActivity) this.c).shareToFacebook();
			break;
		default:
			break;
		}
		dismiss();
	}
}
