package com.example.place2be.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.place2be.R;
import com.example.place2be.models.GroupMarker;
import com.example.place2be.models.PersonalMarker;
import com.example.place2be.services.LocationTracker;
import com.example.place2be.services.LocationTracker.LocationReceivedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.Hashtable;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private Context mainContext;
    private GoogleMap mMap;
    private final Hashtable<String, GroupMarker> groupMarkerHashtable = new Hashtable<>();
    private final Hashtable<String, PersonalMarker> personalMarkerHashtable = new Hashtable<>();

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainContext = getActivity();

        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(this);

        Button addGroupButton = (Button) view.findViewById(R.id.add_group_button);

        addGroupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GroupDialog dialog = new GroupDialog(mainContext);
                dialog.setListener(new GroupDialog.GroupDialogListener() {
                    @Override
                    public void onDialogResult(boolean result) {
                        if (result) {
                            LocationTracker locationTracker = new LocationTracker(mainContext);
                            locationTracker.getLatLng(new LocationReceivedCallback() {
                                @Override
                                public void onLocationReceived(double latitude, double longitude) {
                                    addGroupMarker(latitude, longitude, latitude + ":" + longitude);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        // Return view
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // When map is loaded
        mMap = googleMap;

        // Sets onMarkerClickListener
        mMap.setOnMarkerClickListener(this);

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
        locationTracker.getLatLng(new LocationReceivedCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            }
        });

        addGroupMarker(52.04361437713596, 4.539530732068769, "g1");
        addGroupMarker(52.055279697855504, 4.490693069796051, "g2");
    }

    public void addGroupMarker(double latitude, double longitude, String key) {
        // Checks if key already exists
        if (groupMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key already in use!");
            return;
        }
        // Creates group marker
        groupMarkerHashtable.put(key, new GroupMarker(mainContext, mMap, key, latitude, longitude));
        groupMarkerHashtable.get(key).setMarker();
    }

    public void removeGroupMarker(String key) {
        // Checks if key exists
        if (!groupMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key not found");
            return;
        }
        // Removes group marker
        groupMarkerHashtable.get(key).removeMarker();
        groupMarkerHashtable.remove(key);
    }

    public void addPersonalMarker(double latitude, double longitude, String key) {
        // Checks if key already exists
        if (personalMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key already in use!");
            return;
        }
        // Creates personal marker
        personalMarkerHashtable.put(key, new PersonalMarker(mainContext, mMap, key, latitude, longitude));
        personalMarkerHashtable.get(key).setMarker();
    }

    public void removePersonalMarker(String key) {
        // Chekcs if key exists
        if (!personalMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key not found");
            return;
        }
        // Removes personal marker
        personalMarkerHashtable.get(key).removeMarker();
        personalMarkerHashtable.remove(key);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String key = marker.getTitle();
        Object type = marker.getTag();
        if (type == "GroupMarker") {
            groupMarkerHashtable.get(key).onMarkerClick();
            return true;
        }
        if (type == "PersonalMarker") {
            personalMarkerHashtable.get(key).onMarkerClick();
            return true;
        }
        return false;
    }
}
