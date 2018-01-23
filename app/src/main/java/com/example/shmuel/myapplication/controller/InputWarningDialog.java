package com.example.shmuel.myapplication.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.shmuel.myapplication.R;

/**
 * Created by User on 07/01/2018.
 */

public class InputWarningDialog {
    public static void showWarningDialog(String title, String message, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(R.drawable.ic_warning);

        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
