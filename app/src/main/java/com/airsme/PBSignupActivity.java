package com.airsme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airsme.models.JNavigate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PBSignupActivity extends AppCompatActivity {
    private Button btnSignup, btnLogin;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbsignup);
        btnSignup =  findViewById(R.id.btn_indi);
        btnLogin = findViewById(R.id.btn_bus);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PBSignupActivity.this, SignupActivity.class);
                i.putExtra("KEY","BUSINESS");
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PBSignupActivity.this, SignupActivity.class);

                i.putExtra("KEY","PROXY");
                startActivity(i);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        //Globals.msgbox(this, "updateuistart "+(user==null?"  null":user.getUid()));
        JNavigate.overalldecider(this);
    }
}
