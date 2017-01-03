package com.tringapps.googlemaps;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tringapps.googlemaps.model.Placess;
import com.tringapps.googlemaps.model.Result;

import org.json.JSONObject;


import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, RecyclerAdapter.Callback, View.OnClickListener {
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;
    private Location location;
    private double latitude;
    private double longitude;
    RecyclerView recyclerView;
    EditText searchView;
    ImageView searchButton;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private GoogleMap mMap;
    Marker marker;
    private String apiKey = "AIzaSyBPKBo6VP4JKB4XJil88ubtDq5fqvDS_pM";
    private String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private String type = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocation();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        searchView = (EditText)findViewById(R.id.search_text);
        searchButton = (ImageView) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

    }

    private void setAll(List<Result> results) {
         mMap.clear();
         for(int pos =0;pos<results.size();pos++)
         {

             LatLng sydney = new LatLng(results.get(pos).getGeometry().getLocation().getLat(),results.get(pos).getGeometry().getLocation().getLng());
             mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).position(sydney).title(results.get(pos).getName()));
             mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
             mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
         }

    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);


                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {

                } else {
                    this.canGetLocation = true;


                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).position(sydney).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    public void listener(double a, double b, StringBuilder sb) {
        if(marker != null)
        {
            marker.remove();

        }
            LatLng sydney = new LatLng(a,b);
            marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.loc)).position(sydney).title(sb.toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

    }

    @Override
    public void onClick(View v) {

        type = String.valueOf(searchView.getText());
        StringBuilder sb = new StringBuilder(PLACES_API_BASE);
        sb.append(latitude);
        sb.append(",");
        sb.append(longitude);
        sb.append("&radius=500");
        sb.append("&types=");
        sb.append(type);
        sb.append("&key=");
        sb.append(apiKey);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(sb.toString(),null,new JsonHttpResponseHandler(){



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new GsonBuilder().create();
                Log.e("TAG","responce ............ " +response.toString());
                Placess places = gson.fromJson(response.toString(),Placess.class);
                RecyclerAdapter adapter = new RecyclerAdapter(places.getResults(), MapsActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                setAll(places.getResults());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }


}