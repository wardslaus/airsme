package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.airsme.R;

public class BPushNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpush_notification);


        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.signup_main));
        getSupportActionBar().setTitle("Reminder");
    }
}
