package com.airsme.activity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airsme.R;
import com.airsme.models.Business;
import com.airsme.models.Model;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    public static Model model;
    GoogleMap mMap;
    LatLng latLng;
    boolean showonly = false;
    private String locationtitle;
   public  static LatLng latLngs;
    public static boolean showonlys = false;
    public static String locationtitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        latLng=latLng;
        showonly=showonlys;
        locationtitle=locationtitles;


        MapsMarkerActivity.latLngs=null;
        MapsMarkerActivity.showonlys=false;
        MapsMarkerActivity.locationtitles=null;
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button submitbtn = (Button) findViewById(R.id.marklocation);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showonly) {
                    if (model instanceof Tender) {
                        ((Tender) model).jsetMaplocation(latLng);
                    }
                    if (model instanceof Proxy) {
                        ((Proxy) model).jsetMaplocation(latLng);
                    }
                    if (model instanceof Business) {
                        ((Business) model).jsetMaplocation(latLng);
                    }
                }
                finishActivity(1);
                onBackPressed();
            }
        });

        //new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.maps_main));
        getSupportActionBar().hide();
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (showonly && latLng != null)
            mark(latLng, "", true);
        else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                LatLng sydney = new LatLng(-33.9238445,18.4244628);
                mark(sydney, "Cape Town", true);

            }
            else {
                googleMap.setMyLocationEnabled(true);
                Location myLocation = googleMap.getMyLocation();  //Nullpointer exception.........
                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                        myLocation.getLongitude());

                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(myLatLng).zoom(100).bearing(90).tilt(30).build();
                googleMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(myPosition));
            }
        }


        if(!showonly)
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mark(latLng, locationtitle, false);
            }
        });
    }

    private Marker lastmark;
    public void mark(LatLng latLng, String title, boolean movecamera) {
        this.latLng=latLng;
        if(lastmark!=null){lastmark.remove();}
        MarkerOptions mo=new MarkerOptions().position(latLng).title(title);

        lastmark=mMap.addMarker(mo);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 100.0f ) );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        if(movecamera)mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
public static final double PRICE_PER_KM=0.65;//dollars not rands
    public static double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        double r=Radius * c;
        return Math.round((r) * 10) / 10.0;


    }
    public static double calculateCost(LatLng StartP, LatLng EndP) {

        double r= calculationByDistance(StartP, EndP) * PRICE_PER_KM;
        return Math.round((r) * 100) / 100.0;
    }
}
