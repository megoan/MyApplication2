package com.example.shmuel.myapplication.controller.dialogs.sort;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.shmuel.myapplication.R;

/**
 * Created by shmuel on 26/10/2017.
 */

public class SortClientDialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sort_clients_layout, container,
                false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Button name=rootView.findViewById(R.id.namesort);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getDialog().dismiss();
            }
        });
        // Do something else
        return rootView;
    }
}
