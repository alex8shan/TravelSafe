package com.example.ridesafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks{

    private final String TAG = "RideSafe";
    private TextView txtMain;
    private GoogleMap mMap;
    private boolean pGranted;
    private GoogleApiClient mgac;
    private LocationRequest rl;
    private Marker dest;
    private LatLng desti;
    private boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mgac = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        txtMain = (TextView) findViewById(R.id.txtMain);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (dest != null){
                    dest.remove();
                }
                dest = mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));
                desti = latLng;
                txtMain.setText(latLng.toString());
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mgac.connect();
        first = true;
    }

    @Override
    protected void onStop(){
        mgac.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle){
        rl = LocationRequest.create();
        rl.setInterval(1000);
        rl.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mgac, rl, this);
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.i(TAG, "Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.i(TAG, "Connection failed!");
    }

    @Override
    public void onLocationChanged(Location location){
        if (first){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
            first = false;
        }
    }

    public void onMapSearch(View view){
        EditText sv = (EditText) findViewById(R.id.editText);
        String location = sv.getText().toString();
        List<Address> addressList= null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            }catch (IOException e){
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            txtMain.setText(address.getFeatureName().toString());
            if (dest != null){
                dest.remove();
            }
            desti = new LatLng(address.getLatitude(), address.getLongitude());
            dest = mMap.addMarker(new MarkerOptions().position(desti).title("Destination"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desti, 14.0f));
        }

    }


}
