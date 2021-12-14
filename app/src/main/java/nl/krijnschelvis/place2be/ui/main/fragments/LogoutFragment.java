package nl.krijnschelvis.place2be.ui.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.krijnschelvis.place2be.R;
import nl.krijnschelvis.place2be.ui.authentication.SignInActivity;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getContext();
        SharedPreferences sp = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putBoolean("access", false);
        ed.putString("username", null);
        ed.apply();

        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        // Return view
        return view;
    }
}
