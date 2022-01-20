package nl.krijnschelvis.place2be.ui.main.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import nl.krijnschelvis.place2be.R;
import nl.krijnschelvis.place2be.network.models.Gathering;
import nl.krijnschelvis.place2be.network.repositories.GatheringRepository;
import nl.krijnschelvis.place2be.ui.main.components.GroupMarker;
import nl.krijnschelvis.place2be.ui.main.components.PersonalMarker;
import nl.krijnschelvis.place2be.services.LocationTracker;
import nl.krijnschelvis.place2be.ui.main.dialogs.GroupDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;


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
        // Initialize context
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
                            createNewGatheringAtCurrentLocation(new GatheringCallback() {
                                @Override
                                public void onResult() {
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
        locationTracker.getLatLng(new LocationTracker.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            }
        });

        // Update gatherings
        updateGatherings();
    }

    private void updateGatherings() {
        // Build Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.26:8080/gathering/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create gathering repository
        GatheringRepository gatheringRepository = retrofit.create(GatheringRepository.class);

        // Execute GET request
        Call<List<Gathering>> call = gatheringRepository.getAllGatherings();
        call.enqueue(new Callback<List<Gathering>>() {
            @Override
            public void onResponse(Call<List<Gathering>> call, Response<List<Gathering>> response) {
                // Get response
                Iterable<Gathering> iterable = response.body();
                assert iterable != null;
                for (Gathering gathering: iterable) {
                    // Initialize values
                    double latitude = gathering.getLatitude();
                    double longitude = gathering.getLongitude();
                    String key = gathering.getId().toString();

                    // Add group marker
                    addGroupMarker(latitude, longitude, key);
                }
            }

            @Override
            public void onFailure(Call<List<Gathering>> call, Throwable t) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + t.toString());
            }
        });
    }

    private void createNewGatheringAtCurrentLocation(GatheringCallback gatheringCallback) {
        LocationTracker locationTracker = new LocationTracker(mainContext);
        locationTracker.getLatLng(new LocationTracker.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                String street;
                String postalCode;
                String city;
                String state;
                String country;

                try {
                    Geocoder geocoder = new Geocoder(mainContext, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    street = addresses.get(0).getThoroughfare();
                    postalCode = addresses.get(0).getPostalCode();
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                } catch (Exception e) {
                    return;
                }

                // Build Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.2.26:8080/gathering/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create gathering repository
                GatheringRepository gatheringRepository = retrofit.create(GatheringRepository.class);

                // Execute POST request
                Call<Gathering> call = gatheringRepository.addGathering(latitude, longitude, street, postalCode, city, state, country);
                call.enqueue(new Callback<Gathering>() {
                    @Override
                    public void onResponse(Call<Gathering> call, Response<Gathering> response) {
                        // Checks if gathering has been added to database
                        Gathering gathering = response.body();
                        assert gathering != null;
                        if (gathering.getId() == null) {
                            gatheringCallback.onResult();
                            return;
                        }

                        // Add group marker
                        addGroupMarker(gathering.getLatitude(), gathering.getLongitude(), gathering.getId().toString());
                        gatheringCallback.onResult();
                    }

                    @Override
                    public void onFailure(Call<Gathering> call, Throwable t) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + t.toString());
                        gatheringCallback.onResult();
                    }
                });
            }
        });
    }

    private void addGroupMarker(double latitude, double longitude, String key) {
        // Checks if key already exists
        if (groupMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key already in use!");
            return;
        }
        // Creates group marker
        groupMarkerHashtable.put(key, new GroupMarker(mainContext, mMap, key, latitude, longitude));
        groupMarkerHashtable.get(key).setMarker();
    }

    private void removeGroupMarker(String key) {
        // Checks if key exists
        if (!groupMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key not found");
            return;
        }
        // Removes group marker
        groupMarkerHashtable.get(key).removeMarker();
        groupMarkerHashtable.remove(key);
    }

    private void addPersonalMarker(double latitude, double longitude, String key) {
        // Checks if key already exists
        if (personalMarkerHashtable.containsKey(key)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Key already in use!");
            return;
        }
        // Creates personal marker
        personalMarkerHashtable.put(key, new PersonalMarker(mainContext, mMap, key, latitude, longitude));
        personalMarkerHashtable.get(key).setMarker();
    }

    private void removePersonalMarker(String key) {
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

interface GatheringCallback {
    void onResult();
}