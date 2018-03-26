package com.airsme.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.helpers.TenderHelper;
import com.airsme.models.Business;
import com.airsme.models.DBUtil;
import com.airsme.models.ListenerMgr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class fragment_bookings extends Fragment {
    Activity activity = getActivity();
    static Business business;

    private String TAG ="Rubhabha";


    private ListView listView;

    private List<TenderHelper> tenderList;
    private int offSet = 0;
    Button btnPending , btnNew ;
    public fragment_bookings()  {
        // Required empty public constructor
    }
    public static fragment_bookings newInstance(String title) {
       fragment_bookings f = new fragment_bookings();
        Bundle args = new Bundle();
        f.setArguments(args);
       return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        //listView =  view.findViewById(R.id.listView);

        tenderList = new ArrayList<>();
        btnPending = view.findViewById(R.id.btnPending);
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f = fragment_bookings.newInstance("Demo");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).commit();
            }
        });

        btnNew = view.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f = fragment_tenderspecs.newInstance("Demo");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).commit();
            }
        });

        Globals.setDummycontext(activity);
        business=new Business();
        business.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.retriaveModelByKey(business, new ListenerMgr(){

            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {
                Log.e("Rubhabha", dataSnapshot.toString());
                Log.e("Rubhabha", "dataSnapshot.toString()");
                business=dataSnapshot.getValue(Business.class);

            }
        }.onchangeListener());


      //  new GlobalTender(getActivity(), (LinearLayout) view.findViewById(R.id.bdashboard_layout), false).listenMyBTenders();
        return view;
    }


}
