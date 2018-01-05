package com.example.shmuel.myapplication.controller.cars;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.controller.branches.BranchEditActivity;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.backend.Updates;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.Car;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarActivity extends AppCompatActivity {

    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
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
    private int position;
    int branchid;
    int carModelid;
    private ProgressDialog progDailog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        MyActionModeCallbackClient callback=new MyActionModeCallbackClient();
        actionMode=startActionMode(callback);
        progressBar=findViewById(R.id.downloadProgressBar);
        Intent intent =getIntent();
        imgUrl=intent.getStringExtra("imgUrl");
        ImageView imageView1=(ImageView)findViewById(R.id.mainImage);
        Glide.with(this)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.default_car_image)
                .placeholder(R.drawable.default_car_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView1);
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

        position=intent.getIntExtra("position",0);
        branchid=intent.getIntExtra("branchID",0);
        carModelid=intent.getIntExtra("carmodelID",0);


        TextView branch1=(TextView)findViewById(R.id.branch_car);
        TextView carModel1 =(TextView)findViewById(R.id.carModel);
        TextView carNum1 =(TextView)findViewById(R.id.id_car);
        TextView mileage1 =(TextView)findViewById(R.id.numOfSpots);
        TextView year1 =(TextView)findViewById(R.id.numOfCars);
        TextView numOfRatings1 =(TextView)findViewById(R.id.numberOfRatings);
        TextView oneDayCost1 =(TextView)findViewById(R.id.established);
        TextView oneKilometerCost1 =(TextView)findViewById(R.id.mileage_cost_car);
        TextView inUse1=(TextView)findViewById(R.id.inUse_car);

        RatingBar ratingBar1=(RatingBar)findViewById(R.id.rating_car);



        branch1.setText(branchName);
        carModel1.setText(carModel);
        carNum1.setText("#"+String.valueOf(carNum));
        mileage1.setText(String.valueOf(mileage));
        year1.setText(String.valueOf(year));
        numOfRatings1.setText(String.valueOf(numOfRatings));
        oneDayCost1.setText(String.valueOf(oneDayCost));
        oneKilometerCost1.setText(String.valueOf(oneKilometerCost));
        inUse1.setText(String.valueOf(inUse));
        ratingBar1.setRating((float) rating);

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
                    if(inUse==true)
                    {
                        Toast.makeText(CarActivity.this,
                                "cannot delete car! car in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);

                    builder.setTitle("Delete Car");

                    builder.setMessage("are you sure?");

                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            new BackGroundDeleteCar().execute();
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
                    if(inUse==true)
                    {
                        Toast.makeText(CarActivity.this,
                                "cannot update car! car in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Intent intent=new Intent(CarActivity.this,CarEditActivity.class);
                    intent.putExtra("update","true");
                    intent.putExtra("branchName",branchName);
                    intent.putExtra("branchID",branchid);
                    intent.putExtra("carmodel",carModelid);
                    intent.putExtra("mileage",mileage);
                    intent.putExtra("carID",carNum);
                    intent.putExtra("oneDayCost",oneDayCost);
                    intent.putExtra("oneKilometerCost",oneKilometerCost);
                    intent.putExtra("year",year);
                    intent.putExtra("position",position);
                    intent.putExtra("imgUrl",imgUrl);
                    intent.putExtra("inUse",inUse);
                    intent.putExtra("rating",rating);
                    intent.putExtra("numOfRatings",numOfRatings);
                    finish();
                    startActivity(intent);
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
    public class BackGroundDeleteCar extends AsyncTask<Void,Void,Updates> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Updates doInBackground(Void... voids) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("car"+"/"+carNum+".jpg");
            Updates updates=backEndFunc.deleteCar(carNum,branchid);
            switch (updates){
                case ERROR:{
                    return Updates.ERROR;
                }
                case CARMODEL_AND_BRANCH:
                {
                    MySqlDataSource.carModelList=backEndFunc.getAllCarModels();
                    break;
                }
            }

            imageRef.delete();
            MySqlDataSource.carList=backEndFunc.getAllCars();
            MySqlDataSource.branchList=backEndFunc.getAllBranches();
            //backEndFunc.removeCarFromBranch(carNum,branchid);
            return updates;
        }


        @Override
        protected void onPostExecute(Updates updates) {
            super.onPostExecute(updates);
            if(updates==Updates.ERROR)
            {
                inputWarningDialog("cannot delete car");
                CarsTabFragment.mAdapter.objects=MySqlDataSource.carList;
                CarsTabFragment.cars=MySqlDataSource.carList;
                CarsTabFragment.mAdapter.notifyDataSetChanged();
                return;
            }
            if(updates==Updates.CARMODEL_AND_BRANCH)
            {
                CarModelsFragment.mAdapter.objects=MySqlDataSource.carModelList;
                CarModelsFragment.carModels=MySqlDataSource.carModelList;
                CarModelsFragment.mAdapter.notifyDataSetChanged();

            }
            BranchesFragment.mAdapter.objects=MySqlDataSource.branchList;
            BranchesFragment.branches=MySqlDataSource.branchList;
            BranchesFragment.mAdapter.notifyDataSetChanged();
            //TabFragments.carsTab.updateView2(position);
            Toast.makeText(CarActivity.this,
                    "car deleted", Toast.LENGTH_SHORT).show();
            actionMode.finish();
            progDailog.dismiss();
        }
    }
    public void inputWarningDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
        builder.setTitle("Invalid input!").setIcon(R.drawable.ic_warning);
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
