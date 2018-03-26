package com.airsme.appconfig;

import android.content.Context;
import android.graphics.Color;
import android.text.DynamicLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airsme.R;
import com.airsme.activity.BTenderInfor;
import com.airsme.activity.BTenderSpecifics;
import com.airsme.activity.PTenderInfor;
import com.airsme.models.Business;
import com.airsme.models.DBUtil;
import com.airsme.models.JNavigate;
import com.airsme.models.ListenerMgr;
import com.airsme.models.Messeges;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by M91p-04 on 1/19/2018.
 */

public class GlobalTender {

    DynamicLayout dynamicLayout;
    private Context context;
    LinearLayout layout;

    private boolean pending=false;
    private boolean isProxy=false;


    private ListenerMgr listenerMgr=new ListenerMgr() {
        @Override
        public void methodHolder(DataSnapshot dataSnapshot) {
            for(DataSnapshot ds:dataSnapshot.getChildren()){
                ListenerMgr lm=new ListenerMgr() {
                    @Override
                    public void methodHolder(DataSnapshot dataSnapshot) {
                        Tender tender=dataSnapshot.getValue(Tender.class);
                        if(tender!=null && tender.getTenderno()!=null){
                                handle(tender);
                        }
                    }
                };
                dataSnapshot.child(ds.getKey()).getRef().addValueEventListener(lm.onchangeListener());
            }
        }
    };
    private ListenerMgr listenerMgrMyTender=new ListenerMgr() {
        @Override
                    public void methodHolder(DataSnapshot dataSnapshot) {
                        Tender tender=dataSnapshot.getValue(Tender.class);
                        if(tender!=null && tender.getTenderno()!=null){
                                handle(tender);
                        }
                    }
    };

    private ListenerMgr alllistenerMgr=new ListenerMgr() {
        @Override
        public void methodHolder(DataSnapshot dataSnapshot) {


            for(DataSnapshot ds:dataSnapshot.getChildren()){

                dataSnapshot.child(ds.getKey()).getRef().addValueEventListener(listenerMgr.onchangeListener());
            }
        }
    };

    private ListenerMgr plistenerMgr=new ListenerMgr() {
        @Override
        public void methodHolder(DataSnapshot dataSnapshot) {
            for(DataSnapshot ds:dataSnapshot.getChildren()){
                //for(String tenderno:buz.getAppliedtenders()){
                ListenerMgr lm=new ListenerMgr() {
                    @Override
                    public void methodHolder(DataSnapshot dataSnapshot) {
                        Tender tender=dataSnapshot.getValue(Tender.class);
                        if(tender!=null && tender.getTenderno()!=null){
                            handle(tender);
                        }
                    }
                };
                dataSnapshot.child(ds.getKey()).getRef().equalTo(dataSnapshot.getRef().getRoot().child(buz.getNodePath())
                        .child("appliedtenders").getKey()).addValueEventListener(lm.onchangeListener());
            //}
            }
        }
    };

    private Business buz;
    private ListenerMgr palllistenerMgr=new ListenerMgr() {
        @Override
        public void methodHolder(DataSnapshot dataSnapshot) {
            for(DataSnapshot ds:dataSnapshot.getChildren()){
                dataSnapshot.child(ds.getKey()).getRef().addValueEventListener(plistenerMgr.onchangeListener());
            }
        }
    };

    public GlobalTender(Context context, LinearLayout layout, boolean isProxy) {
        this.isProxy = isProxy;
        this.context = context;
        this.layout = layout;

        if(Globals.CURRENT_PROXY==null) {
            Globals.CURRENT_PROXY =new Proxy();
            Globals.CURRENT_PROXY.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            DBUtil.retriaveModelByKey(Globals.CURRENT_PROXY, new ListenerMgr() {
                @Override
                public void methodHolder(DataSnapshot dataSnapshot) {
                    Globals.CURRENT_PROXY =dataSnapshot.getValue(Proxy.class);
                }
            }.onchangeListener(), true);
        }

        if(layout!=null)layout.removeAllViewsInLayout();
    }

    public void listenToAllTenders(){
        pending=false;
        DBUtil.listenToNode(alllistenerMgr, "tender");

    }

    public void listenToAppliedTenders(Business b){
        this.buz=b;
        pending=false;
        DBUtil.listenToNode(palllistenerMgr, "tender");
    }

    public void listenMyTenders(){
        pending=false;
        DBUtil.listenToNode(new ListenerMgr() {
            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {
                Proxy p=dataSnapshot.getValue(Proxy.class);
                if(p==null)return;
                for(String tenderno:p.getAppliedtenders()){
                    DBUtil.listenToNode(listenerMgrMyTender, "tender/"+tenderno);
                }
            }}, "proxy/"+ JNavigate.USER.getUid());
    }

    public void listenMyBTenders(){
        pending=false;
        DBUtil.listenToNode(new ListenerMgr() {
            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {

                for(DataSnapshot tndrno:dataSnapshot.getChildren()){
                    tndrno.getRef().addValueEventListener(listenerMgrMyTender.onchangeListener());
                }
            }}, "tender/"+ JNavigate.USER.getUid());
    }

