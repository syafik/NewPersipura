package com.persipura.socialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FacebookLikePage extends Dialog implements
android.view.View.OnClickListener{

	public Activity c;
	public Dialog d;
	public Button btn_like_persipura, btn_like_freeport;
	private boolean has_package;

	public FacebookLikePage(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.facebook_like_page2);
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.FILL_PARENT;

		
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		btn_like_persipura = (Button) findViewById(R.id.btn_like_persipura);
		btn_like_freeport = (Button) findViewById(R.id.btn_like_freeport);
		
		btn_like_persipura.setOnClickListener(this);
		btn_like_freeport.setOnClickListener(this);
		String fqlQuery = "SELECT page_id FROM page_fan WHERE uid=me() AND page_id=572779142753263";
		Bundle parameters = new Bundle();
		parameters.putString("q", fqlQuery);
		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", parameters,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						FacebookRequestError error = response.getError();
						
//						WebView webview;
//						webview = (WebView) findViewById(R.id.webPersipura);
//						TextView likedPersipura = (TextView) findViewById(R.id.likedPersipura);
						if (error == null && response.toString().contains("page_id")) {
							btn_like_freeport.setClickable(false);
							btn_like_freeport.setText("LIKED");
							
						}else{
							btn_like_freeport.setClickable(true);
							btn_like_freeport.setText("LIKE");
//							webview.getSettings().setJavaScriptEnabled(true);
//							webview.loadUrl("http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2F572779142753263&width=10&height=62&colorscheme=light&show_faces=false&header=false&stream=false&show_border=false&appId=171262573080320");
						
						}

					}
				});
		Request.executeBatchAsync(request);
		
		
		
		String fqlQuery2 = "SELECT page_id FROM page_fan WHERE uid=me() AND page_id=536852216398619";
		Bundle parameters2 = new Bundle();
		parameters2.putString("q", fqlQuery2);
		Session session2 = Session.getActiveSession();
		Request request2 = new Request(session2, "/fql", parameters2,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response2) {
						Log.i("tag", "Result: " + response2.toString());
						FacebookRequestError error = response2.getError();
//						WebView webview2;
//						webview2 = (WebView) findViewById(R.id.webFreeport);
//						TextView likedFreeport = (TextView) findViewById(R.id.likedFreeport);
						if (error == null && response2.toString().contains("page_id")) {
							btn_like_persipura.setClickable(false);
							btn_like_persipura.setText("LIKED");
//							webview2.setVisibility(View.GONE);
//							likedFreeport.setVisibility(View.VISIBLE);
//							
						}else{
							btn_like_persipura.setClickable(true);
							btn_like_persipura.setText("LIKE");
//							webview2.getSettings().setJavaScriptEnabled(true);
//							webview2.loadUrl("http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2F536852216398619&width=10&height=62&colorscheme=light&show_faces=false&header=false&stream=false&show_border=false&appId=171262573080320");
						}
						
					}
				});
		Request.executeBatchAsync(request2);

		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_like_persipura:
			has_package = false;
			try{
	        	ApplicationInfo info = this.c.getPackageManager().
	                    getApplicationInfo("com.facebook.katana", 0 );
	            Log.d("katana ", "katana info : " + info);
	        	has_package = true;
	        } catch( PackageManager.NameNotFoundException e ){
	        	AlertDialog alertDialog1 = new AlertDialog.Builder(this.c).create();
	            alertDialog1.setTitle("Info");
	            alertDialog1.setCancelable(false);
	            alertDialog1.setMessage("Please Install Facebook for Android to continue this action");
	 
	            alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.dismiss();          
	                }
	            });
	 
	            alertDialog1.show();
	            has_package = false;
	        }
			
			if(has_package){
				((MainActivity) this.c).likePersipuraPage();	
			}
			
			break;
		case R.id.btn_like_freeport:
			has_package = false;
			try{
	        	ApplicationInfo info = this.c.getPackageManager().
	                    getApplicationInfo("com.facebook.katana", 0 );
	            Log.d("katana ", "katana info : " + info);
	        	has_package = true;
	        } catch( PackageManager.NameNotFoundException e ){
	        	AlertDialog alertDialog1 = new AlertDialog.Builder(this.c).create();
	            alertDialog1.setTitle("Info");
	            alertDialog1.setCancelable(false);
	            alertDialog1.setMessage("Please Install Facebook for Android to continue this action");
	 
	            alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.dismiss();          
	                }
	            });
	 
	            alertDialog1.show();
	            has_package = false;
	        }
			
			if(has_package){
				((MainActivity) this.c).likeFreeportPage();	
			}
			
			break;
		default:
			break;
		}
		dismiss();
		
	}

}
