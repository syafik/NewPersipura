package com.persipura.socialize;

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
import android.webkit.WebView;
import android.widget.Button;

public class FacebookLikePage extends Dialog  {

	public Activity c;
	public Dialog d;
	public Button yes, no;

	public FacebookLikePage(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.facebook_like_page);
		LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
//		yes = (Button) findViewById(R.id.btn_yes);
		WebView webview;
		webview = (WebView) findViewById(R.id.webPersipura);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2F572779142753263&width=10&height=62&colorscheme=light&show_faces=false&header=false&stream=false&show_border=false&appId=171262573080320");
		
		WebView webview2;
		webview2 = (WebView) findViewById(R.id.webFreeport);
		webview2.getSettings().setJavaScriptEnabled(true);
		webview2.loadUrl("http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2F536852216398619&width=10&height=62&colorscheme=light&show_faces=false&header=false&stream=false&show_border=false&appId=171262573080320");
		
	}

}
