package com.airsme.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airsme.R;
import com.airsme.appconfig.GlobalStorage;
import com.airsme.appconfig.Globals;
import com.airsme.models.DBUtil;
import com.airsme.models.Tender;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BTenderSpecifics extends AppCompatActivity implements
        View.OnClickListener  {
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar ccalendar=Calendar.getInstance();
    private int cmYear, cmMonth, cmDay, cmHour, cmMinute;
    Calendar calendar=Calendar.getInstance();


   public static Tender tender;
    Spinner courieroptions;
    // Spinner Drop down elements
    List<String> beelevels = Arrays.asList("Postnet", "DHL", "Swift", "Post office");


    EditText compulsoryvenue;
    EditText compulsorytime;
    EditText compulsorydate;
    Button compulsorytimebtn;
    Button compulsorydatebtn;
    CheckBox compulsorymeeting;
    LinearLayout compulsorylyt;



    private static final int PICK_IMAGE_REQUEST = 1;
    Uri filePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btender_specifics);
        addspinners();

        compulsoryinit();
        imageView=findViewById(R.id.btender_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();

            }
        });
        if(tender!=null) {
            ((TextView) findViewById(R.id.unit)).setText(tender.getUnit());
            ((TextView) findViewById(R.id.floor)).setText(tender.getFloor());
            ((TextView) findViewById(R.id.street)).setText(tender.getStreet());
            ((TextView) findViewById(R.id.suburb)).setText(tender.getSurbub());
            ((TextView) findViewById(R.id.town)).setText(tender.getTown());
            ((TextView) findViewById(R.id.tenderno)).setText(tender.getTenderno());
            (findViewById(R.id.tenderno)).setEnabled(false);
            ((TextView) findViewById(R.id.tendername)).setText(tender.getName());
            ((TextView) findViewById(R.id.contactperson)).setText(tender.getContactperson());
            ((TextView) findViewById(R.id.specialnotes)).setText(tender.getNotes());
            courieroptions.setSelection(beelevels.indexOf(tender.getCourierOptions()));

            if(tender.isCompulsoryMeeting()){
                compulsoryvenue.setText(tender.getCompulsoryMeetingVenue());
            }

            new GlobalStorage(this).loadImage(tender.getImageURL(), imageView);

        }
        else
            tender=new Tender();
        Button chooselocation = (Button) findViewById(R.id.tender_lecationbtn);
        chooselocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.showMapLocation(BTenderSpecifics.this, tender);
                //tender.jsetMaplocation();
            }
        });

        Button submitbtn = (Button) findViewById(R.id.tender_specsbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtDate.setEnabled(false);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtTime.setEnabled(false);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        compulsorydatebtn=(Button)findViewById(R.id.compulsorymeetingbtn_date);
        compulsorytimebtn=(Button)findViewById(R.id.compulsorymeetingbtn_time);
        compulsorydate=(EditText)findViewById(R.id.compulsorymeetingin_date);
        compulsorydate.setEnabled(false);
        compulsorytime=(EditText)findViewById(R.id.compulsorymeetingin_time);
        compulsorytime.setEnabled(false);

        compulsorydatebtn.setOnClickListener(this);
        compulsorytimebtn.setOnClickListener(this);


        compulsorycheck();
        compulsorymeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compulsorycheck();
            }
        });

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.btenspec_main));
        getSupportActionBar().setTitle(tender.getName());
        olddate();
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);

                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == compulsorydatebtn) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            cmYear = c.get(Calendar.YEAR);
            cmMonth = c.get(Calendar.MONTH);
            cmDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            compulsorydate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            ccalendar.set(Calendar.YEAR, year);
                            ccalendar.set(Calendar.MONTH, monthOfYear);
                            ccalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    }, cmYear, cmMonth, cmDay);
            datePickerDialog.show();
        }
        if (v == compulsorytimebtn) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            cmHour = c.get(Calendar.HOUR_OF_DAY);
            cmMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            compulsorytime.setText(hourOfDay + ":" + minute);

                            ccalendar.set(Calendar.MINUTE, minute);
                            ccalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        }
                    }, cmHour, cmMinute, false);
            timePickerDialog.show();
        }
    }
    public void olddate() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.setTime(tender.getDate());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        txtDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

        txtTime.setText(mHour + ":" + mMinute);
    }
    public void colddate() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.setTime(tender.getDate());
        cmYear = c.get(Calendar.YEAR);
        cmMonth = c.get(Calendar.MONTH);
        cmDay = c.get(Calendar.DAY_OF_MONTH);
        cmHour = c.get(Calendar.HOUR_OF_DAY);
        cmMinute = c.get(Calendar.MINUTE);


        compulsorydate.setText(cmDay + "-" + (cmMonth + 1) + "-" + cmYear);

        compulsorytime.setText(cmHour + ":" + cmMinute);
    }

    private void addspinners(){

        courieroptions = (Spinner) findViewById(R.id.courieroption);

        // Creating adapter for spinner
        ArrayAdapter<String> beelevela = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, beelevels);

        // Drop down layout style - list view with radio button
        beelevela.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        courieroptions.setAdapter(beelevela);

    }
    private void submit(){
        EditText name=findViewById(R.id.unit);
        if(name.getText().toString().isEmpty()){
            name.setError("Invalid input");
            return;
        }
        else{
            tender.setUnit(name.getText().toString());
        }

        EditText surname=findViewById(R.id.floor);
        if(surname.getText().toString().isEmpty()){
            surname.setError("Invalid input");
            return;
        }
        else{
            tender.setFloor(surname.getText().toString());
        }

        EditText street=findViewById(R.id.street);
        if(street.getText().toString().isEmpty()){
            street.setError("Invalid input");
            return;
        }
        else{
            tender.setStreet(street.getText().toString());
        }

        EditText email=findViewById(R.id.suburb);
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid input");
            return;
        }
        else{
            tender.setSurbub(email.getText().toString());
        }

        EditText town=findViewById(R.id.town);
        if(town.getText().toString().isEmpty()){
            town.setError("Invalid input");
            return;
        }
        else{
            tender.setTown(town.getText().toString());
        }

        EditText contact=findViewById(R.id.contactperson);
        if(contact.getText().toString().isEmpty()){
            contact.setError("Invalid input");
            return;
        }
        else{
            tender.setContactperson(contact.getText().toString());
        }

        EditText tendername=findViewById(R.id.tendername);
        if(contact.getText().toString().isEmpty()){
            tendername.setError("Invalid input");
            return;
        }
        else{
            tender.setName(tendername.getText().toString());
        }

        EditText tenderno=findViewById(R.id.tenderno);
        if(contact.getText().toString().isEmpty()){
            tenderno.setError("Invalid input");
            return;
        }
        else{
            tender.setTenderno(tenderno.getText().toString());
        }

        EditText notes=findViewById(R.id.specialnotes);
        if(notes.getText().toString().isEmpty()){
            notes.setError("Invalid input");
            return;
        }
        else{
            tender.setNotes(notes.getText().toString());
        }

        if(tender.jgetMaplocation()==null){
            Toast.makeText(this, "Please choose map location", Toast.LENGTH_SHORT);
            return;
        }

        tender.setCourierOptions(courieroptions.getSelectedItem().toString());

        tender.setDate(calendar.getTime());


        tender.setCompulsoryMeeting(compulsorymeeting.isChecked());

        if(tender.isCompulsoryMeeting()){
            tender.setCompulsoryMeetingDate(ccalendar.getTime());
            tender.setCompulsoryMeetingVenue(compulsoryvenue.getText().toString());
        }

        tender.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DBUtil.createModel(tender);
        new GlobalStorage(BTenderSpecifics.this).uploadImage(filePath, tender.getImageURL());


        Globals.nextView(this, BDashboard.class);
        tender=null;
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
    protected void compulsorycheck() {
        if(compulsorymeeting.isChecked()){
            for( int i = 0; compulsorylyt!=null&&i < compulsorylyt.getChildCount(); i++ ){
                compulsorylyt.getChildAt( i ).setVisibility(View.VISIBLE);
            }
        }
        else{
            for( int i = 0; compulsorylyt!=null&&i < compulsorylyt.getChildCount(); i++ ){
                compulsorylyt.getChildAt( i ).setVisibility(View.INVISIBLE);
            }
        }
    }
    protected void compulsoryinit() {
        compulsorymeeting=findViewById(R.id.compulsorymeeting);
        compulsorylyt=findViewById(R.id.compulsorymeetinglyt);
        compulsorydatebtn=findViewById(R.id.compulsorymeetingbtn_date);
        compulsorytimebtn=findViewById(R.id.compulsorymeetingbtn_time);
        compulsorydate=findViewById(R.id.compulsorymeetingin_date);
        compulsorytime=findViewById(R.id.compulsorymeetingin_time);
        compulsoryvenue=findViewById(R.id.compulsorymeetingvenue);
    }
}
