package com.airsme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airsme.activity.BSignup;
import com.airsme.activity.PSignup;
import com.airsme.models.DBUtil;
import com.airsme.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity  {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String regtype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();

        btnSignIn =   findViewById(R.id.sign_in_button);
        btnSignUp =  findViewById(R.id.sign_up_button);
        inputEmail =  findViewById(R.id.email);
        inputPassword =   findViewById(R.id.password);
        progressBar =   findViewById(R.id.progressBar);
        FirebaseAuth.getInstance().signOut();
        TextView ttyr  = findViewById(R.id.text);
          regtype = getIntent().getExtras().getString("KEY");

        ttyr.setText(regtype + " SIGNUP" );

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    writeNewUser(user.getUid(), (regtype
                                            .equalsIgnoreCase(User.PROXY.toString()))?
                                            User.PROXY:User.BUSINESS);
                                    if(regtype.equalsIgnoreCase(User.PROXY))
                                        startActivity(new Intent(SignupActivity.this, PSignup.class));
                                    else
                                        startActivity(new Intent(SignupActivity.this, BSignup.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }
    // [START basic_write]
    private void writeNewUser(String uid, String role) {
        User user = new User(uid, role);
        DBUtil.createModel(user);
    //    Globals.msgbox(this, "user created!!");
        //FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}