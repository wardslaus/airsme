package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airsme.R;

public class SuccessMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_message);

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.successmsg_main));
        getSupportActionBar().setTitle("Message");
    }
}
