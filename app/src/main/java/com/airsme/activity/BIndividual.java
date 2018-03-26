package com.airsme.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.Business;
import com.airsme.models.DBUtil;
import com.airsme.models.Individual;
import com.airsme.models.JNavigate;
import com.google.firebase.auth.FirebaseAuth;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BIndividual extends AppCompatActivity {
    static Business business=BSignup.business;
    static Individual individual=new Individual();
    NiceSpinner position;
    List<String> positions  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindividual);
        addspinners();
        Button submitbtn = (Button) findViewById(R.id.bindividualbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });



        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.bind_main));
//        getSupportActionBar().hide();

    }
    private void addspinners(){

        position =   findViewById(R.id.position);

        // Spinner Drop down elements
        positions = new LinkedList<>(Arrays.asList("--BEE Level--", "1", "2", "3", "4", "5", "6", "7", "8"));
          position.attachDataSource(positions);

    }
    @SuppressLint("ShowToast")
    private void submit(){
        EditText name=findViewById(R.id.bindividual_name);
        if(name.getText().toString().isEmpty()){
            name.setError("Invalid input");
            return;
        }
        else{
            individual.setName(name.getText().toString());
        }

        EditText surname=findViewById(R.id.bindividual_surname);
        if(surname.getText().toString().isEmpty()){
            surname.setError("Invalid input");
            return;
        }
        else{
            individual.setSurname(surname.getText().toString());
        }

        EditText contact=findViewById(R.id.bindividual_contactno);
        if(contact.getText().toString().isEmpty()){
            contact.setError("Invalid input");
            return;
        }
        else{
            individual.setContent(contact.getText().toString());
        }

        EditText email=findViewById(R.id.bindividual_email);
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid input");
            return;
        }
        else{
            individual.setEmail(email.getText().toString());
        }

        if(positions.get(position.getSelectedIndex()).contains("-")){
            Toast.makeText(this, "Please select BEE Level", Toast.LENGTH_SHORT);
            return;
        }
        else{
            individual.setPosition(positions.get(position.getSelectedIndex()));
        }


        //business.getIndividuals().add(individual);

        individual.setBusiness(business);

        business.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        individual.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.createModel(business);
        DBUtil.createModel(individual);

        JNavigate.updateUserSigned(this);

        Globals.nextView(this, BDashboard.class);
    }
}
