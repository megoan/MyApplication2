package com.example.shmuel.myapplication.controller.branches;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.BranchImage;
import com.example.shmuel.myapplication.model.entities.MyAddress;
import com.example.shmuel.myapplication.model.entities.MyDate;

import java.util.ArrayList;

public class BranchActivity extends AppCompatActivity {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    public ActionMode actionMode;
    BranchImage branchImage=new BranchImage();
    //private String myAddress=new MyAddress();
    private MyAddress myAddress =new MyAddress();
    private MyDate myDate=new MyDate();
    private int parkingSpotsNum;
    private int numOfCars;
    private int avaibaleSpots;
    private int branchNum;
    //private String imgUrl;
    private double branchRevenue;
    private String establishedDate;
    private boolean inUse;
    private ArrayList<Integer>carList=new ArrayList<>();
    private ProgressDialog progDailog;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        BranchActivity.MyActionModeCallbackClient callback=new BranchActivity.MyActionModeCallbackClient();
        actionMode=startActionMode(callback);

        Intent intent =getIntent();
        branchNum=intent.getIntExtra("id",0);
        branchImage.setBranchID(branchNum);
        imageView=(ImageView)findViewById(R.id.mainImage);
        new DownloadImage().execute();
        myAddress.setCountry(intent.getStringExtra("country"));
        myAddress.setAddressName(intent.getStringExtra("addressName"));
        myAddress.setLatitude(intent.getDoubleExtra("latitude",0));
        myAddress.setLongitude(intent.getDoubleExtra("longitude",0));
        //myAddress.setCity(intent.getStringExtra("city"));
        //myAddress.setStreet(intent.getStringExtra("street"));
        //myAddress.setNumber(intent.getStringExtra("number"));
        myDate.setYear(intent.getIntExtra("year",0));
        myDate.setMonth(intent.getStringExtra("month"));
        myDate.setDay(intent.getIntExtra("day",0));
        establishedDate=intent.getStringExtra("established");
        parkingSpotsNum=intent.getIntExtra("parkingSpotsNum",0);
        avaibaleSpots=intent.getIntExtra("available",0);
        inUse=intent.getBooleanExtra("inUse",false);
        //imgUrl=intent.getStringExtra("imgUrl");
        branchRevenue=intent.getDoubleExtra("revenue",0);
        numOfCars=intent.getIntExtra("numOfCars",0);
        carList=intent.getIntegerArrayListExtra("carList");

        TextView branchIDText=(TextView)findViewById(R.id.branchid);
        TextView addressText =(TextView)findViewById(R.id.address);
        TextView numOfCarsText =(TextView)findViewById(R.id.numOfCars);
        TextView numOfSpotsText =(TextView)findViewById(R.id.numOfSpots);
        TextView revenueText =(TextView)findViewById(R.id.revenue);

        TextView inUseText=(TextView)findViewById(R.id.inUse_branch);
        TextView establish=(TextView)findViewById(R.id.established);

        branchIDText.setText(String.valueOf("#"+branchNum));
        addressText.setText(myAddress.getAddressName());
        numOfCarsText.setText(String.valueOf(numOfCars));
        numOfSpotsText.setText(String.valueOf(String.valueOf(avaibaleSpots)));
        revenueText.setText(String.valueOf(branchRevenue));
        inUseText.setText(String.valueOf(inUse));
        establish.setText(establishedDate);




        actionMode.setTitle(myAddress.getAddressName());
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
                        Toast.makeText(BranchActivity.this,
                                "cannot delete branch! branch in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchActivity.this);

                    builder.setTitle("Delete Branch");

                    builder.setMessage("are you sure?");

                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                          //backEndFunc.deleteBranch(branchNum);
                           new BackGroundDeleteBranch().execute();
                            //TabFragments.branchesTab.updateView2(position);

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
                        Toast.makeText(BranchActivity.this,
                                "cannot update branch! branch in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Intent intent=new Intent(BranchActivity.this, BranchEditActivity.class);
                    intent.putExtra("update","true");
                    intent.putExtra("addressName", myAddress.getAddressName());
                    intent.putExtra("latitude", myAddress.getLatitude());
                    intent.putExtra("longitude", myAddress.getLongitude());
                    intent.putExtra("country", myAddress.getCountry());
                    // intent.putExtra("street",myAddress.getStreet());
                   // intent.putExtra("number",myAddress.getNumber());
                    intent.putExtra("branchID",branchNum);
                    //intent.putExtra("imgUrl",imgUrl);
                    intent.putExtra("inUse",inUse);
                    intent.putExtra("established",establishedDate);
                    intent.putExtra("parkingSpotsNum",parkingSpotsNum);
                    intent.putExtra("available",avaibaleSpots);
                    intent.putExtra("revenue",branchRevenue);
                    intent.putExtra("year",myDate.getYear());
                    intent.putExtra("month",myDate.getMonth());
                    intent.putExtra("day",myDate.getDay());
                    intent.putExtra("numOfCars",numOfCars);
                    intent.putExtra("carList",carList);
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
    public class BackGroundDeleteBranch extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(BranchActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            backEndFunc.deleteBranch(branchNum);
            MySqlDataSource.branchList=backEndFunc.getAllBranches();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(BranchActivity.this,
                    "branch deleted", Toast.LENGTH_SHORT).show();
            actionMode.finish();
            progDailog.dismiss();
        }
    }
    public class DownloadImage extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            branchImage=backEndFunc.getBranchImage(branchImage.getBranchID());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (branchImage.getImgURL()==null|| branchImage.getImgURL().equals("@drawable/rental")) {
                int defaultImage = BranchActivity.this.getResources().getIdentifier("@drawable/rental", null, BranchActivity.this.getPackageName());
                Drawable drawable = ContextCompat.getDrawable(BranchActivity.this, defaultImage);
                imageView.setImageDrawable(drawable);
            } else {
                byte[] imageBytes= Base64.decode(branchImage.getImgURL(),Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
