package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.airsme.R;

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.tncs_main));
        getSupportActionBar().hide();
    }
}
