package com.example.shmuel.myapplication.controller.branches;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;


import com.example.shmuel.myapplication.controller.InputWarningDialog;
import com.example.shmuel.myapplication.controller.Utility;
import com.example.shmuel.myapplication.controller.carmodels.CarModelEditActivity;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.Updates;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.MyAddress;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.MyDate;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BranchEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    Branch branch=new Branch();

    DatePickerDialog datePickerDialog;
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
    private MyDate myDate;
    private MyAddress myAddress =new MyAddress();
    private ArrayList<Integer>carList=new ArrayList<>();
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CropIntent;
    boolean imageSelected=false;
    Bitmap mBitmap;
    ScrollView scrollView;
    TextView numOfCarsText;
    EditText numOfSpotsText;
    TextView addressText;
    EditText branchRevenueText;
    TextView establishedDateText;
    ImageView imageView;
    EditText branchIDText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_edit);

        scrollView=findViewById(R.id.branch_scroll_view);
        numOfCarsText=(TextView) findViewById(R.id.numOfCars);
        progressBar=findViewById(R.id.downloadProgressBar);
        numOfSpotsText=(EditText)findViewById(R.id.numOfSpots);
        addressText=(TextView)findViewById(R.id.address);
        branchRevenueText=(EditText)findViewById(R.id.revenue);
        establishedDateText=(TextView)findViewById(R.id.established);
        imageView=(ImageView)findViewById(R.id.mainImage);
        branchIDText=(EditText)findViewById(R.id.branch_id);

        MyActionModeCallbackBranch callback=new MyActionModeCallbackBranch();
        actionMode=startActionMode(callback);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectImage();
            }
        });

        ((FloatingActionButton) findViewById(R.id.dateButton))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });

        progressBar.setVisibility(View.INVISIBLE);
        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            imgUrl=intent.getStringExtra("imgUrl");
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(intent.getStringExtra("imgUrl"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.rental)
                    .placeholder(R.drawable.rental)
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

            //branch myAddress
            myAddress.setAddressName(intent.getStringExtra("addressName"));
            myAddress.setLatitude(intent.getDoubleExtra("latitude",0));
            myAddress.setLongitude(intent.getDoubleExtra("longitude",0));
            myAddress.setCountry(intent.getStringExtra("country"));
            myAddress.setLocality(intent.getStringExtra("locality"));
           // myAddress.setStreet(intent.getStringExtra("street"));
           // myAddress.setNumber(intent.getStringExtra("number"));
            addressText.setText(myAddress.getAddressName());

            //branch revenue
            branchRevenue=intent.getDoubleExtra("revenue",0);
            branchRevenueText.setText(String.valueOf(branchRevenue));

            //branch established date
            myDate=new MyDate();
            myDate.setYear(intent.getIntExtra("year",0));
            myDate.setMonth(intent.getStringExtra("month"));
            myDate.setDay(intent.getIntExtra("day",0));
            establishedDateText.setText(intent.getStringExtra("established"));

            int mYear=myDate.getYear();
            int mMonth=myDate.getMonth(0);
            int mDay=myDate.getDay();
            datePickerDialog = new DatePickerDialog(
                    BranchEditActivity.this, BranchEditActivity.this, myDate.getYear(), myDate.getMonth(0), myDate.getDay());



            inUse=intent.getBooleanExtra("inUse",false);
            numOfCars=intent.getIntExtra("numOfCars",0);




        }
        else {
            actionMode.setTitle("Add new branch");
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(
                    BranchEditActivity.this, BranchEditActivity.this, mYear, mMonth, mDay);
            progressBar.setVisibility(View.INVISIBLE);
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
                myAddress.setAddressName(place.getAddress().toString());
                myAddress.setLatitude(lan);
                myAddress.setLongitude(lon);
                Locale locale=place.getLocale();

                Geocoder geocoder = new Geocoder(BranchEditActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lan, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }



                String addressNameString=addresses.get(0).getAddressLine(0);
                if (addressNameString!=null) {
                    addressNameString = Normalizer.normalize(addressNameString, Normalizer.Form.NFD);
                    addressNameString= addressNameString.replaceAll("[^\\x00-\\x7F]", "");
                    addressNameString= addressNameString.replaceAll("[^a-zA-Z0-9\\s]", "");
                }

                String localityName = addresses.get(0).getLocality();
                if (localityName!=null) {
                    localityName= Normalizer.normalize(localityName, Normalizer.Form.NFD);
                    localityName = localityName.replaceAll("[^\\x00-\\x7F]", "");
                    localityName= localityName.replaceAll("[^a-zA-Z0-9\\s]", "");
                }

                String countryName = addresses.get(0).getCountryName();
                if (countryName!=null) {
                    countryName= Normalizer.normalize(countryName, Normalizer.Form.NFD);
                    countryName = countryName.replaceAll("[^\\x00-\\x7F]", "");
                    countryName= countryName.replaceAll("[^a-zA-Z0-9\\s]", "");
                }

                myAddress.setCountry(countryName);
                myAddress.setLocality(localityName);
                myAddress.setAddressName(addressNameString+", "+localityName+", "+countryName);
                addressText.setText(myAddress.getAddressName());

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
        myDate=new MyDate();
        myDate.setYear(year);
        myDate.setMonth(month);
        myDate.setDay(dayOfMonth);
        establishedDateText.setText(myDate.toString());
    }


    public class MyActionModeCallbackBranch implements android.view.ActionMode.Callback{

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
                    String parkingNumString=numOfSpotsText.getText().toString();
                    if (parkingNumString.length()>0) {
                        parkingSpotsNum= Integer.parseInt(parkingNumString);
                    }

                    if(!imageSelected && update==false){
                        InputWarningDialog.showWarningDialog("Missing Image","please add an image of the branch! \nand try again!",BranchEditActivity.this);return false;}


                    if(id1.equals("")||parkingSpotsNum==0|| addressString==null || establishedString==null ||myDate==null)
                    {
                        InputWarningDialog.showWarningDialog("Missing information","please enter all fields! \nand try again!",BranchEditActivity.this);
                        return false;
                    }
                    if(parkingSpotsNum<numOfCars)
                    {
                        InputWarningDialog.showWarningDialog("Invalid information","there are more cars then parking spots! \nplease change the number of spots and try again!",BranchEditActivity.this);
                    }

                    if (update) {
                        branch.setBranchNum(Integer.valueOf(id1.substring(1)));
                    }
                    else
                    {
                        branch.setBranchNum(Integer.valueOf(id1));
                    }
                    branch.setInUse(inUse);
                    branch.setBranchRevenue(branchRevenueString);
                    branch.setEstablishedDate(myDate);
                    branch.setParkingSpotsNum(parkingSpotsNum);
                    branch.setMyAddress(myAddress);
                    branch.setCarIds(carList);

                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchEditActivity.this);
                    builder.setMessage("are you sure?");


                    if(update) {
                        branch.setImgURL(imgUrl);
                        builder.setTitle("Update branch");
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    if (imageSelected) {

                                        new  BackGroundUpdateBranchWithImage().execute();
                                    }
                                    else {

                                        new BackGroundUpdateBranchNoImage().execute();
                                    }
                                } catch (Exception e) {
                                    InputWarningDialog.showWarningDialog("Server error","sorry, branch wasn't updated! \nplease try again soon!",BranchEditActivity.this);
                                    return;
                                }
                            }
                        });
                    }
                    else {
                        builder.setTitle("Add branch");
                        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    new BackGroundAddBranchNoImgUrl().execute();
                                } catch (Exception e) {
                                    InputWarningDialog.showWarningDialog("Server error","sorry, branch wasn't added! \nplease try again soon!",BranchEditActivity.this);
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


    public void resetView()
    {
        numOfCarsText.setText("0");
        numOfSpotsText.setText("");
        addressText.setText("");
        branchRevenueText.setText("");
        establishedDateText.setText("");
        branchIDText.setText("");
        int defaultImage = BranchEditActivity.this.getResources().getIdentifier("@drawable/rental",null,BranchEditActivity.this.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(BranchEditActivity.this, defaultImage);
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

            if (update)
            {
                backEndFunc.updateBranch(branch);
                MySqlDataSource.branchList=backEndFunc.getAllBranches();
                if(update==true && imageSelected==true)
                {

                }
            }
            else
            {
                try {
                    backEndFunc.addBranch(branch);

                } catch (Exception e) {
                   //inputWarningDialog("try uploading again soon!");
                }
                MySqlDataSource.branchList=backEndFunc.getAllBranches();
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
                BranchesFragment.branches=(ArrayList<Branch>) MySqlDataSource.branchList;
                BranchesFragment.mAdapter.objects= (ArrayList<Branch>) MySqlDataSource.branchList;
                BranchesFragment.mAdapter.notifyDataSetChanged();
                finish();
            }
            else
            {
                Toast.makeText(BranchEditActivity.this,"new bramch added", Toast.LENGTH_SHORT).show();
                BranchesFragment.branches=(ArrayList<Branch>) MySqlDataSource.branchList;
                BranchesFragment.mAdapter.objects= (ArrayList<Branch>) MySqlDataSource.branchList;
                BranchesFragment.mAdapter.notifyDataSetChanged();
                resetView();
                branch=new Branch();
                progDailog.dismiss();
                scrollView.fullScroll(ScrollView.FOCUS_UP);

            }
            actionMode.finish();
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(BranchEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(BranchEditActivity.this);

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
                    CropImage.activity(uri).setAspectRatio(1,1)
                            .start(this);
                    //CropImage();
                }
                //onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                CropImage.activity(uri).setAspectRatio(1,1)
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
        CropImage();
        imageView.setImageBitmap(thumbnail);
    }

    private void CropImage() {
        try {
            CropIntent =new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri,"image/*");
            CropIntent.putExtra("crop",true);
            CropIntent.putExtra("outputX",180);
            CropIntent.putExtra("outputY",180);
            CropIntent.putExtra("aspectX",1);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progDailog!=null) {
            progDailog.dismiss();
        }
    }
    public class BackGroundAddBranchNoImgUrl extends AsyncTask<Void,Void,Updates> {

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
        protected Updates doInBackground(Void... voids) {
               return backEndFunc.addBranch(branch);
        }

        @Override
        protected void onPostExecute(Updates updates) {
            super.onPostExecute(updates);
            if(updates==Updates.ERROR){
                progDailog.dismiss();
                InputWarningDialog.showWarningDialog("Server error","sorry, branch wasn't added! \nplease try again soon!",BranchEditActivity.this);
                return;
            }
            else if(updates==Updates.DUPLICATE)
            {
                progDailog.dismiss();
                InputWarningDialog.showWarningDialog("Duplicate ID","A branch with this ID already exists! \nchange client's id and try again",BranchEditActivity.this);
                return;
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("branch"+"/"+branch.getBranchNum()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String url=downloadUrl.toString();
                    branch.setImgURL(url);
                    new BackGroundUpdateBranchNoImage().execute();
                }
            });

        }
    }
    public class BackGroundUpdateBranchNoImage extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(update){
                progDailog = new ProgressDialog(BranchEditActivity.this);
                progDailog.setMessage("Updating...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(false);
                progDailog.show();
            }

        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean b=backEndFunc.updateBranch(branch);
            if (b==true) {
                MySqlDataSource.branchList=backEndFunc.getAllBranches();
            }
            return b;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(!b)
            {
                InputWarningDialog.showWarningDialog("Server error","sorry, branch wasn't updated! \nplease try again soon!",BranchEditActivity.this);
                progDailog.dismiss();
                return;
            }
            BranchesFragment.branches=MySqlDataSource.branchList;
            BranchesFragment.mAdapter.objects= (ArrayList<Branch>) MySqlDataSource.branchList;
            BranchesFragment.mAdapter.notifyDataSetChanged();
            progDailog.dismiss();
            if (!update) {
                Toast.makeText(BranchEditActivity.this,
                        "new branch added", Toast.LENGTH_SHORT).show();
                resetView();
                branch=new Branch();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
            else {
                Toast.makeText(BranchEditActivity.this,
                        "new branch updated", Toast.LENGTH_SHORT).show();
            }
            if (update) {
                finish();
            }
        }
    }
    public class BackGroundUpdateBranchWithImage extends AsyncTask<Void,Void,Void> {

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
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("branch"+"/"+branch.getBranchNum()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    new BackGroundUpdateBranchNoImage().execute();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
