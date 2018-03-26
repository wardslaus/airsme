package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.DBUtil;
import com.airsme.models.JNavigate;
import com.airsme.models.Proxy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PQualifications extends AppCompatActivity {

    Proxy proxy=PSignup.proxy;
    Spinner education;
    Spinner transport;
    Spinner employment;
    Spinner profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pqualifications);
        addspinners();
        Button submitbtn = (Button) findViewById(R.id.pqualbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }
    private void addspinners(){

        education = (Spinner) findViewById(R.id.pqual_education);
        transport = (Spinner) findViewById(R.id.pqual_own_transport);
        employment = (Spinner) findViewById(R.id.pqual_employed);
        profession = (Spinner) findViewById(R.id.pqual_profession);

        // Spinner Drop down elements
        List<String> educations = Arrays.asList("Metric", "NC", "Diploma", "Honors", "Masters", "PhD");
        List<String> transports = Arrays.asList("I have own transport", "I dont have own transport");
        List<String> employments = Arrays.asList("Employed", "Not employed");
        List<String> professions = Arrays.asList("Engineering", "Web developer", "Teaching", "Farmer");


        // Creating adapter for spinner
        ArrayAdapter<String> educationsa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, educations);
        ArrayAdapter<String> transportsa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transports);
        ArrayAdapter<String> employmentsa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employments);
        ArrayAdapter<String> professionsa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, professions);

        // Drop down layout style - list view with radio button
        educationsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        education.setAdapter(educationsa);
        transport.setAdapter(transportsa);
        employment.setAdapter(employmentsa);
        profession.setAdapter(professionsa);



        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.pqual_main));
     //   getSupportActionBar().setTitle("Qualifications");

    }
    private void submit(){
        EditText skills=findViewById(R.id.pqual_skills);
        if(skills.getText().toString().isEmpty()){
            skills.setError("Invalid input");
            return;
        }
        else{
            List<String> al=new ArrayList<>();
            al.add(skills.getText().toString());
            proxy.setSkills(al);
        }

        EditText aboutme=findViewById(R.id.pqual_aboutme);
        if(aboutme.getText().toString().isEmpty()){
            aboutme.setError("Invalid input");
            return;
        }
        else{
            proxy.setSurname(aboutme.getText().toString());
        }

        proxy.setEducation(education.getSelectedItem().toString());
        proxy.setTransport(transport.getSelectedItem().toString());
        proxy.setEmploymentstatus(employment.getSelectedItem().toString());
        proxy.setProfession(profession.getSelectedItem().toString());

        proxy.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.createModel(proxy);

        JNavigate.updateUserSigned(this);

        Globals.nextView(this, PDashboard.class);
    }
}
