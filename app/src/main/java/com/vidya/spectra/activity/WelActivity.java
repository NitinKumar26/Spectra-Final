package com.vidya.spectra.activity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.AsyncTaskLoader;

import com.vidya.spectra.R;

import java.util.List;

public class WelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);


        Button logInButton= (Button)findViewById(R.id.login_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new intent to open the login activity
                Intent logInIntent=new Intent(WelActivity.this,LogInActivity.class);
                startActivity(logInIntent);
            }
        });

        Button registerButton= (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new intent to open the login activity
                Intent registerIntent=new Intent(WelActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });





    }
    /**
     * Checks if there is Internet accessible.
     * Based on a stackoverflow snippet
     *
     * @return True if there is Internet. False if not.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
