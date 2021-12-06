package com.example.place2be.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.place2be.R;
import com.example.place2be.ui.authentication.SignInActivity;
import com.example.place2be.ui.main.fragments.MapFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private MapFragment mapFragment;

    public MainActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request login status
        if (!requestLoginStatus()) {
            openLoginActivity();
        }

        // Request location permission
        requestLocationPermission();
    }

    private void setup() {
        // Initialize content view
        setContentView(R.layout.activity_main);

        // Setup navigation controller
        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);

        // Setup navigation button
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        findViewById(R.id.navigation_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private boolean requestLoginStatus() {
        // Opens shared preferences file "Login"
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        // Checks if access already true
        if (!sp.getBoolean("access", false)) {
            ed.putBoolean("access", false);
            ed.putString("username", null);
            ed.apply();
            return false;
        }
        return true;
    }

    private void openLoginActivity() {
        // Opens login activity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void requestLocationPermission() {
        // Checks location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Requests location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            setup();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults[0] == 0) {
            setup();
        } else {
            requestLocationPermission();
        }
    }
}
