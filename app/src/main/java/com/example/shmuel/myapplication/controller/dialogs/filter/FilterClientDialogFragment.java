package com.example.shmuel.myapplication.controller.dialogs.filter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.shmuel.myapplication.R;

/**
 * Created by shmuel on 26/10/2017.
 */

public class FilterClientDialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_clients_layout, container,
                false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Do something else
        return rootView;
    }
}
