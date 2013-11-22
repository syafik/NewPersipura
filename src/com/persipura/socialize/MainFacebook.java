package com.persipura.socialize;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

public class MainFacebook extends FragmentActivity {
    private UserSettingsFragment userSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
