package com.airsme.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.airsme.R;
import com.airsme.fragments.fragment_chat;
import com.airsme.fragments.fragment_coming;
import com.airsme.fragments.fragment_contact;
import com.airsme.fragments.fragment_jobboard;
import com.airsme.fragments.fragment_main;
import com.airsme.fragments.fragment_settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class PDashboard extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private IProfile profile;
    private IProfile profile2;
    FirebaseUser user;
    private FirebaseAuth auth;
    Fragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdashboard);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Toolbar toolbar =   findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AirSME");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        Log.e("Rubhabha", "PDashboard");
        profile = new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(Uri.parse(user.getPhotoUrl() + ""));
        profile2 = new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(getResources().getDrawable(R.drawable.logo)).withIdentifier(2);
        buildHeader(false, savedInstanceState);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                       new PrimaryDrawerItem().withName("Job Board").withIcon(FontAwesome.Icon.faw_briefcase).withIdentifier(1)
                      , new SecondaryDrawerItem().withName("Contact").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(2)
                       , new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(3)


                ) // add the items we want to use with our Drawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        PDashboard.this.finish();
                        //return true if we have consumed the event
                        return true;
                    }
                }).addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Chat").withIcon(FontAwesome.Icon.faw_comments).withIdentifier(4),
                        new SecondaryDrawerItem().withName("LogOut").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(100)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 10000) {
                                frag = fragment_main.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            }  else if (drawerItem.getIdentifier() == 1) {
                                frag = fragment_jobboard.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            } else if (drawerItem.getIdentifier() == 2) {
                                frag = fragment_contact.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            } else if (drawerItem.getIdentifier() == 3) {
                                frag = fragment_settings.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            }  else if (drawerItem.getIdentifier() == 4) {
                                frag = fragment_chat.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            } else if (drawerItem.getIdentifier() == 100) {

                                auth.signOut();
                                finish();
                            } else {
                                frag = fragment_coming.newInstance("Demo");
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();

                            }
                        }

                        return false;
                    }
                })

                .withSavedInstance(savedInstanceState)
                .build();

        if (savedInstanceState == null) {
            Fragment f = fragment_jobboard.newInstance("Demo");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).commit();


        }
    }


    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(
                        profile,
                        profile2
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }
}
