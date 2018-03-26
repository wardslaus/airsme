package com.airsme.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airsme.R;


public class fragment_settings extends Fragment {
    Activity activity = getActivity();
    public fragment_settings()  {
        // Required empty public constructor
    }
    public static fragment_settings newInstance(String title) {
       fragment_settings f = new fragment_settings();
        Bundle args = new Bundle();
        f.setArguments(args);
       return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // SqLite database handler


        // Displaying the user details on the screen

        return view;
    }


}
