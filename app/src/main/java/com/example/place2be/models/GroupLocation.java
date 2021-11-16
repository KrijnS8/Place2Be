package com.example.place2be.models;

import android.content.Context;
import android.widget.Toast;

import com.example.place2be.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class GroupLocation implements GoogleMap.OnMarkerClickListener {

    private final Context mainContext;
    private final GoogleMap mMap;
    private final String key;
    private final double latitude;
    private final double longitude;
    private Marker marker;
    private int population = 1;

    public GroupLocation(Context context, GoogleMap googleMap, String key, double latitude, double longitude) {
        this.mainContext = context;
        this.mMap = googleMap;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        mMap.setOnMarkerClickListener(this);
    }

    public void setMarker() {
        // Sets correct marker options and adds marker
        IconGenerator iconGen = new IconGenerator(mainContext);
        iconGen.setBackground(mainContext.getResources().getDrawable(R.drawable.marker_background));
        iconGen.setTextAppearance(R.style.MarkerStyle);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(Integer.toString(population))))
                .position(new LatLng(latitude, longitude)).anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

        marker = mMap.addMarker(markerOptions);
    }

    public void removeMarker() {
        // Removes marker
        marker.remove();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // When marker gets clicked
        if (this.marker.equals(marker)) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16f),200,null);
            Toast.makeText(mainContext, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
