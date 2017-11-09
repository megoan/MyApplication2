package com.example.shmuel.myapplication.controller.carmodels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shmuel.myapplication.R;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarCompaneisFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_models, container, false);
    }
}
