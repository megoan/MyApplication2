package com.example.shmuel.myapplication.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shmuel.myapplication.controller.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.controller.branches.BranchesFragment;
import com.example.shmuel.myapplication.controller.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.controller.cars.CarsTabFragment;

/**
 * Created by shmuel on 22/10/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                CarsTabFragment tab1= /*new CarsTabFragment();*/TabFragments.tab1;
                return tab1;
            case 1:
                CarModelsFragment tab2=/*new CarModelsFragment();*/ TabFragments.tab2;
                return tab2;
            case 2:
                BranchesFragment tab3 =/*new BranchesFragment();*/  TabFragments.tab3;
                return tab3;
            case 3:
                ClientTabFragment tab4 =/* new ClientTabFragment();*/  TabFragments.tab4;
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CARS";
            case 1:
                return "CAR MODELS";
            case 2:
                return "BRANCHES";
            case 3:
                return "CLIENTS";
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
