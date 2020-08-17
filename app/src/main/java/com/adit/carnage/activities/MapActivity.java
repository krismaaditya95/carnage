package com.adit.carnage.activities;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adit.carnage.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleApiClient googleApiClient;
    private Location loc;
    private LocationManager locMan;
    private LocationProvider locProv;

    private GoogleMap gMap;

    public static void startActivity(BaseActivity source) {
        Intent i = new Intent(source, MapActivity.class);
        source.startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        MapView trackerMap = findViewById(R.id.trackerMap);
        Button btnBackFromTracker = findViewById(R.id.btnBackFromTracker);

        View.OnClickListener backToMain = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        btnBackFromTracker.setOnClickListener(backToMain);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng jogja = new LatLng(-34, 151);

        gMap.addMarker(new MarkerOptions()
                .position(jogja)
                .title("JOGJA"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(jogja));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
