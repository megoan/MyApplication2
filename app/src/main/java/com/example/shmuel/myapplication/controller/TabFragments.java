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

    public static CarsTabFragment carsTab;

    public static CarModelsFragment carModelsTab;

    public static BranchesFragment branchesTab;

    public static ClientTabFragment clientsTab;

    private CarsTabFragment cars;
    private CarModelsFragment carModels;
    private BranchesFragment branches;
    private ClientTabFragment clients;

    public TabFragments() {
        if(carsTab!=null)
        {
            //cars=new CarsTabFragment();
           // carsTab=cars;
        }
        else
        {
            carsTab=new CarsTabFragment();
        }
        if(carModelsTab!=null)
        {
            //carModels=new CarModelsFragment();
           // carModelsTab=carModels;
        }
        else
        {
            carModelsTab=new CarModelsFragment();
        }
        if(branchesTab!=null)
        {
            //branches=new BranchesFragment();
           // branchesTab=branches;
        }
        else {
            branchesTab =new BranchesFragment();
        }

        if(clientsTab!=null)
        {
           // clients=new ClientTabFragment();
           // clientsTab=clients;
        }
        else {
            clientsTab =new ClientTabFragment();
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
