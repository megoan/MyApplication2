package com.example.shmuel.myapplication.model.datasource;

import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;

/**
 * Created by User on 22/12/2017.
 */

abstract class DataSource {
    public static ArrayList<Car> carList;
    public static ArrayList<CarModel> carModelList;
    public static ArrayList<Branch> branchList;
    public static ArrayList<Client> clientList;

    public DataSource() {
        carList=new ArrayList<>();
        carModelList=new ArrayList<>();
        branchList=new ArrayList<>();
        clientList=new ArrayList<>();
    }
    public abstract void initialize();
}