    public void handle(final Tender... tenders) {
        for(Tender t:tenders){
            final Tender tender=t;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BTenderInfor.tender = tender;
                    if(isProxy)
                        Globals.nextView(context, PTenderInfor.class);
                    else
                        Globals.nextView(context, BTenderInfor.class);
                }
            });

            final Button applybtn = new Button(context);
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(tender.getPKeyValue())) {
                applybtn.setText("Edit");
                applybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BTenderSpecifics.tender = tender;
                        Globals.nextView(context, BTenderSpecifics.class);
                    }
                });
            }
            else{
                applybtn.setText("Apply");
                PTenderInfor.tender = tender;
                if(Globals.CURRENT_PROXY.getAppliedtenders().contains(tender.getUniqueLongTenderno())){
                    applybtn.setEnabled(false);
                    applybtn.setText("");
                }
                else{
                    applybtn.setEnabled(true);
                }
                applybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applyTender(tender);
                        applybtn.setEnabled(false);
                        applybtn.setText("");
                        //likeTender(tender, applybtn.getText().toString().equalsIgnoreCase("Like"));
                        //if(Globals.CURRENT_PROXY.getAppliedtenders().contains(tender.getUniqueLongTenderno())){
                        //    applybtn.setEnabled(false);
                       // }
                       // else{
                       //     applybtn.setEnabled(true);
                       // }
                        //Globals.msgbox(context, "Added to your interested tender list!");
                    }
                });
            }

            LinearLayout lytrightbtns = new LinearLayout(context);
            lytrightbtns.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams matchmatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            LinearLayout.LayoutParams matchwrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams wrapwrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams wrapmatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            LinearLayout.LayoutParams imgdim = new LinearLayout.LayoutParams(333,
                    300);


            //Button b = new Button(this);

            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            rl.addRule(RelativeLayout.ALIGN_RIGHT);
            //infobtn.setLayoutParams(rl);
            //RelativeLayout rlytinfo=new RelativeLayout(context);
            applybtn.setLayoutParams(rl);
            lytrightbtns.setLayoutParams(rl);
            RelativeLayout rlytapply=new RelativeLayout(context);

          //   // new RoundViews(context).transparentYellow(applybtn);
            //rlytinfo.addView(infobtn, wrapwrap);
            rlytapply.addView(applybtn, wrapwrap);

            //lytrightbtns.addView(rlytinfo, rl);
            lytrightbtns.addView(rlytapply, rl);
            //lytrightbtns.setPadding(0,0,30,0);

            //lytrightbtns.setBaselineAlignedChildIndex(0);

            //////////////////////////////////////////////////////////

            LinearLayout lyttendetails = new LinearLayout(context);
            lyttendetails.setOrientation(LinearLayout.VERTICAL);
            lyttendetails.setWeightSum(1);

            TextView tendername = new TextView(context);
            TextView daysleft = new TextView(context);
            TextView status = new TextView(context);

            tendername.setText(tender.getName());
            tendername.setTextSize(24);
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            daysleft.setText(df.format(tender.getDate()).toString());
            status.setText(tender.getTenderno());

             // new RoundViews(context).justwhitetext(tendername);
             // new RoundViews(context).justwhitetext(daysleft);
             // new RoundViews(context).justwhitetext(status);

            lyttendetails.addView(tendername, matchwrap);
            lyttendetails.addView(daysleft, matchwrap);
            lyttendetails.addView(status, matchwrap);

            /////////////////////////////////////////////////////////////

            LinearLayout lytimg = new LinearLayout(context);
            lytimg.setOrientation(LinearLayout.HORIZONTAL);

            ImageView prof = new ImageView(context);
            prof.setPadding(0,0,0,0);
            //prof.setCropToPadding(true);
            prof.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            new GlobalStorage(context).loadImage(tender.getImageURL(), prof);

            lytimg.addView(prof, imgdim);
            lytimg.addView(lyttendetails, wrapwrap);
            lytimg.addView(lytrightbtns, rl);

            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(Color.rgb(244, 245, 222));
            layout.setPadding(0,25,0,20);
            layout.addView(lytimg, matchwrap);

             // new RoundViews(context).round(layout);


            LinearLayout lastlayout = new LinearLayout(context);

            lastlayout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(lastlayout, matchwrap);
            layout = lastlayout;
        }

    }

    public void applyTender(final Tender t){
        DBUtil.deleteNode("proxy/"+Globals.CURRENT_USER.getUid()+"/appliedtenders/"+t.getUniqueLongTenderno());
        final Proxy b = new Proxy();
        b.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.retriaveModelByKey(b, new ListenerMgr(){

            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {

                t.addInterest(Globals.CURRENT_USER.getUid());
                DBUtil.createModel(t);


                Proxy bz=dataSnapshot.getValue(Proxy.class);
                bz.getAppliedtenders().add(t.getUniqueLongTenderno());
                bz.setAppliedtenders(new ArrayList<String>(new HashSet<>(bz.getAppliedtenders())));
                DBUtil.createModel(bz);

                Messeges messeges=new Messeges(t, Globals.CURRENT_USER.getUid());
                messeges.addMessege(Globals.CURRENT_USER.getUid(), t.getPKeyValue(),
                        "Hi, I'm writting to show interest applying for this tender.");
                DBUtil.createModel(messeges);

            }
        }.onchangeListener());
    }

    public static void markAsDone(Context context, Tender tender) {
    }

    public static void deleteTender(Context context, Tender tender) {
    }
}
