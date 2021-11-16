package com.example.place2be.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.place2be.R;
import com.example.place2be.services.LocationTracker;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request location permission
        requestLocationPermission();
    }

    private void createMap() {
        // Initialize map fragment
        mapFragment = new MapFragment(MainActivity.this);

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mapFragment)
                .commit();

        // Adds group location marker to current location
        LocationTracker locationTracker = new LocationTracker(this);
        locationTracker.getLatLng(new LocationTracker.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                mapFragment.addGroupLocation(latitude, longitude, latitude + ":" + longitude);
            }
        });
    }

    private void requestLocationPermission() {
        // Checks location permission
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Requests location permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            createMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults[0] == 0) {
            createMap();
        } else {
            requestLocationPermission();
        }
    }
}
