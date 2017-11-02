package com.example.shmuel.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.shmuel.myapplication.Clients.ClientTabFragment;
import com.example.shmuel.myapplication.branches.BranchesFragment;
import com.example.shmuel.myapplication.carmodels.CarModelsFragment;
import com.example.shmuel.myapplication.cars.CarsTabFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shmuel on 22/10/2017.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    FragmentManager fm;
    private Map<Integer,String> mFragmentsTags;
    public PageAdapter(FragmentManager fm) {
        super(fm);
        this.fm=fm;
        mFragmentsTags=new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                CarsTabFragment tab1=TabFragments.tab1;
                return tab1;
            case 1:
                CarModelsFragment tab2=TabFragments.tab2;
                return tab2;
            case 2:
                BranchesFragment tab3 =TabFragments.tab3;
                return tab3;
            case 3:
                ClientTabFragment tab4 =TabFragments.tab4;
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
        if(object instanceof BranchesFragment) return POSITION_NONE;
        return super.getItemPosition(object);
    }
}
