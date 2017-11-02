package com.example.shmuel.myapplication;

import com.example.shmuel.myapplication.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.branches.BranchesFragment;
import com.example.shmuel.myapplication.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.cars.CarsTabFragment;

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
