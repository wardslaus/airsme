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
import com.airsme.activity.BProxyInfor;
import com.airsme.activity.MapsMarkerActivity;
import com.airsme.activity.Message;

import com.airsme.models.DBUtil;
import com.airsme.models.JNavigate;
import com.airsme.models.ListenerMgr;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by M91p-04 on 1/19/2018.
 */

public class GlobalProxy {

    DynamicLayout dynamicLayout;
    private Context context;
    LinearLayout layout;
    Tender subject;

    Map<Proxy, Tender> map=new HashMap<>();

    private boolean pending=false;
    private boolean isProxy=false;

    public GlobalProxy(Context context, LinearLayout layout, Tender subjecttender) {
        this.context = context;
        this.layout = layout;
        this.subject = subjecttender;

        layout.removeAllViewsInLayout();
    }


    private ListenerMgr listenerMgr=new ListenerMgr() {
        @Override
        public void methodHolder(DataSnapshot dataSnapshot) {
            Proxy proxy=dataSnapshot.getValue(Proxy.class);
            handle(proxy);
        }
    };

    public void listenResponses(Tender t){
        for(String s:t.getInterests()){
            Proxy p=new Proxy();
            p.setPKeyValue(s);

            map.put(p,t);
            DBUtil.retriaveModelByKey(p, listenerMgr.onchangeListener());
        }
    }

    public void listenResponses(){
        pending=false;
        DBUtil.listenToNode(new ListenerMgr() {
            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {

                for(DataSnapshot tndrno:dataSnapshot.getChildren()){
                    tndrno.getRef().addValueEventListener(new ListenerMgr() {
                        @Override
                        public void methodHolder(DataSnapshot dataSnapshot) {
                            Tender tender=dataSnapshot.getValue(Tender.class);
                            if(tender!=null && tender.getTenderno()!=null){
                                listenResponses(tender);
                            }
                        }
                    }.onchangeListener());
                }
            }}, "tender/"+ JNavigate.USER.getUid());
    }

    public void handle(final Proxy... proxys) {
        for(Proxy t:proxys){
            final Proxy proxy=t;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BProxyInfor.proxy = proxy;
                    BProxyInfor.subject = subject;
                    Globals.nextView(context, BProxyInfor.class);
                }
            });
            subject=map.get(proxy);

            final Button applybtn = new Button(context);
                applybtn.setText("Chat");
                applybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message.receiver = proxy;
                        Message.subject = subject;
                        Globals.nextView(context, Message.class);
                    }
                });
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
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rl.addRule(RelativeLayout.ALIGN_RIGHT);
            //infobtn.setLayoutParams(rl);
            RelativeLayout rlytinfo=new RelativeLayout(context);
            applybtn.setLayoutParams(rl);
            lytrightbtns.setLayoutParams(rl);
            RelativeLayout rlytapply=new RelativeLayout(context);



           //  // new RoundViews(context).transparentYellow(applybtn);

            //rlytinfo.addView(infobtn, wrapwrap);
            rlytapply.addView(applybtn, wrapwrap);

            lytrightbtns.addView(rlytinfo, rl);
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

            tendername.setText(proxy.getName()+" ");
            tendername.setTextSize(24);
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            daysleft.setText(proxy.getProfession());
            status.setText(MapsMarkerActivity.calculationByDistance(proxy.jgetMaplocation(), subject.jgetMaplocation())+"KM");



          /*   // new RoundViews(context).justwhitetext(tendername);
             // new RoundViews(context).justwhitetext(daysleft);
             // new RoundViews(context).justwhitetext(status);
*/

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
            new GlobalStorage(context).loadImage(proxy.getPic(), prof);

            lytimg.addView(prof, imgdim);
            lytimg.addView(lyttendetails, wrapwrap);
            lytimg.addView(lytrightbtns, rl);

            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(Color.rgb(244, 245, 222));
            layout.setPadding(0,25,0,20);
            layout.addView(lytimg, matchwrap);


           /*  // new RoundViews(context).round(layout);
*/
            LinearLayout lastlayout = new LinearLayout(context);

            lastlayout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(lastlayout, matchwrap);
            layout = lastlayout;
        }
    }

    public void likeTender(final Tender t, final boolean liked){
        DBUtil.deleteNode("proxy/"+Globals.CURRENT_USER.getUid()+"/appliedtenders/"+t.getUniqueLongTenderno());
        final Proxy b = new Proxy();
        b.setPKeyValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBUtil.retriaveModelByKey(b, new ListenerMgr(){

            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {
                Proxy bz=dataSnapshot.getValue(Proxy.class);
                if(liked)
                    bz.getAppliedtenders().add(t.getUniqueLongTenderno());
                else
                    bz.getAppliedtenders().remove(t.getUniqueLongTenderno());
                DBUtil.createModel(bz);
            }
        }.onchangeListener());
    }
}
