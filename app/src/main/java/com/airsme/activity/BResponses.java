package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.LinearLayout;

import com.airsme.R;
import com.airsme.appconfig.GlobalProxy;
import com.airsme.appconfig.Globals;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;

public class BResponses extends AppCompatActivity {
    static Tender tender;
    static Proxy proxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bresponses);
        Globals.setDummycontext(this);

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.signup_main));
        getSupportActionBar().setTitle("Responses");

        new GlobalProxy(this, (LinearLayout)findViewById(R.id.bresponses_layout), tender).listenResponses(tender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.btender_menu, menu);
        return true;
    }



}
