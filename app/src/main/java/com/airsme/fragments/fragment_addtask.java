package com.airsme.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airsme.R;
import com.airsme.RecyclerViewAdapter;
import com.airsme.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_addtask extends Fragment {
    Activity activity = getActivity();
    String TAG ="Rubhabha";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText addTaskBox;
    private DatabaseReference databaseReference;
    private List<Task> allTask;
    public fragment_addtask()  {
        // Required empty public constructor
    }
    public static fragment_addtask newInstance(String title) {
       fragment_addtask f = new fragment_addtask();
        Bundle args = new Bundle();
        f.setArguments(args);
       return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        View view = inflater.inflate(R.layout.fragment_addtask, container, false);

        allTask = new ArrayList<Task>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addTaskBox = view.findViewById(R.id.add_task_box);
        recyclerView = view.findViewById(R.id.task_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        Button addTaskButton = view.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredTask = addTaskBox.getText().toString();
                if(TextUtils.isEmpty(enteredTask)){
                    Toast.makeText(getActivity(), "You must enter a task first", Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredTask.length() < 6){
                    Toast.makeText(getActivity(), "Task count must be more than 6", Toast.LENGTH_LONG).show();
                    return;
                }
                Task taskObject = new Task(enteredTask);
                databaseReference.push().setValue(taskObject);
                addTaskBox.setText("");
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                taskDeletion(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }
    private void getAllTask(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            String taskTitle = singleSnapshot.getValue(String.class);
            allTask.add(new Task(taskTitle));
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), allTask);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
    private void taskDeletion(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for(int i = 0; i < allTask.size(); i++){
                if(allTask.get(i).getTask().equals(taskTitle)){
                    allTask.remove(i);
                }
            }
            Log.d(TAG, "Task tile " + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), allTask);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

}
