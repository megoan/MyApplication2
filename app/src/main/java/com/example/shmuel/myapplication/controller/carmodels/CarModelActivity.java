package com.example.shmuel.myapplication.controller.carmodels;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarModelActivity extends AppCompatActivity {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    BackEndFunc backEndForSql=FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    public ActionMode actionMode;

    private int position;
    private ProgressDialog progDailog;
    private String companyName;
    private String modelName;
    private int id;
    private double engine;
    private Transmission transmission;
    private int passengers;
    private int luggage;
    private boolean ac;
    private boolean inUse;
    ImageView imageView1;
    ProgressBar progressBar;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_model);
        MyActionModeCallbackCarModel callback=new MyActionModeCallbackCarModel();
        actionMode=startActionMode(callback);

        Intent intent =getIntent();
        id=intent.getIntExtra("id",0);
        imgUrl=intent.getStringExtra("imgUrl");
        imageView1=(ImageView)findViewById(R.id.Car_imageView);
        progressBar=findViewById(R.id.downloadProgressBar);

        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
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
        //new DownloadImage().execute();

        companyName=intent.getStringExtra("companyName");
        modelName=intent.getStringExtra("modelName");

        engine=intent.getDoubleExtra("engine",0);
        transmission=(Transmission)intent.getSerializableExtra("transmission");
        passengers=intent.getIntExtra("passengers",0);
        luggage=intent.getIntExtra("luggage",0);
        ac=intent.getBooleanExtra("ac",false);

        inUse=intent.getBooleanExtra("inUse",false);
        position=intent.getIntExtra("position",0);




        TextView companyNameText=findViewById(R.id.CompanyName_display_textView);
        TextView modelNameText=findViewById(R.id.ModelName_display_textView);
        TextView modelCodeText=findViewById(R.id.ModelCode_display_textView);
        TextView engineText=findViewById(R.id.EngineDisplacement_display_textView);
        TextView transmissionText=findViewById(R.id.Transmission_display_textView);
        TextView passengersText=findViewById(R.id.Passengers_display_textView);
        TextView luggageText=findViewById(R.id.Luggage_display_textView);
        TextView acBox=findViewById(R.id.AC_display_textView);
        TextView inUseText=findViewById(R.id.InUse_display_textView);






        companyNameText.setText(companyName);
        modelNameText.setText(modelName);
        modelCodeText.setText(String.valueOf(id));
        engineText.setText(String.valueOf( engine));
        transmissionText.setText(String.valueOf(transmission));
        passengersText.setText(String.valueOf(passengers));
        luggageText.setText(String.valueOf(luggage));
        acBox.setText(String.valueOf(ac));
        inUseText.setText(String.valueOf(inUse));
        actionMode.setTitle(companyName+ " "+modelName);

    }
    public class MyActionModeCallbackCarModel implements ActionMode.Callback{

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
                        Toast.makeText(CarModelActivity.this,
                                "cannot delete car model! car model in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarModelActivity.this);

                    builder.setTitle("Delete Car model");

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
                        Toast.makeText(CarModelActivity.this,
                                "cannot update car! car in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Intent intent=new Intent(CarModelActivity.this,CarModelEditActivity.class);
                    intent.putExtra("update","true");
                    intent.putExtra("companyName",companyName);
                    intent.putExtra("modelName",modelName);
                    intent.putExtra("id",id);
                    intent.putExtra("engine",engine);
                    intent.putExtra("transmission",transmission);
                    intent.putExtra("passengers",passengers);
                    intent.putExtra("luggage",luggage);
                    intent.putExtra("ac",ac);
                    intent.putExtra("imgUrl",imgUrl);

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
    public class BackGroundDeleteCar extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarModelActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("carModel"+"/"+id+".jpg");
            backEndForSql.deleteCarModel(id);
            imageRef.delete();
            MySqlDataSource.carModelList=backEndForSql.getAllCarModels();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TabFragments.carModelsTab.updateView2(position);
            Toast.makeText(CarModelActivity.this,
                    "car model deleted", Toast.LENGTH_SHORT).show();
            actionMode.finish();
            progDailog.dismiss();
        }
    }

}
