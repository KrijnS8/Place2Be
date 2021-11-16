package com.example.place2be;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    private final Context mainContext;
    private GoogleMap mMap;

    public MapFragment(Context mainContext) {
        this.mainContext = mainContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(this);

        // Return view
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // When map is loaded
        mMap = googleMap;

        // Sets style of map
        try {
            boolean isSucces = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mainContext, R.raw.style_map));
            if (!isSucces) {
                Toast.makeText(mainContext, "Maps style loads failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }

        // Moves camera to current location
        LocationTracker locationTracker = new LocationTracker(mainContext);
        locationTracker.updateLocation((latitude, longitude) -> mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude))));

        // Adds ClickListener event to add marker on click location
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                // Set position of marker
                markerOptions.position(latLng);
                // Set title of marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // Remove all markers
                mMap.clear();
                // Animate to zoom marker
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                // Add marker on map
                mMap.addMarker(markerOptions);
            }
        });
    }
}
