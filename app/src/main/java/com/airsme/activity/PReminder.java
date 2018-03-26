package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.airsme.R;

public class PReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preminder);

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.premind_main));
        getSupportActionBar().setTitle("Reminer");
    }
}
