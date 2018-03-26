package com.airsme.activity;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airsme.LoginActivity;
import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.DBUtil;
import com.airsme.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class ChooseSignUp extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    static final String TAG="signup";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_choose_signup);
        populateAutoComplete();

       spinner = (Spinner) findViewById(R.id.account_type);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Choose one");
        categories.add("Business");
        categories.add("Proxy");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        mPasswordView = (EditText) findViewById(R.id.password_choose_signup);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
               /* if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;

                    FIXTHIS
                }*/
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.signupbtn);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.signup_main));
        getSupportActionBar().hide();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    private void attemptSignup() {
        String email=((EditText) findViewById(R.id.email_choose_signup)).getText().toString();
        String pwd=((EditText) findViewById(R.id.password_choose_signup)).getText().toString();
        String pwd2=((EditText) findViewById(R.id.password2_choose_signup)).getText().toString();

        if(validateForm(email, pwd, pwd2))
        createAccount(email,pwd);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        Globals.showprogress(this);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), (spinner.getSelectedItem().toString()
                                    .equalsIgnoreCase(User.PROXY.toString()))?
                                    User.PROXY:User.BUSINESS);
                            Intent intent = new Intent(ChooseSignUp.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        Globals.hideprogress(ChooseSignUp.this);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }



    private boolean validateForm(String email, String pwd, String pwd2) {
        if(spinner.getSelectedItem().toString()
                .equalsIgnoreCase("Choose one")){
            Toast.makeText(this, "Please choose user type!", Toast.LENGTH_LONG);
            return false;}
        if(!pwd.equals(pwd2)){ mPasswordView.setError("Password mismatch");return false;}
        if(pwd.length()<6) {mPasswordView.setError("Password too short");return false;}
        if(!email.contains("@")){ mEmailView.setError("Invalid email");return false;}
        return true;
    }

    // [START basic_write]
    private void writeNewUser(String uid, String role) {
        User user = new User(uid, role);
        DBUtil.createModel(user);
        Globals.msgbox(this, "user created!!");
        //FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}
