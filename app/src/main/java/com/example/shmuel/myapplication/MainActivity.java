package com.example.shmuel.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.branches.BranchesFragment;
import com.example.shmuel.myapplication.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.cars.CarsTabFragment;
import com.example.shmuel.myapplication.dialogs.filter.FilterBranchesDialogFragment;
import com.example.shmuel.myapplication.dialogs.filter.FilterCarDialogFragment;
import com.example.shmuel.myapplication.dialogs.filter.FilterCarModelDialogFragment;
import com.example.shmuel.myapplication.dialogs.filter.FilterClientDialogFragment;
import com.example.shmuel.myapplication.dialogs.sort.SortBranchesDialogFragment;
import com.example.shmuel.myapplication.dialogs.sort.SortCarDialogFragment;
import com.example.shmuel.myapplication.dialogs.sort.SortCarModelDialogFragment;
import com.example.shmuel.myapplication.dialogs.sort.SortClientDialogFragment;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
    private ViewPager mViewPager;
    private TabsType tabsType=TabsType.CARS;
    private PageAdapter pageAdapter;
    int updatedTab=0;
    boolean check=true;
    SearchView searchView;


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
        super.onCreate(savedInstanceState);
        TabFragments tabFragments=new TabFragments();

        ListDataSource listDataSource=new ListDataSource();

        setContentView(R.layout.activity_main);


        if (savedInstanceState!=null) {
            updatedTab=savedInstanceState.getInt("CHILD");
            check=false;
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
        pageAdapter = new PageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        searchView=(SearchView)findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(check) {
                    switch (tabsType) {
                        case CARS: {
                            CarsTabFragment carsTabFragment = (CarsTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                            carsTabFragment.filterCardsSearch(newText);
                            break;
                        }
                        case CAR_MODELS: {
                            CarModelsFragment carModelsFragment = (CarModelsFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                            carModelsFragment.filterCardsSearch(newText);
                            break;
                        }
                        case BRANCHES: {
                            BranchesFragment branchesFragment = (BranchesFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                            branchesFragment.filterCardsSearch(newText);
                            break;
                        }
                        case CLIENTS: {
                            ClientTabFragment clientTabFragment = (ClientTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                            clientTabFragment.filterCardsSearch(newText);
                            break;
                        }
                    }
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mViewPager.getAdapter().notifyDataSetChanged();
                switch (position)
                {
                    case 0:{
                        tabsType=TabsType.CARS;
                        searchView.setQueryHint("cars");
                        break;
                    }
                    case 1:{
                        tabsType=TabsType.CAR_MODELS;
                        searchView.setQueryHint("car models");
                        break;
                    }
                    case 2:{
                        tabsType=TabsType.BRANCHES;
                        searchView.setQueryHint("branches");
                        break;
                    }
                    case 3:{
                        tabsType=TabsType.CLIENTS;
                        searchView.setQueryHint("clients");
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    public void onClickSort(View view)
    {
        switch (tabsType)
        {
            case CARS:{
                final CarsTabFragment carsTabFragment=(CarsTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                final CarModelsFragment carModelsFragment=(CarModelsFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                final BranchesFragment branchesFragment=(BranchesFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                final ClientTabFragment clientTabFragment=(ClientTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
        switch (tabsType)
        {
            case CARS:{
                final CarsTabFragment carsTabFragment=(CarsTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                        carsTabFragment.filterByCompanyName(carCompanies,carCompaniesChecked);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case CAR_MODELS:{
                final CarModelsFragment carModelsFragment=(CarModelsFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                        carModelsFragment.filterByCompanyName(carModelCompanies,carModelCompaniesChecked);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case BRANCHES:{
                final BranchesFragment branchesFragment=(BranchesFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
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
                        branchesFragment.filterByCompanyName(branchesCities,branchesCitiesChecked);
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
                /*final ClientTabFragment clientTabFragment=(ClientTabFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("filter by:");

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
                            clientTabFragment.filterByName("shmuel");
                        }
                        else if(checkedItem=="last name")
                        {
                            clientTabFragment.filterByLastName("soibelman");
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
                dialog.show();*/
                break;
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CHILD", mViewPager.getCurrentItem());
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

    public void activateFilters()
    {
        for (CarModel carmodel:ListDataSource.carModelList
                ) {
            carCompanySet.add(carmodel.getCompanyName());
        }

        s = new LinkedHashSet<>(carCompanySet);
        carCompanySet.clear();
        carCompanySet.addAll(s);

        carCompanies=new String[carCompanySet.size()];
        for (int i = 0; i < carCompanies.length; i++) {
            carCompanies[i]=  carCompanySet.get(i);

        }
        carCompaniesChecked=new boolean[carCompanies.length];
        for (int i = 0; i < carCompaniesChecked.length; i++) {
            carCompaniesChecked[i]=true;
        }




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
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
