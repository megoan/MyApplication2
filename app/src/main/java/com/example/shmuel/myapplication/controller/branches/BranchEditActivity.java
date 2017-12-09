package com.example.shmuel.myapplication.controller.branches;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.controller.cars.CarEditActivity;
import com.example.shmuel.myapplication.model.backend.BackEndForList;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.MyDate;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BranchEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;
    Branch branch=new Branch();
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    public ActionMode actionMode;

    boolean update=false;
    private int branchID;
    private int parkingSpotsNum;
    private int numOfCars;
    private int avaibaleSpots;
    private String imgUrl;
    private double branchRevenue;
    ProgressDialog progDailog;
    private boolean inUse;
    private MyDate myDate=new MyDate();
    private Address address=new Address();
    private ArrayList<Integer>carList=new ArrayList<>();



    ScrollView scrollView;
    TextView numOfCarsText;

    EditText numOfSpotsText;
    TextView addressText;
    EditText branchRevenueText;
    TextView establishedDateText;
    ImageView imageView;
    EditText branchIDText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_edit);

        scrollView=findViewById(R.id.branch_scroll_view);
        numOfCarsText=(TextView) findViewById(R.id.numOfCars);

        numOfSpotsText=(EditText)findViewById(R.id.numOfSpots);
        addressText=(TextView)findViewById(R.id.address);
        branchRevenueText=(EditText)findViewById(R.id.revenue);
        establishedDateText=(TextView)findViewById(R.id.established);
        imageView=(ImageView)findViewById(R.id.mainImage);
        branchIDText=(EditText)findViewById(R.id.branch_id);

        MyActionModeCallbackCar callback=new MyActionModeCallbackCar();
        actionMode=startActionMode(callback);



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

            //id
            branchID=(intent.getIntExtra("branchID",0));
            branchIDText.setText("#"+String.valueOf(branchID));

            //number of parking spots
            parkingSpotsNum=intent.getIntExtra("parkingSpotsNum",0);


            //number of available spots
            avaibaleSpots=intent.getIntExtra("available",0);
            numOfSpotsText.setText(String.valueOf(parkingSpotsNum));
            carList=intent.getIntegerArrayListExtra("carList");

            //branch address
            address.setAddressName(intent.getStringExtra("addressName"));
            address.setLatitude(intent.getDoubleExtra("latitude",0));
            address.setLongitude(intent.getDoubleExtra("longitude",0));
            address.setCountry(intent.getStringExtra("country"));
           // address.setStreet(intent.getStringExtra("street"));
           // address.setNumber(intent.getStringExtra("number"));
            addressText.setText(address.getAddressName());

            //branch revenue
            branchRevenue=intent.getDoubleExtra("revenue",0);
            branchRevenueText.setText(String.valueOf(branchRevenue));

            //branch established date
            myDate.setYear(intent.getIntExtra("year",0));
            myDate.setMonth(intent.getStringExtra("month"));
            myDate.setDay(intent.getIntExtra("day",0));
            establishedDateText.setText(intent.getStringExtra("established"));

            int mYear=myDate.getYear();
            int mMonth=myDate.getMonth(0);
            int mDay=myDate.getDay();
            datePickerDialog = new DatePickerDialog(
                    BranchEditActivity.this, BranchEditActivity.this, myDate.getYear(), myDate.getMonth(0), myDate.getDay());


            imgUrl=intent.getStringExtra("imgUrl");
            inUse=intent.getBooleanExtra("inUse",false);
            numOfCars=intent.getIntExtra("numOfCars",0);

            int defaultImage = BranchEditActivity.this.getResources().getIdentifier(imgUrl,null,BranchEditActivity.this.getPackageName());
            Drawable drawable= ContextCompat.getDrawable(BranchEditActivity.this, defaultImage);
            imageView.setImageDrawable(drawable);


        }
        else {
            actionMode.setTitle("Add new branch");
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(
                    BranchEditActivity.this, BranchEditActivity.this, mYear, mMonth, mDay);
        }
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            public static final String TAG = "HELLO";

            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng=place.getLatLng();
                double lan=latLng.latitude;
                double lon=latLng.longitude;
                address.setAddressName(place.getAddress().toString());
                address.setLatitude(lan);
                address.setLongitude(lon);
                address.setCountry(place.getLocale().getDisplayCountry());

                /*// TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                String a=place.getAddress().toString();
                String[] addresssArray = a.split(",");
                String[] streetArray=addresssArray[0].split(" ");
                if (streetArray.length == 3)
                {
                    address.setNumber(streetArray[2]);
                    address.setStreet(streetArray[0]+" "+streetArray[1]);
                    address.setCity(addresssArray[1]);
                }
                else {
                    address.setCity(addresssArray[1]);
                    address.setStreet(addresssArray[0]);
                    address.setNumber("");
                }
                addressText.setText(address.toString());*/
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myDate.setYear(year);
        myDate.setMonth(month);
        myDate.setDay(dayOfMonth);
        establishedDateText.setText(myDate.toString());
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


                    String addressString=addressText.getText().toString();
                    String establishedString= establishedDateText.getText().toString();
                    parkingSpotsNum= Integer.parseInt(numOfSpotsText.getText().toString());



                    if(id1.equals("")||parkingSpotsNum==0|| addressString==null || establishedString==null ||myDate==null)
                    {
                        inputWarningDialog("Please fill all fields!");
                        return false;
                    }
                    if(parkingSpotsNum<numOfCars)
                    {
                        inputWarningDialog("cant lower parking spots! too many cars!");
                    }

                    if (update) {
                        branch.setBranchNum(Integer.valueOf(id1.substring(1)));
                    }
                    else
                    {
                        branch.setBranchNum(Integer.valueOf(id1));
                    }
                    branch.setInUse(inUse);
                    //TODO fix address
                    //branch.setAddress(new Address());
                    branch.setBranchRevenue(branchRevenueString);
                    //TODO fix myDate
                    branch.setEstablishedDate(myDate);
                    branch.setParkingSpotsNum(parkingSpotsNum);

                    branch.setAddress(address);
                    branch.setCarIds(carList);




                    if (imgUrl!=null) {
                        branch.setImgURL(imgUrl);
                    }
                    else
                    {
                        branch.setImgURL("@drawable/rental");
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchEditActivity.this);
                    if(update){
                        builder.setTitle("Update branch");}
                    else{
                        builder.setTitle("Add branch");
                    }

                    builder.setMessage("are you sure?");

                    if(update) {
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO check that all sadot are filled

                                try {
                                    new BackGroundDeleteBranch().execute();
                                   /* backEndFunc.updateBranch(branch);
                                    Toast.makeText(BranchEditActivity.this,
                                            "branch updated", Toast.LENGTH_SHORT).show();*/


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

                                    new BackGroundDeleteBranch().execute();
                                   /* backEndFunc.addBranch(branch);
                                    BranchesFragment.mAdapter.notifyDataSetChanged();

                                    Toast.makeText(BranchEditActivity.this,
                                            "new bramch added", Toast.LENGTH_SHORT).show();
                                    //actionMode.finish();*/

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
        numOfCarsText.setText("0");
        numOfSpotsText.setText("");
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
    public class BackGroundDeleteBranch extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(BranchEditActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (update)
            {
                backEndFunc.updateBranch(branch);
            }
            else
            {
                backEndFunc.addBranch(branch);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(update)
            {

                Toast.makeText(BranchEditActivity.this,
                        "branch updated", Toast.LENGTH_SHORT).show();
                BranchesFragment.mAdapter.objects = backEndFunc.getAllBranches();
                BranchesFragment.mAdapter.notifyDataSetChanged();
                finish();
            }
            else
            {
                Toast.makeText(BranchEditActivity.this,"new bramch added", Toast.LENGTH_SHORT).show();
                BranchesFragment.mAdapter.objects= backEndFunc.getAllBranches();
                BranchesFragment.mAdapter.notifyDataSetChanged();
                resetView();
                branch=new Branch();
                progDailog.dismiss();
                scrollView.fullScroll(ScrollView.FOCUS_UP);

            }
            actionMode.finish();
        }
    }
}
