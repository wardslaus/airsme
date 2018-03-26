package com.airsme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airsme.R;
import com.airsme.appconfig.Globals;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PBanking extends AppCompatActivity {
    NiceSpinner bankname;
    List<String> banknames;
    private Button btnproceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbanking);
        bankname = findViewById(R.id.psignup_bankname);
        banknames = new LinkedList<>(Arrays.asList("FNB", "ABSA", "Standard", "IMB"));
        bankname.attachDataSource(banknames);

        btnproceed =  findViewById(R.id.btnNext);
        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.nextView(PBanking.this, PCong.class);

            }
        });



    }
}
