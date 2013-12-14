package com.persipura.socialize;

import com.facebook.android.Facebook;
import com.persipura.main.MainActivity;
import com.persipura.main.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	
	public Facebook facebook;

	public ShareDialog(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share);
		LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
		RelativeLayout facebookshare = (RelativeLayout) findViewById(R.id.facebookShare);
		RelativeLayout twittershare = (RelativeLayout) findViewById(R.id.twitterShare);
		
		facebookshare.setOnClickListener(this);
		twittershare.setOnClickListener(this);
        
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.twitterShare:
			((MainActivity) this.c).shareToTwitter();
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
