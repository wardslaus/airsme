package com.airsme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airsme.models.JNavigate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingActivity extends AppCompatActivity {
    private Button btnSignup, btnLogin;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        btnSignup =  findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            updateUI(auth.getCurrentUser());
            finish();
            Log.e("Rubhabha","User Already Logged In " );
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, PBSignupActivity.class));
            }
        });


    }

    private void updateUI(FirebaseUser user) {
        //Globals.msgbox(this, "updateuistart "+(user==null?"  null":user.getUid()));
        JNavigate.overalldecider(this);
    }
}
