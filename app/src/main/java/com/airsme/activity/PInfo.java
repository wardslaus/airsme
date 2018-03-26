package com.airsme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airsme.R;
import com.airsme.appconfig.GlobalStorage;
import com.airsme.appconfig.Globals;
import com.airsme.models.DBUtil;
import com.airsme.models.JNavigate;
import com.airsme.models.Proxy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.angmarch.views.NiceSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PInfo extends AppCompatActivity {

    Proxy proxy = PSignup.proxy;
    NiceSpinner education;
    NiceSpinner transport;
    NiceSpinner employment;
    NiceSpinner profession;
    List<String> educations, transports, employments, professions;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri filePath;
    FirebaseUser user;
    private FirebaseAuth auth;
    CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinfo);
        addspinners();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        imageView=findViewById(R.id.psignup_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();

            }
        });
        Button submitbtn = (Button) findViewById(R.id.pqualbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void addspinners() {

        education = findViewById(R.id.pqual_education);
        transport = findViewById(R.id.pqual_own_transport);
        employment = findViewById(R.id.pqual_employed);
        profession = findViewById(R.id.pqual_profession);

        // Spinner Drop down elements
        educations = new LinkedList<>(Arrays.asList("Metric", "NC", "Diploma", "Honors", "Masters", "PhD"));
        transports = new LinkedList<>(Arrays.asList("I have own transport", "I dont have own transport"));
        employments = new LinkedList<>(Arrays.asList("Employed", "Not employed"));
        professions = new LinkedList<>(Arrays.asList("Engineering", "Web developer", "Teaching", "Farmer"));


        // attaching data adapter to spinner
        education.attachDataSource(educations);
        transport.attachDataSource(transports);
        employment.attachDataSource(employments);
        profession.attachDataSource(professions);

        Log.e("Rubhabha", transports.get(transport.getSelectedIndex()));

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.pqual_main));
        //   getSupportActionBar().setTitle("Qualifications");

    }

    private void submit() {
        EditText skills = findViewById(R.id.pqual_skills);
        if (skills.getText().toString().isEmpty()) {
            skills.setError("Invalid input");
            return;
        } else {
            List<String> al = new ArrayList<>();
            al.add(skills.getText().toString());
            proxy.setSkills(al);
        }

        EditText aboutme = findViewById(R.id.pqual_aboutme);
        if (aboutme.getText().toString().isEmpty()) {
            aboutme.setError("Invalid input");
            return;
        } else {
            proxy.setSurname(aboutme.getText().toString());
        }

        proxy.setEducation(educations.get(education.getSelectedIndex()));
        proxy.setTransport(transports.get(transport.getSelectedIndex()));
        proxy.setEmploymentstatus(employments.get(employment.getSelectedIndex()));
        proxy.setProfession(professions.get(profession.getSelectedIndex()));

        proxy.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.createModel(proxy);

        JNavigate.updateUserSigned(this);
        new GlobalStorage(PInfo.this).uploadImage(filePath, proxy.getPic());
        Globals.nextView(this, PDashboard.class);
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
}
