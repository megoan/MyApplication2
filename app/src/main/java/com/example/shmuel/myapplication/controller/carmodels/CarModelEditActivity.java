package com.example.shmuel.myapplication.controller.carmodels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.Utility;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CarModelEditActivity extends AppCompatActivity {
    ProgressDialog progDailog;
    public ActionMode actionMode;
    CarModel carModel=new CarModel();
    String ret = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CropIntent;
    boolean imageSelected=false;
    Bitmap mBitmap;
    boolean update=false;
    private boolean inUse;
    private String imgUrl;
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    EditText companyNameText;
    EditText modelNameText;
    EditText modelIdText;
    EditText engineText;
    RadioButton automaticRadio;
    RadioButton manualRadio;
    RadioGroup transmission;
    EditText passengersText;
    EditText luggageText;
    CheckBox acBox;
    ImageView imageView;
    ScrollView scrollView;

    ProgressBar progressBar;
    boolean originalID=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_model_edit);



        MyActionModeCallbackCarModel callback=new MyActionModeCallbackCarModel();
        actionMode=startActionMode(callback);
        transmission=findViewById(R.id.transmission);

        scrollView=findViewById(R.id.carModel_scroll_view);
        companyNameText=findViewById(R.id.CompanyName_display_textView);
        modelNameText=findViewById(R.id.ModelName_display_textView);
        modelIdText=findViewById(R.id.ModelCode_display_textView);
        engineText=findViewById(R.id.EngineDisplacement_display_textView);
        automaticRadio=findViewById(R.id.radioButton);
        manualRadio=findViewById(R.id.radioButton2);
        transmission=findViewById(R.id.transmission);
        passengersText=findViewById(R.id.Passengers_display_textView);
        luggageText=findViewById(R.id.Luggage_display_textView);
        acBox=findViewById(R.id.AC_display_textView);
        imageView=findViewById(R.id.Car_imageView);
        progressBar=findViewById(R.id.downloadProgressBar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectImage();
            }
        });

        automaticRadio.setChecked(true);
        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            update=true;
            modelIdText.setEnabled(false);
            actionMode.setTitle("Update car model");
            modelIdText.setText("#"+String.valueOf(intent.getIntExtra("id",0)));
            companyNameText.setText(intent.getStringExtra("companyName"));
            modelNameText.setText(intent.getStringExtra("modelName"));
            engineText.setText(String.valueOf(intent.getDoubleExtra("engine",0)));
            Transmission transmission=(Transmission)intent.getSerializableExtra("transmission") ;
            passengersText.setText(String.valueOf(intent.getIntExtra("passengers",0)));
            luggageText.setText(String.valueOf(intent.getIntExtra("luggage",0)));
            imgUrl=intent.getStringExtra("imgUrl");

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(intent.getStringExtra("imgUrl"))
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
                    .into(imageView);
            //new DownloadImage().execute();




            acBox.setChecked(intent.getBooleanExtra("ac",false));
            if(transmission==Transmission.AUTOMATIC)
            {
                automaticRadio.setChecked(true);
                manualRadio.setChecked(false);
            }
            else
            {
                automaticRadio.setChecked(false);
                manualRadio.setChecked(true);
            }
        }
        else {
            actionMode.setTitle("Add new car model");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public class MyActionModeCallbackCarModel implements android.view.ActionMode.Callback{

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
                    String id1=modelIdText.getText().toString();

                    String companyName=companyNameText.getText().toString();
                    String modelName=modelNameText.getText().toString();
                    String engineTextString=engineText.getText().toString();
                    double engine= 0;
                    if (engineTextString.length()>0) {
                        engine = Double.parseDouble(engineTextString);
                    }
                    int clickedId=transmission.getCheckedRadioButtonId();
                    Transmission mTransmission=null;
                    switch (clickedId)
                    {
                        case R.id.radioButton:{
                            mTransmission=Transmission.AUTOMATIC;
                            break;
                        }
                        case R.id.radioButton2:{
                            mTransmission=Transmission.MANUAL;
                            break;
                        }
                    }
                    String passengersTextString=passengersText.getText().toString();
                    int passengers= 0;
                    if (passengersTextString.length()>0) {
                        passengers = Integer.parseInt(passengersTextString);
                    }
                    String luggageTextString=luggageText.getText().toString();
                    int luggage= 0;
                    if (luggageTextString.length()>0) {
                        luggage = Integer.parseInt(luggageTextString);
                    }
                    boolean ac=acBox.isChecked();
                     if(!imageSelected && update==false){
                         inputWarningDialog("don't be shy and upload a picture!");
                         return false;
                     }



                    if(id1.equals("")|| companyName.equals("") || modelName.equals("") || engine==0 || passengers==0 || luggage==0)
                    {
                        inputWarningDialog("Please fill all fields!");
                        return false;
                    }
                    //bad email address

                    if (update) {
                        carModel.setCarModelCode(Integer.valueOf(id1.substring(1)));
                    }
                    else
                    {
                        carModel.setCarModelCode(Integer.valueOf(id1));
                    }
                    carModel.setCompanyName(companyName);
                    carModel.setCarModelName(modelName);
                    carModel.setAc(ac);
                    carModel.setEngineDisplacement(engine);
                    carModel.setPassengers(passengers);
                    carModel.setTransmission(mTransmission);
                    carModel.setLuggage(luggage);
                    carModel.setInUse(inUse);
                    carModel.setImgURL(imgUrl);





                    AlertDialog.Builder builder = new AlertDialog.Builder(CarModelEditActivity.this);

                    builder.setMessage("are you sure?");

                    if(update) {
                        builder.setTitle("Update car model");
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    if (imageSelected) {
                                        new  BackGroundOnlyUpdateCarModel().execute();
                                    }
                                    else {
                                        new BackGroundUpdateCar().execute();
                                    }
                                } catch (Exception e) {
                                    inputWarningDialog(e.getMessage());
                                    return;
                                }
                            }
                        });
                    }
                    else {
                        builder.setTitle("Add car model");
                        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                   new BackGroundAddCar().execute();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CarModelEditActivity.this);
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
        companyNameText.setText("");
        modelNameText.setText("");
        modelIdText.setText("");
        passengersText.setText("");
        luggageText.setText("");
        acBox.setChecked(false);
        engineText.setText("");
        int defaultImage = CarModelEditActivity.this.getResources().getIdentifier("@drawable/default_car_image",null,CarModelEditActivity.this.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(CarModelEditActivity.this, defaultImage);
        imageView.setImageDrawable(drawable);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CarModelEditActivity .this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(CarModelEditActivity  .this);

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
        uri=Uri.fromFile(file);
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



    public class BackGroundAddCar extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarModelEditActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.addCarModel(carModel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("carModel"+"/"+carModel.getCarModelCode()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String url=downloadUrl.toString();
                    carModel.setImgURL(url);
                    new BackGroundupdateCarAfterAdd().execute();
                }
            });

        }
    }
    public class BackGroundupdateCarAfterAdd extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.updateCarModel(carModel);
            MySqlDataSource.carModelList=backEndFunc.getAllCarModels();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CarModelsFragment.mAdapter.objects= (ArrayList<CarModel>) MySqlDataSource.carModelList;
            CarModelsFragment.mAdapter.notifyDataSetChanged();
            progDailog.dismiss();
            if (!update) {
                Toast.makeText(CarModelEditActivity.this,
                        "new car model added", Toast.LENGTH_SHORT).show();
                resetView();
                carModel=new CarModel();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
            else {
                Toast.makeText(CarModelEditActivity.this,
                        "new car model updated", Toast.LENGTH_SHORT).show();
            }
            if (update) {
                finish();
            }
        }
    }
    public class BackGroundOnlyUpdateCarModel extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarModelEditActivity.this);
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
            imageRef = storageRef.child("carModel"+"/"+carModel.getCarModelCode()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    new BackGroundupdateCarAfterAdd().execute();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class BackGroundUpdateCar extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(CarModelEditActivity.this);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.updateCarModel(carModel);
            MySqlDataSource.carModelList=backEndFunc.getAllCarModels();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CarModelEditActivity.this,
                    "car model updated", Toast.LENGTH_SHORT).show();
            CarModelsFragment.mAdapter.objects= (ArrayList<CarModel>) MySqlDataSource.carModelList;
            CarModelsFragment.mAdapter.notifyDataSetChanged();
            finish();
        }
    }
}
