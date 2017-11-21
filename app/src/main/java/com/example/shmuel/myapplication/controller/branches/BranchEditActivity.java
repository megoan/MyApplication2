package com.example.shmuel.myapplication.controller.branches;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.cars.CarEditActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.MyDate;

public class BranchEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;
    Branch branch=new Branch();
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;

    boolean update=false;
    private int branchID;
    private String imgUrl;
    private boolean inUse;
    private String address;
    private int parkingSpotsNum;
    private int avaibaleSpots;
    private int branchNum;
    private double branchRevenue;
    private String establishedDate;
    private MyDate myDate;



    EditText parkingSpotsNumText;
    EditText avaibaleSpotsText;
    TextView addressText;
    EditText branchRevenueText;
    TextView establishedDateText;
    ImageView imageView;
    EditText branchIDText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_edit);

        parkingSpotsNumText=(EditText)findViewById(R.id.numOfCars);
        avaibaleSpotsText=(EditText)findViewById(R.id.numOfSpots);
        addressText=(TextView)findViewById(R.id.address);
        branchRevenueText=(EditText)findViewById(R.id.revenue);
        establishedDateText=(TextView)findViewById(R.id.established);
        imageView=(ImageView)findViewById(R.id.mainImage);
        branchIDText=(EditText)findViewById(R.id.branch_id);

        MyActionModeCallbackCar callback=new MyActionModeCallbackCar();
        actionMode=startActionMode(callback);

        datePickerDialog = new DatePickerDialog(
                BranchEditActivity.this, BranchEditActivity.this, 1990, 12, 25);

        ((FloatingActionButton) findViewById(R.id.dateButton))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });


        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            update=true;
            branchIDText.setEnabled(false);
            actionMode.setTitle("Update branch");

            branchIDText.setText("#"+String.valueOf(intent.getIntExtra("branchID",0)));
            parkingSpotsNumText.setText(String.valueOf(intent.getDoubleExtra("parkingSpotsNum",0)));
            avaibaleSpotsText.setText(String.valueOf(intent.getDoubleExtra("available",0)));
            addressText.setText(intent.getStringExtra("address"));
            branchRevenueText.setText(String.valueOf(intent.getDoubleExtra("revenue",0)));
            establishedDateText.setText(intent.getStringExtra("established"));
            imgUrl=intent.getStringExtra("imgUrl");
            int defaultImage = BranchEditActivity.this.getResources().getIdentifier(imgUrl,null,BranchEditActivity.this.getPackageName());
            Drawable drawable= ContextCompat.getDrawable(BranchEditActivity.this, defaultImage);
            imageView.setImageDrawable(drawable);


        }
        else {
            actionMode.setTitle("Add new branch");
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String mon=null;
        switch (month)
        {
            case 1:{mon="January";break;}
            case 2:{mon="February";break;}
            case 3:{mon="March";break;}
            case 4:{mon="April";break;}
            case 5:{mon="May";break;}
            case 6:{mon="June";break;}
            case 7:{mon="July";break;}
            case 8:{mon="August";break;}
            case 9:{mon="September";break;}
            case 10:{mon="October";break;}
            case 11:{mon="November";break;}
            case 12:{mon="December";break;}
        }
        myDate=new MyDate(year,mon,dayOfMonth);
    }


    public class MyActionModeCallbackCar implements android.view.ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.edit,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.check_item:{
                    // TODO check that all sadot are filled
                    String id1=branchIDText.getText().toString();

                    double branchRevenueString=tryParseDouble(branchRevenueText.getText().toString());
                    int parkingSpotsNumInt=tryParseInt(parkingSpotsNumText.getText().toString());
                    int avaibaleSpotsInt=tryParseInt(avaibaleSpotsText.getText().toString());
                    String addressString=addressText.getText().toString();
                    String establishedString= establishedDateText.getText().toString();



                    if(id1.equals("")|| branchRevenueString==0||parkingSpotsNumInt==0|| avaibaleSpotsInt==0|| addressString==null || establishedString==null ||myDate==null)
                    {
                        inputWarningDialog("Please fill all fields!");
                        return false;
                    }
                    //bad email address

                    if (update) {
                        branch.setBranchNum(Integer.valueOf(id1.substring(1)));
                    }
                    else
                    {
                        branch.setBranchNum(Integer.valueOf(id1));
                    }
                    branch.setInUse(inUse);
                    //TODO fix address
                    branch.setAddress(new Address());
                    branch.setBranchRevenue(branchRevenueString);
                    //TODO fix myDate
                    branch.setEstablishedDate(new MyDate());
                    branch.setParkingSpotsNum(parkingSpotsNumInt);


                    if (imgUrl!=null) {
                        branch.setImgURL(imgUrl);
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchEditActivity.this);
                    if(update){
                        builder.setTitle("Update car");}
                    else{
                        builder.setTitle("Add car");
                    }

                    builder.setMessage("are you sure?");

                    if(update) {
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO check that all sadot are filled

                                try {
                                    backEndFunc.updateBranch(branch);

                                    Toast.makeText(BranchEditActivity.this,
                                            "car updated", Toast.LENGTH_SHORT).show();
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
                                    backEndFunc.addBranch(branch);

                                    BranchesFragment.mAdapter.notifyDataSetChanged();

                                    Toast.makeText(BranchEditActivity.this,
                                            "new car added", Toast.LENGTH_SHORT).show();
                                    //actionMode.finish();
                                    resetView();
                                    branch=new Branch();
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
        public void onDestroyActionMode(android.view.ActionMode actionMode) {
            finish();
        }

    }
    public void inputWarningDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(BranchEditActivity.this);
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

    public void resetView()
    {
        parkingSpotsNumText.setText("");
        avaibaleSpotsText.setText("");
        addressText.setText("");
        branchRevenueText.setText("");
        establishedDateText.setText("");
        branchIDText.setText("");

    }
    int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
    }
    double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch(NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
    }
}
