package nl.krijnschelvis.place2be.ui.main.components;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import nl.krijnschelvis.place2be.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;
import java.util.Locale;

public class GroupMarker extends MapMarker {

    private int population = 1;

    public GroupMarker(Context context, GoogleMap googleMap, String key, double latitude, double longitude) {
        super(context, googleMap, key, latitude, longitude);
    }

    @Override
    public void setMarker() {
        // Sets correct marker options and adds marker
        IconGenerator iconGen = new IconGenerator(mainContext);
        iconGen.setBackground(mainContext.getResources().getDrawable(R.drawable.group_marker_background));
        iconGen.setTextAppearance(R.style.GroupMarkerStyle);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon("G")))
                .position(new LatLng(latitude, longitude)).anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

        marker = mMap.addMarker(markerOptions);
        assert marker != null;
        marker.setTitle(key);
        marker.setTag("GroupMarker");
    }

    public void onMarkerClick() {
        // When marker gets clicked
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16f),200,null);
        Toast.makeText(mainContext, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
    }
}
