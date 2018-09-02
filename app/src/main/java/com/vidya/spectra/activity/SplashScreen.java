package com.vidya.spectra.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.vidya.spectra.R;
import com.vidya.spectra.helper.SessionManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        int SPLASH_TIME_OUT = 800;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // Session manager
                SessionManager session = new SessionManager(getApplicationContext());

                // Check if user is already logged in or not
                if (session.isLoggedIn()) {
                    // User is already logged in. Take him to main activity
                    Intent intent = new Intent(SplashScreen.this, MnActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, WelActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }


        },SPLASH_TIME_OUT);

    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
