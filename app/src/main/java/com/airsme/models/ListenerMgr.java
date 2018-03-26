package com.airsme.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by M91p-04 on 1/12/2018.
 */

public abstract class ListenerMgr {
    abstract public void methodHolder(DataSnapshot dataSnapshot);
    Boolean notproxy=null;
    public ValueEventListener onchangeListener(){
        return new ValueEventListener(){


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                methodHolder(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        };
    }
}
