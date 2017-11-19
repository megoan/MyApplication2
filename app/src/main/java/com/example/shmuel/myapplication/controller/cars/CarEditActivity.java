package com.example.shmuel.myapplication.controller.cars;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;

public class CarEditActivity extends AppCompatActivity implements RecyclerViewClickListener{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Car car=new Car();
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;
    boolean update=false;
    int carmodelID=0;
    Spinner yearSpinner;
    Spinner branchSpinner;

    EditText mileage;
    EditText idcar;
    EditText singleDayCost;
    EditText singleMileCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        mRecyclerView=findViewById(R.id.recycler);
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new CarModelListAdapet(backEndFunc.getAllCarModels(),CarEditActivity.this,this);

        mRecyclerView.setAdapter(mAdapter);

        yearSpinner =(Spinner) findViewById(R.id.yearSpinner);
        branchSpinner=(Spinner)findViewById(R.id.branchSpinner);
        idcar=(EditText)findViewById(R.id.id);
        mileage =(EditText)findViewById(R.id.mileage);
        singleDayCost=(EditText)findViewById(R.id.single_Day_Cost);
        singleMileCost =(EditText)findViewById(R.id.single_mile_cost);
        Intent intent =getIntent();
        carmodelID=intent.getIntExtra("carmodel",0);
        int index=backEndFunc.getAllCarModels().indexOf(backEndFunc.getCarModel(carmodelID));
        mRecyclerView.getLayoutManager().scrollToPosition(index);
        ((CarModelListAdapet)mAdapter).selectedPosition=index;
        Integer[] items = new Integer[60];
        for(int i=0;i<items.length;i++)
        {
            items[i]=i+1970;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        yearSpinner.setAdapter(adapter);


        String[] branches=new String[backEndFunc.getAllBranches().size()];
        int j=0;
        for(Branch branch:backEndFunc.getAllBranches())
        {
            branches[j]=branch.getAddress().getCity()+" "+branch.getAddress().getStreet()+" "+branch.getAddress().getNumber();
            j++;
        }
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, branches);
        branchSpinner.setAdapter(branchAdapter);

        MyActionModeCallbackCar callback=new MyActionModeCallbackCar();
        actionMode=startActionMode(callback);






        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            update=true;
            idcar.setEnabled(false);
            actionMode.setTitle("Update client");
            idcar.setText("#"+String.valueOf(intent.getIntExtra("carID",0)));
            singleDayCost.setText(String.valueOf(intent.getDoubleExtra("oneDayCost",0)));
            singleMileCost.setText(String.valueOf(intent.getDoubleExtra("oneKilometerCost",0)));
            mileage.setText(String.valueOf(intent.getDoubleExtra("mileage",0)));
            int spinnerPosition = adapter.getPosition(intent.getIntExtra("year",0));
            yearSpinner.setSelection(spinnerPosition);
            int branchPosition = branchAdapter.getPosition(intent.getStringExtra("branchName"));
            branchSpinner.setSelection( branchPosition);

        }
        else {
            actionMode.setTitle("Add new client");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        carmodelID=backEndFunc.getAllCarModels().get(position).getCarModelCode();
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
                    String id1=idcar.getText().toString();

                   /* yearSpinner =(Spinner) findViewById(R.id.yearSpinner);
                    branchSpinner=(Spinner)findViewById(R.id.branchSpinner);
                    mileage =(EditText)findViewById(R.id.mile_cost);
                    singleDayCost=(EditText)findViewById(R.id.single_Day_Cost);
                    singleMileCost =(EditText)findViewById(R.id.single_mile_cost);
                    */

                    double mileage1=Double.valueOf(mileage.getText().toString());
                    double  singleDayCost1=Double.valueOf(singleDayCost.getText().toString());
                    double singleMileCost1=Double.valueOf(singleMileCost.getText().toString());
                    int year=Integer.valueOf(yearSpinner.getSelectedItem().toString());
                    String brasnch1=branchSpinner.getSelectedItem().toString();
                    int branchID=0;
                    for(Branch branch:backEndFunc.getAllBranches()){
                        if(brasnch1.contains(branch.getAddress().getCity()+" "+branch.getAddress().getStreet()+" "+branch.getAddress().getNumber()))
                        {
                            branchID=branch.getBranchNum();
                        }
                    }

                    if(id1.equals("")|| mileage1==0||singleDayCost1==0|| singleMileCost1==0||year==0)
                    {
                        inputWarningDialog("Please fill all fields!");
                        return false;
                    }
                    //bad email address

                    if (update) {
                        car.setCarNum(Integer.valueOf(id1.substring(1)));
                    }
                    else
                    {
                        car.setCarNum(Integer.valueOf(id1.substring(1)));
                    }
                    car.setCarNum(Integer.valueOf(id1.substring(1)));
                    car.setBranchNum(branchID);
                    car.setMileage(mileage1);
                    car.setOneDayCost(singleDayCost1);
                    car.setOneKilometerCost(singleMileCost1);
                    car.setCarModel(carmodelID);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarEditActivity.this);
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
                                    backEndFunc.updateCar(car);
                                    Toast.makeText(CarEditActivity.this,
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
                                    backEndFunc.addCar(car);
                                    Toast.makeText(CarEditActivity.this,
                                            "new car added", Toast.LENGTH_SHORT).show();
                                    //actionMode.finish();
                                    resetView();
                                    car=new Car();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CarEditActivity.this);
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
        mileage.setText("");
        singleMileCost.setText("");
        singleDayCost.setText("");
        idcar.setText("");
        branchSpinner.setSelection(0);
        yearSpinner.setSelection(0);
    }
}

