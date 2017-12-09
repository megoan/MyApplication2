package com.example.shmuel.myapplication.controller.cars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shmuel.myapplication.controller.DividerItemDecoration;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.model.backend.BackEndForList;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CarsTabFragment extends Fragment {

    private RecyclerView recyclerView;
    public static CarRecyclerViewAdapter mAdapter;
    ArrayList<Car>cars;
    BackEndFunc backEndFunc;
    View view1;
    LayoutInflater inflater1;
    ViewGroup container1;

    public CarsTabFragment() {
        backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater1=inflater;
        container1=container;
        // Inflate the layout for this fragment
        backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
        cars=backEndFunc.getAllCars();
        view1=inflater.inflate(R.layout.recycle_view_layout, container, false);
        recyclerView= view1.findViewById(R.id.recycleView);
        if (mAdapter==null) {
            mAdapter=new CarRecyclerViewAdapter(cars,getActivity());
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view1;
    }

    public void filterByCompanyName(String[] carCompanies,boolean[] carCompaniesChecked)
    {
        cars=new ArrayList<>(backEndFunc.getAllCars());
        ArrayList<Car> tmp=new ArrayList<>();
        for (int j = 0; j < carCompanies.length; j++) {
            for (Car car:cars) {
                CarModel carModel=backEndFunc.getCarModel(car.getCarModel());
                if(carModel.getCompanyName()==carCompanies[j])
                {
                    if (carCompaniesChecked[j]==false) {
                        tmp.add(car);
                    }
                }
            }

        }
        cars.removeAll(tmp);
        updateView();
    }

    public void sortCarByCarCompanyName()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){
                CarModel cm1=backEndFunc.getCarModel(o1.getCarModel());
                CarModel cm2=backEndFunc.getCarModel(o2.getCarModel());
                return cm1.getCompanyName().compareToIgnoreCase(cm2.getCompanyName());
            }
        });
        updateView();
    }
    public void sortCarByRating()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){

                return String.valueOf(o1.getRating()).compareToIgnoreCase(String.valueOf(o2.getRating()));
            }
        });
        updateView();
    }
    public void sortCarByYear()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){

                return String.valueOf(o1.getYear()).compareToIgnoreCase(String.valueOf(o2.getYear()));
            }
        });
        updateView();
    }
    public void sortCarByMileage()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){

                return String.valueOf(o1.getMileage()).compareToIgnoreCase(String.valueOf(o2.getMileage()));
            }
        });
        updateView();
    }
    public void sortCarBydailyPrice()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){

                return String.valueOf(o1.getOneDayCost()).compareToIgnoreCase(String.valueOf(o2.getOneDayCost()));
            }
        });
        updateView();
    }

    public void sortCarByMilePrice()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){

                return String.valueOf(o1.getOneKilometerCost()).compareToIgnoreCase(String.valueOf(o2.getOneKilometerCost()));
            }
        });
        updateView();
    }
    public void sortCarByCarBranch()
    {
        Collections.sort(cars, new Comparator<Car>(){
            public int compare(Car o1, Car o2){
                Branch cm1=backEndFunc.getBranch(o1.getBranchNum());
                Branch cm2=backEndFunc.getBranch(o2.getBranchNum());
                return cm1.getAddress().compare().compareToIgnoreCase(cm2.getAddress().compare());
            }
        });
        updateView();
    }

    public void updateView()
    {

        cars=backEndFunc.getAllCars();
        mAdapter.objects=cars;
        mAdapter.notifyDataSetChanged();
        //cars= backEndFunc.getAllCars();
       // mAdapter=new CarRecyclerViewAdapter(cars,getActivity());
       //recyclerView.setAdapter(mAdapter);
        //(recyclerView.getAdapter()).notifyDataSetChanged();
        // mAdapter=new CarRecyclerViewAdapter(cars,getActivity());
       // recyclerView.setAdapter(mAdapter);
    }
    public void updateView2(int position)
    {
        //updateView();
     //   view1=inflater1.inflate(R.layout.recycle_view_layout, container1, false);
     //   recyclerView= view1.findViewById(R.id.recycleView);
        if(mAdapter.objects.size()>backEndFunc.getAllCars().size())
        mAdapter.removeitem(position);
        //mAdapter.objects=cars;
        mAdapter.notifyDataSetChanged();
        //(recyclerView.getAdapter()).notifyDataSetChanged();
       //cars=backEndFunc.getAllCars();
       // mAdapter=new CarRecyclerViewAdapter(cars,getActivity());
       // recyclerView.setAdapter(mAdapter);
    }

    public String carSearchString(Car car)
    {
        CarModel carModel=backEndFunc.getCarModel(car.getCarModel());
        Branch branch=backEndFunc.getBranch(car.getBranchNum());
        return (carModel.getCompanyName()+" "+carModel.getCarModelName()+" "+ branch.getAddress().getAddressName()).toLowerCase();
    }

    public void filterCardsSearch(String string)
    {
        cars=new ArrayList<>(backEndFunc.getAllCars());
        ArrayList<Car> tmp=new ArrayList<>();
        for (int j = 0; j < cars.size(); j++) {
            for (Car car:cars) {

                if(!carSearchString(car).contains(string.toLowerCase()))
                {
                        tmp.add(car);
                }
            }
        }
        cars.removeAll(tmp);
        updateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mAdapter.actionMode!=null)
        {
            mAdapter.actionMode.finish();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAdapter.actionMode!=null)
        {
            mAdapter.actionMode.finish();
        }
    }
    public void setCarList()
    {
       cars=new ArrayList<>(backEndFunc.getAllCars());
       updateView();
       //mAdapter.notifyDataSetChanged();
    }
}
