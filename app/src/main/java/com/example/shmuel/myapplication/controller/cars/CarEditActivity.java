package com.example.shmuel.myapplication.controller.cars;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.Utility;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelEditActivity;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.backend.Updates;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CarEditActivity extends AppCompatActivity implements RecyclerViewClickListener{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog progDailog;
    public ActionMode actionMode;
    Car car=new Car();
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    BackEndFunc backEndForSql= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);

    String ret = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CropIntent;
    boolean imageSelected=false;
    Bitmap mBitmap;

    boolean update=false;
    int carmodelID=0;
    Spinner yearSpinner;
    Spinner branchSpinner;
    String imgUrl;
    boolean inUse;
    double rating;
    int numOfRating;
    int originalCarModel;
    int originalBranchID;
    int branchID=0;
    EditText mileage;
    EditText idcar;
    EditText singleDayCost;
    EditText singleMileCost;
    TextView pickedCarModel;
    FloatingActionButton check;
    FloatingActionButton right;
    FloatingActionButton left;
    ImageView imageView;
    CarModel carModel2;
    ScrollView scrollView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        car.setImgURL("@drawable/default_car_image");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);
        mRecyclerView=findViewById(R.id.recycler);
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new CarModelListAdapet(backEndFunc.getAllCarModels(),CarEditActivity.this,this);
        mRecyclerView.setAdapter(mAdapter);

        scrollView=findViewById(R.id.car_scroll_view);
        yearSpinner =(Spinner) findViewById(R.id.yearSpinner);
        branchSpinner=(Spinner)findViewById(R.id.branchSpinner);
        idcar=(EditText)findViewById(R.id.id);
        mileage =(EditText)findViewById(R.id.mileage);
        singleDayCost=(EditText)findViewById(R.id.single_Day_Cost);
        singleMileCost =(EditText)findViewById(R.id.single_mile_cost);
        pickedCarModel=(TextView)findViewById(R.id.picked_car_model);
        right=(FloatingActionButton)findViewById(R.id.scrollRight);
        left=(FloatingActionButton)findViewById(R.id.scrollLeft);
        check=(FloatingActionButton)findViewById(R.id.carModelSelectedCheck);
        progressBar=(ProgressBar)findViewById(R.id.downloadProgressBar);
        check.hide();
        imageView=(ImageView)findViewById(R.id.mainImage);

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
            branches[j]=branch.getMyAddress().getAddressName();
            j++;
        }
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, branches);
        branchSpinner.setAdapter(branchAdapter);

        MyActionModeCallbackCar callback=new MyActionModeCallbackCar();
        actionMode=startActionMode(callback);
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            imgUrl=intent.getStringExtra("imgUrl");
            progressBar.setVisibility(View.VISIBLE);
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
                    .into(imageView);
            update=true;
            idcar.setEnabled(false);
            actionMode.setTitle("Update car");
            idcar.setText("#"+String.valueOf(intent.getIntExtra("carID",0)));
            singleDayCost.setText(String.valueOf(intent.getDoubleExtra("oneDayCost",0)));
            singleMileCost.setText(String.valueOf(intent.getDoubleExtra("oneKilometerCost",0)));
            mileage.setText(String.valueOf(intent.getDoubleExtra("mileage",0)));
            int spinnerPosition = adapter.getPosition(intent.getIntExtra("year",0));
            yearSpinner.setSelection(spinnerPosition);
            int branchPosition = branchAdapter.getPosition(intent.getStringExtra("branchName"));
            branchSpinner.setSelection( branchPosition);
            inUse=intent.getBooleanExtra("inUse",false);
            rating=intent.getDoubleExtra("rating",0);
            numOfRating=intent.getIntExtra("numOfRating",0);
            carmodelID=intent.getIntExtra("carmodel",0);
            originalCarModel=carmodelID;
            branchID=originalBranchID=intent.getIntExtra("branchID",0);
            CarModel carModel=backEndFunc.getCarModel(carmodelID);
            pickedCarModel.setText(carModel.getCompanyName()+" "+carModel.getCarModelName());
            int index=backEndFunc.getAllCarModels().indexOf(carModel);
            mRecyclerView.getLayoutManager().scrollToPosition(index);
            ((CarModelListAdapet)mAdapter).selectedPosition=index;
        }
        else {
            actionMode.setTitle("Add new car");
        }

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CarEditActivity.this,
                        "swipe view to see other models", Toast.LENGTH_SHORT).show();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CarEditActivity.this,
                        "swipe view to see other models", Toast.LENGTH_SHORT).show();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CarEditActivity.this,
                        carModel2.getCompanyName()+" "+carModel2.getCarModelName()+" was selected", Toast.LENGTH_SHORT).show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

       mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);
               switch (newState){
                   case RecyclerView.SCROLL_STATE_IDLE:{
                       right.show();
                       left.show();
                       break;
                   }
                   case RecyclerView.SCROLL_STATE_DRAGGING:
                   {
                       right.hide();
                       left.hide();
                   }
               }
           }

           @Override
           public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
           }
       });



    }

    @Override
    public void recyclerViewListClicked(View v, int position,boolean selected) {
        if (selected) {
            carModel2=backEndFunc.getAllCarModels().get(position);
            carmodelID=carModel2.getCarModelCode();
            pickedCarModel.setText(carModel2.getCompanyName()+" "+carModel2.getCarModelName());
            Toast.makeText(CarEditActivity.this,
                    carModel2.getCompanyName()+" "+carModel2.getCarModelName()+" was selected", Toast.LENGTH_SHORT).show();
            check.show();
        }
        else{
            check.hide();
            pickedCarModel.setText("");
            Toast.makeText(CarEditActivity.this,
                    "no car models are selected!", Toast.LENGTH_SHORT).show();
            carmodelID=0;
            carModel2=null;
        }

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

                    double mileage1=tryParseDouble(mileage.getText().toString());
                    double  singleDayCost1=tryParseDouble(singleDayCost.getText().toString());
                    double singleMileCost1=tryParseDouble(singleMileCost.getText().toString());
                    int year=tryParseInt(yearSpinner.getSelectedItem().toString());
                    String brasnch1=branchSpinner.getSelectedItem().toString();

                    for(Branch branch:backEndFunc.getAllBranches()){
                        if(brasnch1.contains(branch.getMyAddress().getAddressName()))
                        {
                            branchID=branch.getBranchNum();
                        }
                    }

                    if(id1.equals("")|| mileage1==0||singleDayCost1==0|| singleMileCost1==0||year==0|| carmodelID==0)
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
                        car.setCarNum(Integer.valueOf(id1));
                    }
                    car.setBranchNum(branchID);
                    car.setMileage(mileage1);
                    car.setOneDayCost(singleDayCost1);
                    car.setOneKilometerCost(singleMileCost1);
                    car.setCarModel(carmodelID);
                    car.setInUse(inUse);
                    car.setYear(year);
                    if (imgUrl!=null) {
                        car.setImgURL(imgUrl);
                    }
                    car.setRating(rating);
                    car.setNumOfRatings(numOfRating);

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
                                    if (imageSelected) {
                                        new  UpdateCarAndImageAsyncTask().execute();
                                    }
                                    else {
                                        new UpdateCarNoImageAsyncTask().execute();
                                    }

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
                                    new  AddNewCarWithoutImageUrlAsyncTask().execute();
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
        builder.setTitle("Invalid input!").setIcon(R.drawable.ic_warning);;
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
        mRecyclerView.getLayoutManager().scrollToPosition(-1);
        ((CarModelListAdapet)mAdapter).selectedPosition=-1;
        check.hide();

    }
    int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
    double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
  /*  public class BackGroundUpdateCar extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarEditActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (update)
            {
                if(originalCarModel!=carmodelID)
                {
                   // backEndFunc.updateCar(car,originalCarModel);
                }
                backEndFunc.updateCar(car);
            }
            else
            {
                //backEndFunc.addCar(car,car.getBranchNum());
                BranchesFragment.mAdapter.objects=backEndFunc.getAllBranches();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(update)
            {

                Toast.makeText(CarEditActivity.this,
                        "car updated", Toast.LENGTH_SHORT).show();
                BranchesFragment.mAdapter.objects= backEndFunc.getAllBranches();
                BranchesFragment.mAdapter.notifyDataSetChanged();
                CarsTabFragment.mAdapter.objects= backEndFunc.getAllCars();
                CarsTabFragment.mAdapter.notifyDataSetChanged();
                CarModelsFragment.mAdapter.objects=backEndFunc.getAllCarModels();
                CarModelsFragment.mAdapter.notifyDataSetChanged();
                 finish();
            }
            else
            {
                Toast.makeText(CarEditActivity.this,
                        "new car added", Toast.LENGTH_SHORT).show();
                BranchesFragment.mAdapter.objects= backEndFunc.getAllBranches();
                BranchesFragment.mAdapter.notifyDataSetChanged();
                CarsTabFragment.mAdapter.objects= backEndFunc.getAllCars();
                CarsTabFragment.mAdapter.notifyDataSetChanged();
                CarModelsFragment.mAdapter.objects=backEndFunc.getAllCarModels();
                CarModelsFragment.mAdapter.notifyDataSetChanged();
                resetView();
                car=new Car();
                progDailog.dismiss();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }
    }*/


    public class AddNewCarWithoutImageUrlAsyncTask extends AsyncTask<Void,Void,Updates>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarEditActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Updates doInBackground(Void... voids) {

            Updates updates= backEndForSql.addCar(car);
            return updates;
        }

        @Override
        protected void onPostExecute(Updates updates) {
            super.onPostExecute(updates);
            if(updates==Updates.ERROR)
            {
                inputWarningDialog("Unable to add car, Try again, use other id");
                progDailog.dismiss();
                return;
            }
            else if(updates==Updates.DUPLICATE)
            {
                inputWarningDialog("car with this id already exists!");
                progDailog.dismiss();
                return;
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("car"+"/"+car.getCarNum()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String url=downloadUrl.toString();
                    car.setImgURL(url);
                    new UpdateCarNoImageAsyncTask().execute();
                }
            });
        }
    }


    public class UpdateCarAndImageAsyncTask extends AsyncTask<Void,Void,Updates>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarEditActivity.this);
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
            imageRef = storageRef.child("car"+"/"+car.getCarNum()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    new UpdateCarNoImageAsyncTask().execute();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Updates updates) {
            super.onPostExecute(updates);
        }
    }



    public class UpdateCarNoImageAsyncTask extends AsyncTask<Void,Void,Updates>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (update) {
                progDailog = new ProgressDialog(CarEditActivity.this);
                progDailog.setMessage("Updating...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(false);
                progDailog.show();
            }
        }

        @Override
        protected Updates doInBackground(Void... voids) {
            Updates updates;
            if (update) {
                updates=backEndForSql.updateCar(car);
            }
            else {
                updates=backEndForSql.updateCar(car,originalCarModel,originalBranchID);
            }
            if(updates==Updates.CARMODEL)
            {
                MySqlDataSource.carModelList=backEndForSql.getAllCarModels();
            }
            else if(updates==Updates.CARMODEL_AND_BRANCH)
            {
                MySqlDataSource.carModelList=backEndForSql.getAllCarModels();
                MySqlDataSource.branchList=backEndForSql.getAllBranches();
            }
            MySqlDataSource.carList=backEndForSql.getAllCars();
            return updates;
        }

        @Override
        protected void onPostExecute(Updates updates) {
            super.onPostExecute(updates);
            if(updates==Updates.ERROR)
            {
                if (update) {
                    inputWarningDialog("unable To update car, try again");
                    return;
                }
                else {
                    inputWarningDialog("unable To Add car, try again, or change id");
                    return;
                }
            }
           if(updates==Updates.CARMODEL)
           {
               CarModelsFragment.mAdapter.objects= (ArrayList<CarModel>) MySqlDataSource.carModelList;
               CarModelsFragment.carModels=(ArrayList<CarModel>) MySqlDataSource.carModelList;
               CarModelsFragment.mAdapter.notifyDataSetChanged();
           }
           if(updates==Updates.CARMODEL_AND_BRANCH)
           {
               CarModelsFragment.mAdapter.objects= (ArrayList<CarModel>) MySqlDataSource.carModelList;
               CarModelsFragment.carModels=(ArrayList<CarModel>) MySqlDataSource.carModelList;
               CarModelsFragment.mAdapter.notifyDataSetChanged();

               BranchesFragment.branches=(ArrayList<Branch>) MySqlDataSource.branchList;
               BranchesFragment.mAdapter.objects= (ArrayList<Branch>) MySqlDataSource.branchList;
               BranchesFragment.mAdapter.notifyDataSetChanged();
           }
            CarsTabFragment.cars= MySqlDataSource.carList;
            CarsTabFragment.mAdapter.objects= (ArrayList<Car>) MySqlDataSource.carList;
            CarsTabFragment.mAdapter.notifyDataSetChanged();
            progDailog.dismiss();
            if (!update) {
                Toast.makeText(CarEditActivity.this,
                        "new car added", Toast.LENGTH_SHORT).show();
                resetView();
                car=new Car();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
            else {
                Toast.makeText(CarEditActivity.this,
                        "car updated", Toast.LENGTH_SHORT).show();
            }
            if (update) {
                finish();
            }
        }
    }




    /*
    * image functions
    * */

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CarEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(CarEditActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file=new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis())+".jpg");
        uri= Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                if(data!=null)
                {
                    uri=data.getData();
                    CropImage.activity(uri).setAspectRatio(2,1)
                            .start(this);
                    //CropImage();
                }
                //onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                CropImage.activity(uri).setAspectRatio(2,1)
                        .start(this);
                //CropImage();
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        imageView.setImageBitmap(mBitmap);
                        imageSelected=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                }
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        imageView.setImageBitmap(thumbnail);
    }

    private void CropImage() {
        try {
            CropIntent =new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri,"image/*");
            CropIntent.putExtra("crop",true);
            CropIntent.putExtra("outputX",360);
            CropIntent.putExtra("outputY",180);
            CropIntent.putExtra("aspectX",2);
            CropIntent.putExtra("aspectY",1);
            CropIntent.putExtra("scaleUpIfNeeded",true);
            CropIntent.putExtra("return-data",true);
            startActivityForResult(CropIntent,2);



        }catch (ActivityNotFoundException e)
        {

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageBitmap(bm);
    }

}

