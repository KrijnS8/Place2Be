package com.example.place2be;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize map fragment
        Fragment mapFragment = new MapFragment();

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mapFragment)
                .commit();
    }
}
