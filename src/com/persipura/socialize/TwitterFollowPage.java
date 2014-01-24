package com.persipura.socialize;

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

public class TwitterFollowPage extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes, no;
	public boolean is_tablet;

	public TwitterFollowPage(Activity a, boolean is_tablet) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.is_tablet = is_tablet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twitter_follow_page);
		LayoutParams params = getWindow().getAttributes();
		if(this.is_tablet){
			params.width = 400;
		}else{
			params.width = LayoutParams.FILL_PARENT;
		}
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			((MainActivity) this.c).followTwitter();
			break;
		case R.id.btn_no:
			dismiss();
			((MainActivity) this.c).selectItem(0);
			break;
		default:
			break;
		}
		dismiss();
	}
}