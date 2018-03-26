package com.airsme.models;

import android.content.Context;
import android.util.Log;

import com.airsme.LoginActivity;
import com.airsme.activity.BDashboard;
import com.airsme.activity.BSignup;
import com.airsme.activity.PDashboard;
import com.airsme.activity.PSignup;
import com.airsme.appconfig.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by M91p-04 on 1/12/2018.
 */

public class JNavigate {
    public static User USER;

    static UserType userType =new UserType();
    static class UserType{
        Boolean notproxy=null;
    }

    public static void updateUserSigned(final Context context){
        //Globals.showprogress(context);
        FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        User user=new User();
        if(USER != null){//if authenticated

            USER.setRegistered(true);
            DBUtil.createModel(USER);
        }
        else{//else login
            login(context);
        }
        //Globals.hideprogress(context);
    }

    public static void overalldecider(final Context context){
        Globals.showprogress(context);
        FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        User user=new User();
        if(cuser != null){//if authenticated
            user.setPKeyValue(cuser.getUid());
            ListenerMgr lmp=new ListenerMgr() {
                @Override
                public void methodHolder(DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy______"+dataSnapshot.getRef().toString());
                    Globals.CURRENT_USER=u;

                    USER=u;
                    Log.e("Rubhabha",USER.toString());
                    if(u==null)return;
                    if(u.isRegistered()){
                        Log.e("Rubhabha","User Registered");
                        Log.e("Rubhabha",User.PROXY);
                        if(User.PROXY.equalsIgnoreCase(u.getRoles())){
                            Globals.nextView(context, PDashboard.class);
                            Log.e("Rubhabha","PDashboard");
                        }
                        else{
                            Globals.nextView(context, BDashboard.class);
                            Log.e("Rubhabha","BDashboard");
                        }
                    }
                    else{
                        Log.e("Rubhabha","Not Signed Up");
                        if(User.PROXY.equalsIgnoreCase(u.getRoles())){
                            Globals.nextView(context, PSignup.class);
                            Log.e("Rubhabha","PSignup");
                        }
                        else{
                            Log.e("Rubhabha","BSignup");
                            Globals.nextView(context, BSignup.class);
                        }
                        Log.e("Rubhabha","Done else");
                    }
                }
            };
            Log.e("Rubhabha","Waller");
            DBUtil.retriaveModelByKey(user, lmp.onchangeListener());

        }
        else{//else login
            Log.e("Rubhabha","ElseLogin");
            login(context);
        }
        Log.e("Rubhabha","Hidding Progress");
        Globals.hideprogress(context);
    }

    static void login(Context context){
        Globals.showprogress(context);

        Globals.nextView(context, LoginActivity.class);

        Globals.hideprogress(context);
    }

    public static void autCheck(Context context){
        Globals.showprogress(context);

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            login(context);

        Globals.hideprogress(context);
    }

    public static void overalldecider3(Context context){
        Globals.showprogress(context);

        Globals.hideprogress(context);
    }
}
