package com.example.place2be.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.place2be.models.GroupLocation;
import com.example.place2be.services.LocationTracker;
import com.example.place2be.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Hashtable;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    private final Context mainContext;
    private GoogleMap mMap;
    private final Hashtable<String, GroupLocation> groupLocationHashtable = new Hashtable<String, GroupLocation>();

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
        locationTracker.updateLocation(new LocationTracker.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                addGroupLocation(latitude, longitude, Double.toString(latitude) + longitude);
            }
        });
    }

    private void addGroupLocation(double latitude, double longitude, String key) {
        // Checks if key already exists
        if (groupLocationHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key already in use!");
            return;
        }
        // Creates group location
        groupLocationHashtable.put(key, new GroupLocation(mainContext, mMap, key, latitude, longitude));
        groupLocationHashtable.get(key).setMarker();
    }

    private void removeGroupLocation(String key) {
        // Checks if key exists
        if (!groupLocationHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key not found");
            return;
        }
        // Removes group location
        groupLocationHashtable.get(key).removeMarker();
        groupLocationHashtable.remove(key);
    }
}