package com.vidya.spectra.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vidya.spectra.R;

public class BoyGirlActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boy_girl);


        Toolbar toolbar = findViewById(R.id.toolbar_boy_girl);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_button_24dp);

    }



}
