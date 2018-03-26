package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.Business;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BSignup extends AppCompatActivity {
    static Business business=new Business();
    NiceSpinner beelevel;
    NiceSpinner btype;
    NiceSpinner bsize;

    List<String> bsizes, btypes, beelevels ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsignup);
        addspinners();
        Button submitbtn = (Button) findViewById(R.id.bsignupbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


    }
    private void addspinners(){

        beelevel =  findViewById(R.id.bsignup_beelevel);
        btype =   findViewById(R.id.bsignup_type);
        bsize =   findViewById(R.id.bsignup_size);

        // Spinner Drop down elements
         beelevels = new LinkedList<>(Arrays.asList("bee1", "bee2", "bee3", "bee4"));
        btypes = new LinkedList<>(Arrays.asList("techno", "services", "mining", "farming"));
        bsizes = new LinkedList<>(Arrays.asList("small", "medium", "large", "xtra large"));

        // attaching data adapter to spinner
        beelevel.attachDataSource(beelevels);
        btype.attachDataSource(btypes);
        bsize.attachDataSource(bsizes);

    }
    private void submit(){
        EditText name=findViewById(R.id.bsignup_name);
        if(name.getText().toString().isEmpty()){
            name.setError("Invalid input");
            return;
        }
        else{
            business.setName(name.getText().toString());
        }

        EditText regno=findViewById(R.id.bsignup_regno);
        if(regno.getText().toString().isEmpty()){
            regno.setError("Invalid input");
            return;
        }
        else{
            business.setRegno(regno.getText().toString());
        }

        EditText vatno=findViewById(R.id.bsignup_vatno);
        if(vatno.getText().toString().isEmpty()){
            vatno.setError("Invalid input");
            return;
        }
        else{
            business.setVatno(vatno.getText().toString());
        }

        EditText website=findViewById(R.id.bsignup_website);
        if(website.getText().toString().isEmpty()){
            website.setError("Invalid input");
            return;
        }
        else{
            business.setWebsite(website.getText().toString());
        }

        EditText tax=findViewById(R.id.bsignup_taxclearance);
        if(tax.getText().toString().isEmpty()){
            tax.setError("Invalid input");
            return;
        }
        else{
            business.setTaxclearance(tax.getText().toString());
        }

        business.setBeelevel( beelevels.get(beelevel.getSelectedIndex()));
        business.setSize(bsizes.get(bsize.getSelectedIndex()));
        business.setType(btypes.get(btype.getSelectedIndex()));




        Globals.nextView(this, BIndividual.class);
    }


}
