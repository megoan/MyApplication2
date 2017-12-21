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


    public TabFragments() {
        if (carsTab == null) {
            carsTab = new CarsTabFragment();
            carModelsTab = new CarModelsFragment();
            branchesTab = new BranchesFragment();
            clientsTab = new ClientTabFragment();
        }
    }

}
