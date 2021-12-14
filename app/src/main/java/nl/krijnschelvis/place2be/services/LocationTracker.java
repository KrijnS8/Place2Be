package nl.krijnschelvis.place2be.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationTracker extends AppCompatActivity {

    private final Context mainContext;

    public LocationTracker(Context mainContext) {
        this.mainContext = mainContext;
    }

    @SuppressLint("MissingPermission")
    public void getLatLng(LocationReceivedCallback locationReceivedCallback) {
        // Creates location request
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Updates latitude and longitude
        LocationServices.getFusedLocationProviderClient(mainContext)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(mainContext).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            locationReceivedCallback.onLocationReceived(latitude, longitude);
                        }
                    }
                }, Looper.getMainLooper());
    }

    public interface LocationReceivedCallback {
        void onLocationReceived(double latitude, double longitude);
    }
}
