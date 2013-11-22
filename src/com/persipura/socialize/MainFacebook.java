package com.persipura.socialize;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;
import com.webileapps.navdrawer.Splash;

public class MainFacebook extends FragmentActivity {
    private UserSettingsFragment userSettingsFragment;
	private boolean has_package;

    @SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        FragmentManager fragmentManager = getSupportFragmentManager();
        try{
        	ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            Log.d("katana ", "katana info : " + info);
        	has_package = true;
        } catch( PackageManager.NameNotFoundException e ){
        	AlertDialog alertDialog1 = new AlertDialog.Builder(MainFacebook.this).create();
            alertDialog1.setTitle("Info");
            alertDialog1.setCancelable(false);
            alertDialog1.setMessage("Please Install Facebook for Android to continue this action");
 
            alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	finish();          
                }
            });
 
            alertDialog1.show();
            has_package = false;
        }
        
        
        if(has_package){
        	setContentView(R.layout.login_fragment);
        
        	userSettingsFragment.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
            userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
                    Intent intent = new Intent(MainFacebook.this, MainActivity.class);
                    intent.putExtra("callbackFacebook", true);
                    startActivity(intent);
                }
            });
            
        }
        
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
