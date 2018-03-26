package com.airsme.appconfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.airsme.activity.ChatList;
import com.airsme.LoginActivity;
import com.airsme.R;
import com.airsme.activity.BTenderSpecifics;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by M91p-04 on 1/19/2018.
 */

public class GlobalMenus extends Activity{






    //menu handling


    public static boolean menuhandling(Context context, MenuItem item, boolean isProxy, LinearLayout... layout) {

        switch(item.getItemId()) {
            case R.id.add:
                return(true);
            case R.id.messages:
                 Globals.nextView(context, ChatList.class);
                return true;
            case R.id.alltenders:
                new GlobalTender(context, layout[0], isProxy).listenToAllTenders();
                return true;
            /*case R.id.appliedtenders:
                new GlobalTender(context, layout[0], isProxy).listenToAppliedTenders(BDashboard.business);
                return true;*/
            case R.id.mytenders:
                if(Globals.CURRENT_PROXY!=null){
                    new GlobalTender(context, layout[0], isProxy).listenMyTenders();
                    return true;
                }
                new GlobalTender(context, layout[0], isProxy).listenMyBTenders();
                return true;
            case R.id.addtender:
                Globals.nextView(context, BTenderSpecifics.class);
                return(true);
            case R.id.about:
                return(true);
            case R.id.logout:
                logout(context);
                return(true);

        }
        return false;
    }

    private static void logout(Context c) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(c, LoginActivity.class);
        c.startActivity(intent);
    }
}
