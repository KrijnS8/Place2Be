package com.example.place2be.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.place2be.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class GroupLocation {

    private final Context mainContext;
    private final GoogleMap mMap;
    private final double latitude;
    private final double longitude;
    private int population = 1;

    public GroupLocation(Context context, GoogleMap googleMap, double latitude, double longitude) {
        this.mainContext = context;
        this.mMap = googleMap;
        this.latitude = latitude;
        this.longitude = longitude;

        addMarker();
    }

    private void addMarker() {
        IconGenerator iconGen = new IconGenerator(mainContext);
        iconGen.setBackground(mainContext.getResources().getDrawable(R.drawable.marker_background));
        iconGen.setTextAppearance(R.style.MarkerStyle);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(Integer.toString(population))))
                .position(new LatLng(latitude, longitude)).anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

        mMap.addMarker(markerOptions);
    }
}
