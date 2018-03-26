package com.airsme.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TenderDate extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Date pickedDate;
    FragmentManager fragmentManager = new FragmentManager() {
        @Override
        public FragmentTransaction beginTransaction() {
            return null;
        }

        @Override
        public boolean executePendingTransactions() {
            return false;
        }

        @Override
        public Fragment findFragmentById(int id) {
            return null;
        }

        @Override
        public Fragment findFragmentByTag(String tag) {
            return null;
        }

        @Override
        public void popBackStack() {

        }

        @Override
        public boolean popBackStackImmediate() {
            return false;
        }

        @Override
        public void popBackStack(String name, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(String name, int flags) {
            return false;
        }

        @Override
        public void popBackStack(int id, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(int id, int flags) {
            return false;
        }

        @Override
        public int getBackStackEntryCount() {
            return 0;
        }

        @Override
        public BackStackEntry getBackStackEntryAt(int index) {
            return null;
        }

        @Override
        public void addOnBackStackChangedListener(OnBackStackChangedListener
                                                          listener) {

        }

        @Override
        public void removeOnBackStackChangedListener(OnBackStackChangedListener
                                                             listener) {

        }

        @Override
        public void putFragment(Bundle bundle, String key, Fragment fragment) {

        }

        @Override
        public Fragment getFragment(Bundle bundle, String key) {
            return null;
        }

        @Override
        public List<Fragment> getFragments() {
            return null;
        }

        @Override
        public SavedState saveFragmentInstanceState(Fragment f) {
            return null;
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }

        @Override
        public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks
                                                               cb, boolean recursive) {

        }

        @Override
        public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks
                                                                 cb) {

        }

        @Override
        public Fragment getPrimaryNavigationFragment() {
            return null;
        }

        @Override
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

        }

        @Override
        public boolean isStateSaved() {
            return false;
        }
    };

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

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        pickedDate = c.getTime();
    }

    public Date getPickedDate() {
        return pickedDate;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new TenderDate();
        newFragment.show(fragmentManager, "datePicker");

    }

    static public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        TenderDate tenderDate = new TenderDate();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Calendar c = Calendar.getInstance();
            c.setTime(tenderDate.pickedDate);
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            tenderDate.pickedDate = c.getTime();
        }

        public void showTimePickerDialog(View v) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(tenderDate.fragmentManager, "timePicker");
        }
    }
}