package com.example.shmuel.myapplication.controller.branches;

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
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.controller.cars.CarActivity;
import com.example.shmuel.myapplication.controller.cars.CarEditActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.MyDate;

public class BranchActivity extends AppCompatActivity {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;

    private String address;
    private int parkingSpotsNum;
    private int avaibaleSpots;
    private int branchNum;
    private String imgUrl;
    private double branchRevenue;
    private String establishedDate;
    private boolean inUse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        BranchActivity.MyActionModeCallbackClient callback=new BranchActivity.MyActionModeCallbackClient();
        actionMode=startActionMode(callback);

        Intent intent =getIntent();
        branchNum=intent.getIntExtra("id",0);
        address=intent.getStringExtra("address");
        establishedDate=intent.getStringExtra("established");
        parkingSpotsNum=intent.getIntExtra("parkingSpotsNum",0);
        avaibaleSpots=intent.getIntExtra("available",0);
        inUse=intent.getBooleanExtra("inUse",false);
        imgUrl=intent.getStringExtra("imgUrl");
        branchRevenue=intent.getDoubleExtra("revenue",0);

        TextView branchIDText=(TextView)findViewById(R.id.branchid);
        TextView addressText =(TextView)findViewById(R.id.address);
        TextView numOfCarsText =(TextView)findViewById(R.id.numOfCars);
        TextView numOfSpotsText =(TextView)findViewById(R.id.numOfSpots);
        TextView revenueText =(TextView)findViewById(R.id.revenue);
        ImageView imageView=(ImageView)findViewById(R.id.mainImage);
        TextView inUseText=(TextView)findViewById(R.id.inUse_branch);
        TextView establish=(TextView)findViewById(R.id.established);

        branchIDText.setText(String.valueOf("#"+branchNum));
        addressText.setText(address);
        numOfCarsText.setText(String.valueOf(parkingSpotsNum));
        numOfSpotsText.setText(String.valueOf(String.valueOf(avaibaleSpots)));
        revenueText.setText(String.valueOf(branchRevenue));
        inUseText.setText(String.valueOf(inUse));
        establish.setText(establishedDate);
        int defaultImage = getResources().getIdentifier(imgUrl,null,getApplicationContext().getPackageName());
        Drawable drawable= ContextCompat.getDrawable(this, defaultImage);
        imageView.setImageDrawable(drawable);

        actionMode.setTitle(address);
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
                            backEndFunc.deleteBranch(branchNum);

                            //TabFragments.tab3.updateView2(position);
                            Toast.makeText(BranchActivity.this,
                                    "branch deleted", Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("address",address);
                    intent.putExtra("branchID",branchNum);
                    intent.putExtra("imgUrl",imgUrl);
                    intent.putExtra("inUse",inUse);
                    intent.putExtra("established",establishedDate);
                    intent.putExtra("parkingSpotsNum",parkingSpotsNum);
                    intent.putExtra("available",avaibaleSpots);
                    intent.putExtra("revenue",branchRevenue);
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
}
