package com.airsme.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airsme.R;


public class fragment_coming extends Fragment {
    Activity activity = getActivity();
    public fragment_coming()  {
        // Required empty public constructor
    }
    public static fragment_coming newInstance(String title) {
       fragment_coming f = new fragment_coming();
        Bundle args = new Bundle();
        f.setArguments(args);
       return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        View view = inflater.inflate(R.layout.fragment_coming, container, false);



        return view;
    }


}
