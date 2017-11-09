package com.example.shmuel.myapplication.controller;

import com.example.shmuel.myapplication.controller.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.controller.cars.CarsTabFragment;

/**
 * Created by shmuel on 02/11/2017.
 */

public class TabFragments {
   public static CarsTabFragment tab1;

    public static CarModelsFragment tab2;

    public static BranchesFragment tab3;

    public static ClientTabFragment tab4;

    public TabFragments() {
        tab1=new CarsTabFragment();
        tab2=new CarModelsFragment();
        tab3 =new BranchesFragment();
        tab4 =new ClientTabFragment();
    }
}
