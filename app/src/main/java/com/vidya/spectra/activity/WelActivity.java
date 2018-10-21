package com.vidya.spectra.activity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vidya.spectra.R;

public class WelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);


        Button logInButton= findViewById(R.id.login_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new intent to open the login activity
                Intent logInIntent=new Intent(WelActivity.this,LogInActivity.class);
                startActivity(logInIntent);
            }
        });

        Button registerButton= findViewById(R.id.register_button);
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
        NetworkInfo activeNetworkInfo = null;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
