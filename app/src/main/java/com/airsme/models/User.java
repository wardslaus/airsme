package com.airsme.models;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User extends Model {

    public static final String BUSINESS="BUSINESS";
    public static final String PROXY="PROXY";

    private String ID;
    private String uid;
    private String roles;
    private boolean registered;

    @Override
    public String getNode() {
        return "user";
    }

    @Override
    public String getPKeyValue() {
        return uid;
    }

    @Override
    public String getPKeyName() {
        return "uid";
    }

    @Override
    public void setPKeyValue(String id) {
        this.uid=id;
        this.ID = uid;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
        this.uid=ID;
    }

   // public static enum Role{BUSINESS, PROXY};

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        this.ID = uid;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String roles) {
        this.uid = uid;
        this.ID = this.uid;
        this.roles = roles;
    }

    public boolean isRegistered(ChildEventListener cl) {
        Proxy p=new Proxy();
        Business b=new Business();
        p.setID(this.getUid());
        b.setID(this.getUid());
        return true;
        //return DBUtil.retriaveModelByKey(b)!=null||DBUtil.retriaveModelByKey(p)!=null;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", uid='" + uid + '\'' +
                ", roles=" + roles +
                ", registered=" + registered +
                '}';
    }
}
// [END blog_user_class]
