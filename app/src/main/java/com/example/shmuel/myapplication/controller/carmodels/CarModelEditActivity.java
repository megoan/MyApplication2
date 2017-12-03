package com.example.shmuel.myapplication.controller.carmodels;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.cars.CarEditActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;

public class CarModelEditActivity extends AppCompatActivity {
    ProgressDialog progDailog;
    public ActionMode actionMode;
    CarModel carModel=new CarModel();

    boolean update=false;
    private boolean inUse;
    private String imgUrl;
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_model_edit);
        carModel.setImgURL("@drawable/default_car_image");
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

        automaticRadio.setChecked(true);
        Intent intent =getIntent();
        String update1=intent.getStringExtra("update");
        if(update1.equals("true"))
        {
            update=true;
            modelIdText.setEnabled(false);
            actionMode.setTitle("Update car");
            modelIdText.setText("#"+String.valueOf(intent.getIntExtra("id",0)));
            companyNameText.setText(intent.getStringExtra("companyName"));
            modelNameText.setText(intent.getStringExtra("modelName"));
            engineText.setText(String.valueOf(intent.getDoubleExtra("engine",0)));
            Transmission transmission=(Transmission)intent.getSerializableExtra("transmission") ;
            passengersText.setText(String.valueOf(intent.getIntExtra("passengers",0)));
            luggageText.setText(String.valueOf(intent.getIntExtra("luggage",0)));
            imgUrl=intent.getStringExtra("imgUrl");
            int defaultImage = CarModelEditActivity.this.getResources().getIdentifier(imgUrl,null,CarModelEditActivity.this.getPackageName());
            Drawable drawable= ContextCompat.getDrawable(CarModelEditActivity.this, defaultImage);
            imageView.setImageDrawable(drawable);
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
                    double engine= Double.parseDouble(engineText.getText().toString());
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

                    int passengers= Integer.parseInt(passengersText.getText().toString());
                    int luggage= Integer.parseInt(luggageText.getText().toString());
                    boolean ac=acBox.isChecked();



                    if(id1.equals("")|| companyName.equals("")||modelName.equals("")||engine==0||passengers==0|| luggage==0)
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
                    if (imgUrl!=null) {
                        carModel.setImgURL(imgUrl);
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(CarModelEditActivity.this);
                    if(update){
                        builder.setTitle("Update car model");}
                    else{
                        builder.setTitle("Add car model");
                    }

                    builder.setMessage("are you sure?");

                    if(update) {
                        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO check that all sadot are filled

                                try {
                                    new  BackGroundUpdateCar().execute();
                                    /*backEndFunc.updateCar(car);

                                    Toast.makeText(CarEditActivity.this,
                                            "car updated", Toast.LENGTH_SHORT).show();
                                    actionMode.finish();*/
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
                                    new  BackGroundUpdateCar().execute();
                                    /*backEndFunc.addCar(car);
                                    backEndFunc.addCarToBranch(car.getCarNum(),car.getBranchNum());
                                    BranchesFragment.mAdapter.objects=backEndFunc.getAllBranches();
                                    BranchesFragment.mAdapter.notifyDataSetChanged();

                                    Toast.makeText(CarEditActivity.this,
                                            "new car added", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CarModelEditActivity.this);
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
        companyNameText.setText("");
        modelNameText.setText("");
        modelIdText.setText("");
        passengersText.setText("");
        luggageText.setText("");
        acBox.setChecked(false);
        engineText.setText("");
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
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (update)
            {
                backEndFunc.updateCarModel(carModel);
            }
            else
            {
                backEndFunc.addCarModel(carModel);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(update)
            {

                Toast.makeText(CarModelEditActivity.this,
                        "car model updated", Toast.LENGTH_SHORT).show();

                CarModelsFragment.mAdapter.objects= backEndFunc.getAllCarModels();
                CarModelsFragment.mAdapter.notifyDataSetChanged();
                finish();
            }
            else
            {
                Toast.makeText(CarModelEditActivity.this,
                        "new car model added", Toast.LENGTH_SHORT).show();


                CarModelsFragment.mAdapter.objects= backEndFunc.getAllCarModels();
                CarModelsFragment.mAdapter.notifyDataSetChanged();
                resetView();
                carModel=new CarModel();
                progDailog.dismiss();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }
    }
}
