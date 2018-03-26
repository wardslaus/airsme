package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airsme.R;
import com.airsme.appconfig.Globals;

public class PCong extends AppCompatActivity {
    private Button btnproceed, btncancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcong);

        btnproceed =  findViewById(R.id.accept);

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.nextView(PCong.this, PDashboard.class);

            }
        });


    }
}
