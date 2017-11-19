package com.example.shmuel.myapplication.controller;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shmuel.myapplication.controller.Clients.ClientActivity;
import com.example.shmuel.myapplication.controller.Clients.ClientEditActivity;
import com.example.shmuel.myapplication.controller.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.controller.cars.CarsTabFragment;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ListDataSource listDataSource;
    private BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    TabFragments tabFragments;
    //private ViewPager mViewPager;
    private TabsType tabsType=TabsType.CARS;
    //private PageAdapter pageAdapter;
    int updatedTab=0;
    boolean check=true;
    FloatingActionButton fab;
    SearchView searchView;
    public boolean is_in_action_mode=false;
    public boolean client_is_in_action_mode=false;
    public boolean car_model_is_in_action_mode=false;
    public boolean branch_is_in_action_mode=false;
    boolean searchClicked=false;
    boolean searchViewOn=false;
    public  String filter="";
    public Map<String,Boolean>carCom=new HashMap<String, Boolean>();
    public Map<String,Boolean>carModelCom=new HashMap<String, Boolean>();
    public Map<String,Boolean>branchesCom=new HashMap<String, Boolean>();

    //filtering companies
    String[] carCompanies;
    boolean[] carCompaniesChecked;
    ArrayList<String>carCompanySet=new ArrayList<>();
    Set<String> s;


    String[] carModelCompanies;
    boolean[] carModelCompaniesChecked;
    ArrayList<String>carModelCompanySet=new ArrayList<>();
    Set<String> cm;


    String[] branchesCities;
    boolean[] branchesCitiesChecked;
    ArrayList<String>branchesCitiesSet=new ArrayList<>();
    Set<String> b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i=0;
        i++;
        searchClicked=false;
        super.onCreate(savedInstanceState);
        if (TabFragments.tab1==null) {
            tabFragments=new TabFragments();
        }


        if (ListDataSource.carList==null) {
            listDataSource=new ListDataSource();
        }
        backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabsType)
                {
                    case CLIENTS:
                    {
                        Intent intent=new Intent(MainActivity.this,ClientEditActivity.class);
                        intent.putExtra("update","false");
                        startActivity(intent);
                        return;
                    }
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_blue)));

        if (savedInstanceState!=null) {
            updatedTab=savedInstanceState.getInt("CHILD");

            //check=false;
           // tabsType=TabsType.valueOf(savedInstanceState.getString("tab"));
        }
        else
        {

            check=true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        activateFilters();
        activateCarFilters();
        TabFragments.pageAdapter=new PageAdapter(getSupportFragmentManager());
        TabFragments.mViewPager=(ViewPager) findViewById(R.id.container);
        TabFragments.mViewPager.setOffscreenPageLimit(3);
        TabFragments.mViewPager.setAdapter(TabFragments.pageAdapter);



        // Set up the ViewPager with the sections adapter.


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(TabFragments.mViewPager);

        searchView=(SearchView)findViewById(R.id.search);
        searchView.setFocusable(false);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               // searchClicked=true;
                searchClicked=true;
                return false;
            }
        });

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                searchViewOn=true;
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchClicked && check) {
                    switch (tabsType) {
                        case CARS: {
                            //CarsTabFragment carsTabFragment = (CarsTabFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                            //carsTabFragment.filterCardsSearch(newText);
                            CarsTabFragment.mAdapter.getFilter().filter(newText);
                            //TabFragments.tab1.filterCardsSearch(newText);
                            break;
                        }
                        case CAR_MODELS: {
                            CarModelsFragment.mAdapter.getFilter().filter(newText);
                            break;
                        }
                        case BRANCHES: {
                            BranchesFragment.mAdapter.getFilter().filter(newText);
                            break;
                        }
                        case CLIENTS: {
                            ClientTabFragment.mAdapter.getFilter().filter(newText);

                            break;
                        }
                    }
                }
                return false;
            }
        });

        TabFragments.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                closeAction();
                searchView.setQuery(null, false);
                searchView.clearFocus();

                searchView.setIconified(true);

            }
            @Override
            public void onPageSelected(int position) {
                closeAction();
                TabFragments.mViewPager.getAdapter().notifyDataSetChanged();

                switch (position)
                {
                    case 0:{
                        tabsType=TabsType.CARS;
                        searchView.setQueryHint("cars");
                        if(TabFragments.tab1.mAdapter!=null && searchViewOn==true)
                        {
                           /* TabFragments.tab1.updateView2();
                            searchViewOn=false;*/
                        }
                        break;
                    }
                    case 1:{
                        tabsType=TabsType.CAR_MODELS;
                        searchView.setQueryHint("car models");
                        if(TabFragments.tab2.mAdapter!=null && searchViewOn==true)
                        {
                            /*TabFragments.tab2.updateView2();
                            searchViewOn=false;*/
                        }
                        break;
                    }
                    case 2:{
                        tabsType=TabsType.BRANCHES;
                        searchView.setQueryHint("branches");
                        if(TabFragments.tab3.mAdapter!=null && searchViewOn==true)
                        {
                            /*TabFragments.tab3.updateView2();
                            searchViewOn=false;*/
                        }
                        break;
                    }
                    case 3:{
                        tabsType=TabsType.CLIENTS;
                        searchView.setQueryHint("clients");
                        if(TabFragments.tab4.mAdapter!=null && searchViewOn==true)
                        {
                            /*TabFragments.tab4.updateView2();
                            searchViewOn=false;*/
                        }
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                    switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        changeFab();
                        fab.show();
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                    case ViewPager.SCROLL_STATE_SETTLING:
                        fab.hide();
                        break;
                }
            }
        });



    }
    public void onClickSort(View view)
    {
        closeAction();
        switch (tabsType)
        {
            case CARS:{
                final CarsTabFragment carsTabFragment=(CarsTabFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sort by:");

                // add a radio button list
                String[] options = {"company name", "branch", "year","mileage","daily price","mile price","rating"};
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        if (checkedItem=="company name")
                        {
                            carsTabFragment.sortCarByCarCompanyName();
                        }
                        else if(checkedItem=="branch")
                        {
                            carsTabFragment.sortCarByCarBranch();
                        }

                        else if(checkedItem=="year")
                        {
                            carsTabFragment.sortCarByYear();
                        }

                        else if(checkedItem=="mileage")
                        {
                            carsTabFragment.sortCarByMileage();
                        }
                        else if(checkedItem=="daily price")
                        {
                            carsTabFragment.sortCarBydailyPrice();
                        }

                        else if(checkedItem=="mile price")
                        {
                            carsTabFragment.sortCarByMilePrice();
                        }

                        else
                        {
                            carsTabFragment.sortCarByRating();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case CAR_MODELS:{
                final CarModelsFragment carModelsFragment=(CarModelsFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sort by:");

                // add a radio button list
                String[] options = {"company name", "passengers", "luggage"};
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        if (checkedItem=="company name")
                        {
                            carModelsFragment.sortCarModelsByCompanyName();
                        }
                        else if(checkedItem=="passengers")
                        {
                            carModelsFragment.sortCarModelsByPassengers();
                        }
                        else
                        {
                            carModelsFragment.sortCarModelsByLuggage();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case BRANCHES:{
                final BranchesFragment branchesFragment=(BranchesFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sort by:");

                // add a radio button list
                String[] options = {"address", "established date", "revenue","number of cars","branch number"};
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        if (checkedItem=="address")
                        {
                            branchesFragment.sortBranchByAddress();
                        }
                        else if(checkedItem=="established date")
                        {
                            branchesFragment.sortBranchByEstablished();
                        }

                        else if(checkedItem=="revenue")
                        {
                            branchesFragment.sortBranchByRevenue();
                        }

                        else if(checkedItem=="number of cars")
                        {
                            branchesFragment.sortBranchByNumberOfCars();
                        }
                        else
                        {
                            branchesFragment.sortBranchByBranchNum();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case CLIENTS:{
                final ClientTabFragment clientTabFragment=(ClientTabFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sort by:");

                // add a radio button list
                String[] options = {"first name", "last name", "id"};
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        if (checkedItem=="first name")
                        {
                            clientTabFragment.SortCliantByFirstName();
                        }
                        else if(checkedItem=="last name")
                        {
                            clientTabFragment.SortCliantByLastName();
                        }
                        else
                        {
                            clientTabFragment.SortCliantById();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
        }

    }
    public void onClickFilter(View view)
    {
        closeAction();
        switch (tabsType)
        {
            case CARS:{
                filter="";
                updateAbstractFilter(carCom,carCompanySet);
                //updateCarFilter();

                //final String filter="";
                carCompanies=new String[carCom.size()];
                carCompaniesChecked=new boolean[carCom.size()];
                int i=0;
                for (Map.Entry<String, Boolean> entry :  carCom.entrySet()
                        ) {
                    carCompanies[i]=entry.getKey();
                    carCompaniesChecked[i]=entry.getValue();
                    i++;
                }

                //final CarsTabFragment carsTabFragment=(CarsTabFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Filter by:");

                // add a checkbox button list
                builder.setMultiChoiceItems(carCompanies,carCompaniesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Update the current focused item's checked status
                        carCompaniesChecked[which]=isChecked;
                        // Get the current focused item
                        String currentItem = carCompanies[which];
                        // Notify the current action
                       /* Toast.makeText(getApplicationContext(),
                                currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();*/
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        for (int i=0;i<carCompanies.length;i++)
                        {
                            carCom.put(carCompanies[i],carCompaniesChecked[i]);
                            if (carCompaniesChecked[i]==true) {
                                filter+=carCompanies[i];

                            }

                        }
                        if(filter.length()==0)filter="you got no cars dude";
                        CarsTabFragment.mAdapter.getFilter().filter(filter);
                        CarsTabFragment.mAdapter.notifyDataSetChanged();

                        //carsTabFragment.filterByCompanyName(carCompanies,carCompaniesChecked);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case CAR_MODELS:{
                filter="";
                updateAbstractFilter(carModelCom,carModelCompanySet);
                //updateCarFilter();

                //final String filter="";
                carModelCompanies=new String[carModelCom.size()];
                carModelCompaniesChecked=new boolean[carModelCom.size()];
                int i=0;
                for (Map.Entry<String, Boolean> entry :  carModelCom.entrySet()
                        ) {
                    carModelCompanies[i]=entry.getKey();
                    carModelCompaniesChecked[i]=entry.getValue();
                    i++;
                }
                //final CarModelsFragment carModelsFragment=(CarModelsFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Filter by:");

                // add a checkbox button list
                builder.setMultiChoiceItems(carModelCompanies,carModelCompaniesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Update the current focused item's checked status
                        carModelCompaniesChecked[which]=isChecked;
                        // Get the current focused item
                        String currentItem = carModelCompanies[which];
                        // Notify the current action
                        /*Toast.makeText(getApplicationContext(),
                                currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();*/
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i=0;i<carModelCompanies.length;i++)
                        {
                            carCom.put(carModelCompanies[i],carModelCompaniesChecked[i]);
                            if (carModelCompaniesChecked[i]==true) {
                                filter+=carModelCompanies[i];

                            }

                        }
                        if(filter.length()==0)filter="you got no cars dude";
                        CarModelsFragment.mAdapter.getFilter().filter(filter);
                        CarModelsFragment.mAdapter.notifyDataSetChanged();

                        //carModelsFragment.filterByCompanyName(carModelCompanies,carModelCompaniesChecked);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case BRANCHES:{
                filter="";
                updateAbstractFilter(branchesCom,branchesCitiesSet);
                //updateCarFilter();

                //final String filter="";
                branchesCities=new String[branchesCom.size()];
                branchesCitiesChecked=new boolean[branchesCom.size()];
                int i=0;
                for (Map.Entry<String, Boolean> entry :  branchesCom.entrySet()
                        ) {
                    branchesCities[i]=entry.getKey();
                    branchesCitiesChecked[i]=entry.getValue();
                    i++;
                }
                final BranchesFragment branchesFragment=(BranchesFragment) TabFragments.mViewPager.getAdapter().instantiateItem(TabFragments.mViewPager, TabFragments.mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Filter by:");

                // add a checkbox button list
                builder.setMultiChoiceItems(branchesCities,branchesCitiesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Update the current focused item's checked status
                        branchesCitiesChecked[which]=isChecked;
                        // Get the current focused item
                        String currentItem = branchesCities[which];
                        // Notify the current action
                       /* Toast.makeText(getApplicationContext(),
                                currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();*/
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0;i<branchesCities.length;i++)
                        {
                            branchesCom.put(branchesCities[i],branchesCitiesChecked[i]);
                            if (branchesCitiesChecked[i]==true) {
                                filter+=branchesCities[i];

                            }

                        }
                        if(filter.length()==0)filter="you got no cars dude";
                        BranchesFragment.mAdapter.getFilter().filter(filter);
                        BranchesFragment.mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case CLIENTS:{

                Toast.makeText(getApplicationContext(),
                         "No information to filter", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CHILD", TabFragments.mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
        outState.putString("tab",tabsType.toString());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void activateCarFilters()
    {
        for (Car car:backEndFunc.getAllCars()
             ) {
            CarModel carModel=backEndFunc.getCarModel(car.getCarModel());
            carCompanySet.add(carModel.getCompanyName());
        }

        s = new LinkedHashSet<>(carCompanySet);
        carCompanySet.clear();
        carCompanySet.addAll(s);

        for (int i = 0; i < carCompanySet.size(); i++) {
            carCom.put(carCompanySet.get(i),true);
        }
    }
    public void activateFilters()
    {
        for (CarModel carmodel:ListDataSource.carModelList
                ) {
            carModelCompanySet.add(carmodel.getCompanyName());
        }

        s = new LinkedHashSet<>(carModelCompanySet);
        carModelCompanySet.clear();
        carModelCompanySet.addAll(s);

        carModelCompanies=new String[carModelCompanySet.size()];
        for (int i = 0; i < carModelCompanies.length; i++) {
            carModelCompanies[i]=  carModelCompanySet.get(i);

        }
        carModelCompaniesChecked=new boolean[carModelCompanies.length];
        for (int i = 0; i < carModelCompaniesChecked.length; i++) {
            carModelCompaniesChecked[i]=true;
        }



        for (Branch branch:ListDataSource.branchList
                ) {
            branchesCitiesSet.add(branch.getAddress().getCity());
        }

        b = new LinkedHashSet<>(branchesCitiesSet);
        branchesCitiesSet.clear();
        branchesCitiesSet.addAll(b);

        branchesCities=new String[branchesCitiesSet.size()];
        for (int i = 0; i < branchesCities.length; i++) {
            branchesCities[i]=  branchesCitiesSet.get(i);

        }
        branchesCitiesChecked=new boolean[branchesCities.length];
        for (int i = 0; i < branchesCitiesChecked.length; i++) {
            branchesCitiesChecked[i]=true;

        }
    }
    public void updateAbstractFilter(Map<String,Boolean> hashMap,ArrayList<String> set){

        set.clear();
        switch (tabsType)
        {
            case CARS:{
                for (Car car:backEndFunc.getAllCars()
                        ) {
                    CarModel carModel=backEndFunc.getCarModel(car.getCarModel());
                    set.add(carModel.getCompanyName());
                }
                break;
            }
            case CAR_MODELS:
            {
                for (CarModel carmodel:ListDataSource.carModelList
                        ) {
                    set.add(carmodel.getCompanyName());
                }
                break;
            }
            case BRANCHES:
            {
                for (Branch branch:ListDataSource.branchList
                        ) {
                    branchesCitiesSet.add(branch.getAddress().getCity());
                }
                break;
            }
            case CLIENTS:{
                break;
            }
        }
        s = new LinkedHashSet<>(set);
        set.clear();
        set.addAll(s);

        for(int i=0;i<set.size();i++)
        {
            if(!hashMap.containsKey(set.get(i))){
                hashMap.put(set.get(i),true);
            }
        }
        boolean check;
        ArrayList<String>r=new ArrayList<>();
        for (Map.Entry<String, Boolean> entry :  hashMap.entrySet()
                ) {
            check=false;
            for(int j=0;j<set.size();j++)
            {
                if(entry.getKey()==set.get(j))
                {
                    check=true;
                }
            }
            if(check==false)
            {
                r.add(entry.getKey());
            }
        }
        for (String s:r
                ) {
            hashMap.remove(s);
        }
    }
    public void updateCarFilter()
    {
        carCompanySet.clear();
        for (Car car:backEndFunc.getAllCars()
                ) {
            CarModel carModel=backEndFunc.getCarModel(car.getCarModel());
            carCompanySet.add(carModel.getCompanyName());
        }

        s = new LinkedHashSet<>(carCompanySet);
        carCompanySet.clear();
        carCompanySet.addAll(s);

        for(int i=0;i<carCompanySet.size();i++)
        {
            if(!carCom.containsKey(carCompanySet.get(i))){
            carCom.put(carCompanySet.get(i),true);
                 }


        }
        boolean check;
        ArrayList<String>r=new ArrayList<>();
        for (Map.Entry<String, Boolean> entry :  carCom.entrySet()
                ) {
            check=false;
            for(int j=0;j<carCompanySet.size();j++)
            {
                if(entry.getKey()==carCompanySet.get(j))
                {
                    check=true;
                }
            }
            if(check==false)
            {
               r.add(entry.getKey());
            }
        }
        for (String s:r
             ) {
            carCom.remove(s);
        }

    }

    public void updateCarModelFilter()
    {
        carModelCompanySet.clear();
        for (CarModel carModel:backEndFunc.getAllCarModels()
                ) {

            carCompanySet.add(carModel.getCompanyName());
        }

        s = new LinkedHashSet<>(carModelCompanySet);
        carModelCompanySet.clear();
        carModelCompanySet.addAll(s);

        for(int i=0;i<carModelCompanySet.size();i++)
        {
            if(!carModelCom.containsKey(carModelCompanySet.get(i))){
                carModelCom.put(carModelCompanySet.get(i),true);
            }


        }
        boolean check=false;
        ArrayList<String>r=new ArrayList<>();
        for (Map.Entry<String, Boolean> entry :  carModelCom.entrySet()
                ) {
            check=false;
            for(int j=0;j<carModelCompanySet.size();j++)
            {
                if(entry.getKey()==carModelCompanySet.get(j))
                {
                    check=true;
                }
            }
            if(check==false)
            {
                r.add(entry.getKey());
            }
        }
        for (String s:r
                ) {
            carModelCom.remove(s);
        }
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        int i=0;
        i++;
        switch (tabsType)
        {
            case CARS:{
                if(TabFragments.tab1.mAdapter!=null)
                {
                    //TabFragments.tab1.updateView();
                    //TabFragments.tab1.mAdapter.notifyDataSetChanged();

                }
                break;
            }
            case CAR_MODELS:
            {
                if(TabFragments.tab2.mAdapter!=null)
                {
                    TabFragments.tab2.mAdapter.notifyDataSetChanged();
                }
                break;
            }
            case BRANCHES:
            {
                if(TabFragments.tab3.mAdapter!=null)
                {
                    TabFragments.tab3.mAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CLIENTS:
            {
                if(TabFragments.tab4.mAdapter!=null)
                {

                    //TabFragments.tab4.updateView2();
                    TabFragments.tab4.mAdapter.notifyDataSetChanged();
                }
                break;
            }
        }

    }
    public void closeAction(){
        if (TabFragments.tab1.mAdapter!=null && TabFragments.tab1.mAdapter.actionMode!=null) {
            TabFragments.tab1.mAdapter.actionMode.finish();
        }
        if(TabFragments.tab4.mAdapter!=null && TabFragments.tab4.mAdapter.actionMode!=null)
        {
            TabFragments.tab4.mAdapter.actionMode.finish();
        }
        if(TabFragments.tab2.mAdapter!=null && TabFragments.tab2.mAdapter.actionMode!=null)
        {
            TabFragments.tab2.mAdapter.actionMode.finish();
        }
        if(TabFragments.tab3.mAdapter!=null && TabFragments.tab3.mAdapter.actionMode!=null)
        {
            TabFragments.tab3.mAdapter.actionMode.finish();
        }
    }
    public void changeFab()
    {
        switch (tabsType)
        {
            case CARS:{
                fab.setImageResource(R.drawable.ic_madd);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_blue)));
                break;
            }
            case CLIENTS:
            {
                fab.setImageResource(R.drawable.ic_add_client);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_darkred)));
                break;
            }
            case BRANCHES:
            {
                fab.setImageResource(R.drawable.ic_add_branch);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_darkgreen)));
                break;
            }
            case CAR_MODELS:
            {
                fab.setImageResource(R.drawable.ic_madd);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_darkorange)));
            }
        }
    }
}
