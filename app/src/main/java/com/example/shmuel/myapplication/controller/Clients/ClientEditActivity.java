package com.example.shmuel.myapplication.controller.Clients;

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
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientEditActivity extends AppCompatActivity {
    Client client=new Client();
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;
    boolean update=false;
    TextView nameclient;
    TextView lastnameclient;
    TextView idclient;
    TextView phoneclient;
    TextView emailclient;
    TextView credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit);
        MyActionModeCallbackClient callback=new MyActionModeCallbackClient();
        actionMode=startActionMode(callback);
        nameclient =(TextView)findViewById(R.id.name);
        lastnameclient =(TextView)findViewById(R.id.last_name);
        idclient =(TextView)findViewById(R.id.id);
        phoneclient =(TextView)findViewById(R.id.phone);
        emailclient =(TextView)findViewById(R.id.email);
        credit=(TextView)findViewById(R.id.credit);
        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            update=true;
            actionMode.setTitle("Update client");
            nameclient.setText(intent.getStringExtra("name"));
            lastnameclient.setText(intent.getStringExtra("lastName"));
            idclient.setText("#"+String.valueOf(intent.getIntExtra("my_id",0)));
            phoneclient.setText(intent.getStringExtra("phone"));
            emailclient.setText(intent.getStringExtra("email"));
            credit.setText(intent.getStringExtra("credit"));
        }
        else {
            actionMode.setTitle("Add new client");
        }
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
                case R.id.check_item:{
                    // TODO check that all sadot are filled
                    String id=idclient.getText().toString();
                    String name=nameclient.getText().toString();
                    String lastName=lastnameclient.getText().toString();
                    String phone=phoneclient.getText().toString();
                    String email=emailclient.getText().toString();
                    String credit1=credit.getText().toString();
                    if(id.equals("")|| name.equals("")||lastName.equals("")|| phone.equals("")||email.equals("")||credit1.equals(""))
                    {
                        inputWarningDialog("Please fill all fields!");
                        return false;
                    }
                    //bad id
                    if(id.length()!=9)
                    {
                        inputWarningDialog("ID must be 9 digits!");
                        return false;
                    }
                    //bad email address
                    if(!emailValidator(email))
                    {
                        inputWarningDialog("Invalid email address!");
                        return false;
                    }
                    client.setId(Integer.valueOf(id));
                    client.setName(name);
                    client.setLastName(lastName);
                    client.setPhoneNum(phone);
                    client.setEmailAddress(email);
                    client.setCreditCardNum(credit1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientEditActivity.this);
                    if(update){
                    builder.setTitle("Update Client");}
                    else{
                        builder.setTitle("Add Client");
                    }

                    builder.setMessage("are you sure?");

                    if(update) {
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO check that all sadot are filled

                                try {
                                    backEndFunc.updateClient(client);
                                    Toast.makeText(ClientEditActivity.this,
                                            "client updated", Toast.LENGTH_SHORT).show();
                                    actionMode.finish();
                                } catch (Exception e) {
                                    inputWarningDialog(e.getMessage());
                                    return;
                                }
                            }
                        });
                    }
                    else {
                        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                try {
                                    backEndFunc.addClient(client);
                                    Toast.makeText(ClientEditActivity.this,
                                            "new client added", Toast.LENGTH_SHORT).show();
                                    actionMode.finish();
                                } catch (Exception e) {
                                    inputWarningDialog(e.getMessage());
                                    return;
                                }


                            }
                        });
                    }
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }

            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            finish();
        }

    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void inputWarningDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientEditActivity.this);
        builder.setTitle("Invalid input!");
        //builder.setIcon(getResources().getDrawable(android.R.drawable.stat_notify_error));
        //builder.setIcon(android.R.drawable.stat_notify_error);
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
