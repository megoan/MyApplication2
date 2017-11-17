package com.example.shmuel.myapplication.controller.cars;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.Clients.ClientActivity;
import com.example.shmuel.myapplication.controller.Clients.ClientEditActivity;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;

public class CarActivity extends AppCompatActivity {

    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;
    private String branchName;
    private String carModel;
    private double mileage;
    private int carNum;
    private double rating;
    private int numOfRatings;
    private double oneDayCost;
    private double oneKilometerCost;
    private int year;
    private boolean inUse;
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        MyActionModeCallbackClient callback=new MyActionModeCallbackClient();
        actionMode=startActionMode(callback);

        Intent intent =getIntent();
        branchName=intent.getStringExtra("branch");
        carModel=intent.getStringExtra("carModel");
        mileage=intent.getDoubleExtra("mileage",0);
        carNum=intent.getIntExtra("id",0);
        rating=intent.getDoubleExtra("rating",0);
        numOfRatings=intent.getIntExtra("numberOfRatings",0);
        oneDayCost=intent.getDoubleExtra("oneDayCost",0);
        oneKilometerCost=intent.getDoubleExtra("oneMileCost",0);
        year=intent.getIntExtra("year",0);
        inUse=intent.getBooleanExtra("inUse",false);
        imgUrl=intent.getStringExtra("img");


        TextView branch1=(TextView)findViewById(R.id.branch_car);
        TextView carModel1 =(TextView)findViewById(R.id.carModel);
        TextView carNum1 =(TextView)findViewById(R.id.id_car);
        TextView mileage1 =(TextView)findViewById(R.id.mileage_cost_car);
        TextView year1 =(TextView)findViewById(R.id.year);
        TextView numOfRatings1 =(TextView)findViewById(R.id.numberOfRatings);
        TextView oneDayCost1 =(TextView)findViewById(R.id.day_cost);
        TextView oneKilometerCost1 =(TextView)findViewById(R.id.single_mile_cost);
        TextView inUse1=(TextView)findViewById(R.id.inUse_car);
        ImageView imageView1=(ImageView)findViewById(R.id.mainImage);
        RatingBar ratingBar1=(RatingBar)findViewById(R.id.rating_car);

        branch1.setText(branchName);
        carModel1.setText(carModel);
        carNum1.setText(String.valueOf(carNum));
        mileage1.setText(String.valueOf(mileage));
        year1.setText(String.valueOf(year));
        numOfRatings1.setText(String.valueOf(numOfRatings));
        oneDayCost1.setText(String.valueOf(oneDayCost));
        oneKilometerCost1.setText(String.valueOf(oneKilometerCost));
        inUse1.setText(String.valueOf(inUse));
        ratingBar1.setRating((float) rating);
        int defaultImage = getResources().getIdentifier(imgUrl,null,getApplicationContext().getPackageName());
        Drawable drawable= ContextCompat.getDrawable(this, defaultImage);
        imageView1.setBackgroundDrawable(drawable);

        actionMode.setTitle(carModel);
    }

    public class MyActionModeCallbackClient implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.preview,menu);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);

                    builder.setTitle("Delete Car");

                    builder.setMessage("are you sure?");

                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            if (inUse==false) {
                                backEndFunc.deleteCar(carNum);
                                TabFragments.tab1.updateView2();
                                Toast.makeText(CarActivity.this,
                                        "car deleted", Toast.LENGTH_SHORT).show();
                                actionMode.finish();
                            }
                            else {
                                Toast.makeText(CarActivity.this,
                                        "cannot delete car! car in use!!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                case R.id.edit_item:
                {
                   /* Intent intent=new Intent(ClientActivity.this,ClientEditActivity.class);
                    intent.putExtra("update","true");
                    intent.putExtra("name",name);
                    intent.putExtra("lastName",lastName);
                    intent.putExtra("my_id",id);
                    intent.putExtra("phone",phone);
                    intent.putExtra("email",email);
                    intent.putExtra("credit",creditCardNum);
                    finish();
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
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
}
