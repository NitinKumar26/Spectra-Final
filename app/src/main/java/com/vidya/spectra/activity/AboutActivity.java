package com.vidya.spectra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.vidya.spectra.R;

public class AboutActivity extends AppCompatActivity {

        private static final String FACEBOOK_URL = "https://www.facebook.com/Spectra6/?ref=br_rs";
        private static final String FACEBOOK_PAGE_ID = "Spectra6";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_button_24dp);
        }


        //Setting up Floating Action Button
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutActivity.this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);

            }
        });

    }

        //method to get the right URL to use in the intent
        private String getFacebookPageURL(Context context) {
            PackageManager packageManager = context.getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

                boolean activated =  packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
                if(activated){
                    if ((versionCode >= 3002850)) {
                        return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else {
                        return "fb://page/" + FACEBOOK_PAGE_ID;
                    }
                }else{
                    return FACEBOOK_URL;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return FACEBOOK_URL;
            }
        }
}
