package com.airsme.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.Proxy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PSignup extends AppCompatActivity {
    Spinner title;
    Spinner bank;
    static final Proxy proxy=new Proxy();
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri filePath;
    FirebaseUser user;
    private FirebaseAuth auth;
    CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psignup);
        addspinners();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Button submitbtn = (Button) findViewById(R.id.psignupbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });



        EditText email2 = findViewById(R.id.psignup_email);
        email2.setText(user.getEmail());
        Button chooselocation = (Button) findViewById(R.id.psignupmap);
        chooselocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.showMapLocation(PSignup.this, proxy);
                //tender.jsetMaplocation();
            }
        });

       // // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.psignup_main));
      //  getSupportActionBar().hide();
    }
    private void addspinners(){


        title = (Spinner) findViewById(R.id.psignup_title);


        // Spinner Drop down elements
        List<String> titles = Arrays.asList("Mr.", "Mrs.", "Dr.", "Miss");

        // Creating adapter for spinner
        ArrayAdapter<String> titlesa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, titles);

        // Drop down layout style - list view with radio button
        titlesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        title.setAdapter(titlesa);



    }
    private void submit(){
        EditText name=findViewById(R.id.psignup_name);
        if(name.getText().toString().isEmpty()){
            name.setError("Invalid input");
            return;
        }
        else{
            proxy.setName(name.getText().toString());
        }

        EditText surname=findViewById(R.id.psignup_surname);
        if(surname.getText().toString().isEmpty()){
            surname.setError("Invalid input");
            return;
        }
        else{
            proxy.setSurname(surname.getText().toString());
        }

        EditText contact=findViewById(R.id.psignup_contact);
        if(contact.getText().toString().isEmpty()){
            contact.setError("Invalid input");
            return;
        }
        else{
            proxy.setContact(contact.getText().toString());
        }

        EditText email=findViewById(R.id.psignup_email);
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid input");
            email.setEnabled(true);
            return;
        }
        else{
            proxy.setEmail(email.getText().toString());
        }

        EditText address=findViewById(R.id.psignup_address);
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid input");
            return;
        }
        else{
            proxy.setAddress(email.getText().toString());
        }

        EditText dob=findViewById(R.id.psignup_dob);
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid input");
            return;
        }
        else{
       proxy.setDob(new Date());
        }




        proxy.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //DBUtil.createModel(proxy);


        if(proxy.jgetMaplocation()==null){
            Toast.makeText(this, "Please choose map location", Toast.LENGTH_SHORT);
            return;
        }

        Globals.nextView(this, PTerms.class);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

}
