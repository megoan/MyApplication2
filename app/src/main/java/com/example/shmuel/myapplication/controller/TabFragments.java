package com.example.shmuel.myapplication.controller;

import android.support.v4.view.ViewPager;

import com.example.shmuel.myapplication.controller.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.controller.cars.CarsTabFragment;

/**
 * Created by shmuel on 02/11/2017.
 */

public class TabFragments {


    public static ViewPager mViewPager;

    public static PageAdapter pageAdapter;

    public static CarsTabFragment tab1;

    public static CarModelsFragment tab2;

    public static BranchesFragment tab3;

    public static ClientTabFragment tab4;

    private CarsTabFragment cars;
    private CarModelsFragment carModels;
    private BranchesFragment branches;
    private ClientTabFragment clients;

    public TabFragments() {
        if(tab1!=null)
        {
            //cars=new CarsTabFragment();
           // tab1=cars;
        }
        else
        {
            tab1=new CarsTabFragment();
        }
        if(tab2!=null)
        {
            //carModels=new CarModelsFragment();
           // tab2=carModels;
        }
        else
        {
            tab2=new CarModelsFragment();
        }
        if(tab3!=null)
        {
            //branches=new BranchesFragment();
           // tab3=branches;
        }
        else {
            tab3 =new BranchesFragment();
        }

        if(tab4!=null)
        {
           // clients=new ClientTabFragment();
           // tab4=clients;
        }
        else {
            tab4 =new ClientTabFragment();
        }
    }


    public CarsTabFragment getCarFragment()
    {
       return cars;
    }
    public CarModelsFragment getCarModelFragment()
    {
        return carModels;
    }
    public BranchesFragment getBranchesFragment()
    {
        return branches;
    }
    public ClientTabFragment getClientsFragment()
    {
        return clients;
    }
}
