package com.example.shmuel.myapplication.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.shmuel.myapplication.model.datasource.ListDataSource;

/**
 * Created by shmuel on 28/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    ListDataSource listDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new loadInfo().execute();

    }
    class loadInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if (ListDataSource.carList==null) {
                listDataSource=new ListDataSource();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
}