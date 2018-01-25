package com.example.shmuel.myapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    String usernameclient;
    String passwordclient;
    MySqlDataSource sqlDataSource;
    FrameLayout frameLayout;
    private static String USERNAME = "1234";
    private static String PASSWORD = "1234";
    boolean userCheck = false;
    boolean downloadcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        new loadInfo().execute();
        username = findViewById(R.id.UsernameeditText);
        password = findViewById(R.id.PasswordeditText);
        login = findViewById(R.id.Loginbutton);
        frameLayout = findViewById(R.id.loadingAccount);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                usernameclient = username.getText().toString();
                passwordclient = password.getText().toString();
                if (usernameclient == null || usernameclient.compareTo("") == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwordclient == null || passwordclient.compareTo("") == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                int i = validateInfo(usernameclient, passwordclient);
                switch (i) {
                    case 0: {
                        //frameLayout.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Please enter the right password", Toast.LENGTH_LONG).show();
                        password.setText("");
                        userCheck = false;
                        return;
                    }
                    case -1: {
                        //frameLayout.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "The username is incorrect", Toast.LENGTH_LONG).show();
                        username.setText("");
                        password.setText("");
                        userCheck = false;
                        return;
                    }
                    case 1: {
                        userCheck = true;
                        if (downloadcheck == true) {
                            frameLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            //frameLayout.setVisibility(View.GONE);
                            finish();
                            return;
                        } else {
                            frameLayout.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }
        });
    }

    private int validateInfo(String usernameclient, String passwordclient) {
        if (!usernameclient.equals(USERNAME)) {
            return -1;
        } else if (!passwordclient.equals(PASSWORD)) {
            return 0;
        } else
            return 1;
    }

    class loadInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

           /* if (ListDataSource.carList==null) {
                listDataSource=new ListDataSource();
            }*/
            if (MySqlDataSource.carList == null) {
                sqlDataSource = new MySqlDataSource();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            downloadcheck = true;

            if (userCheck == true) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            super.onPostExecute(aVoid);


        }
    }
}
