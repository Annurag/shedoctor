package com.indglobal.shedoctor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.R;

/**
 * Created by Android on 8/29/16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        final int SPLASH_DISPLAY_LENGTH = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String login_id = Comman.getPreferences(SplashActivity.this, "userLogin");
                if (login_id.equalsIgnoreCase("1")) {
                    Intent mainIntent = new Intent(SplashActivity.this, BaseActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, AdminActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
