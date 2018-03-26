package com.airsme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airsme.R;
import com.airsme.SplashScreen;
import com.airsme.appconfig.Globals;
import com.google.firebase.auth.FirebaseAuth;

public class PTerms extends AppCompatActivity {
    private Button btnproceed, btncancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pterms);

        btnproceed =  findViewById(R.id.accept);
        btncancel = findViewById(R.id.logout);
        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.nextView(PTerms.this, PGetstart.class);

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PTerms.this, SplashScreen.class));

            }
        });
    }
}
