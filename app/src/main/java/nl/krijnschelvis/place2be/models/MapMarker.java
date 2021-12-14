package nl.krijnschelvis.place2be.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapMarker {
    protected final Context mainContext;
    protected final GoogleMap mMap;
    protected final String key;
    protected final double latitude;
    protected final double longitude;
    protected Marker marker;

    public MapMarker(Context context, GoogleMap googleMap, String key, double latitude, double longitude) {
        mainContext = context;
        mMap = googleMap;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setMarker() {}

    public void removeMarker() {
        marker.remove();
    }

    public void onMarkerClick() {}
}
