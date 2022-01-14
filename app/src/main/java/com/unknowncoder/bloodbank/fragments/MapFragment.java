package com.unknowncoder.bloodbank.fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unknowncoder.bloodbank.R;
public class MapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        initViewS();
        return view;
    }

    private void initViewS() {

    }

    @Override
    public void onStart() {
        super.onStart();
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS){

        }else {

        }
    }
}