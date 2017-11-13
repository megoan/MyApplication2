package com.example.shmuel.myapplication.controller.Clients;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;

public class ClientActivity extends AppCompatActivity {

    public ActionMode actionMode;
    int id;
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        MyActionModeCallbackClient callback=new MyActionModeCallbackClient();
        actionMode=startActionMode(callback);

        Intent intent =getIntent();
        String name=intent.getStringExtra("name");
        String lastName=intent.getStringExtra("lastName");
        String phone=intent.getStringExtra("phone");
        String email=intent.getStringExtra("email");
        id=intent.getIntExtra("id",-1);
        String creditCardNum=intent.getStringExtra("credit");

        TextView nameclient =(TextView)findViewById(R.id.nameclient);
        TextView lastnameclient =(TextView)findViewById(R.id.last_name_client);
        TextView idclient =(TextView)findViewById(R.id.id_client);
        TextView phoneclient =(TextView)findViewById(R.id.phoneClient);
        TextView emailclient =(TextView)findViewById(R.id.emailClient);
        TextView credit=(TextView)findViewById(R.id.creditClient);

        nameclient.setText(name);
        lastnameclient.setText(lastName);
        idclient.setText("#"+String.valueOf(id));
        phoneclient.setText(phone);
        emailclient.setText(email);
        credit.setText(creditCardNum);
        actionMode.setTitle(name+" "+lastName);
    }
    public class MyActionModeCallbackClient implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.edit,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.delete_item:{

                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);

                    builder.setTitle("Delete Client");

                    builder.setMessage("are you sure?");

                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                           backEndFunc.deleteClient(id);

                            Toast.makeText(ClientActivity.this,
                                    "client deleted", Toast.LENGTH_SHORT).show();
                            actionMode.finish();
                        }
                    });

                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            finish();
        }

    }
}
