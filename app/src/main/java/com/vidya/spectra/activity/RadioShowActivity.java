package com.vidya.spectra.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vidya.spectra.R;

public class RadioShowActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_show);


        Toolbar toolbar = findViewById(R.id.toolbar_radio_show);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_button_24dp);

    }
}
