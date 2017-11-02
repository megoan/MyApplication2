package com.example.shmuel.myapplication.model.backend;

import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shmuel on 19/10/2017.
 */

public class BackEndForList implements BackEndFunc {
    @Override
    public boolean clientExists(int clientID) {
        for (Client client: ListDataSource.clientList
             ) {
            if(clientID==client.getId())return true;
        }
        return false;
    }

    @Override
    public void addClient(Client client) {
        ListDataSource.clientList.add(client);

    }

    @Override
    public void addCarModel(CarModel carModel) {
        ListDataSource.carModelList.add(carModel);
    }

    @Override
    public void addCar(Car car) {
        ListDataSource.carList.add(car);
    }

    @Override
    public void addBranch(Branch branch) {
        ListDataSource.branchList.add(branch);
    }

    @Override
    public void updateClient(Client client) {

    }

    @Override
    public void updateCarModel(CarModel carModel) {

    }

    @Override
    public void updateCar(Car car) {

    }

    @Override
    public void updateBranch(Branch branch) {

    }

    @Override
    public Client getClient(int id) {
        for (Client client: ListDataSource.clientList
                ) {
            if(id==client.getId())return client;
        }
        return null;
    }

    @Override
    public CarModel getCarModel(int carModelNumber) {
        for (CarModel carModel: ListDataSource.carModelList
                ) {
            if(carModelNumber==carModel.getCarModelCode())return carModel;
        }
        return null;
    }

    @Override
    public Car getCar(int carNumber) {
        for (Car car: ListDataSource.carList
                ) {
            if(carNumber==car.getCarNum())return car;
        }
        return null;
    }

    @Override
    public Branch getBranch(int branchNumber) {
        for (Branch branch: ListDataSource.branchList
                ) {
            if(branchNumber==branch.getBranchNum())return branch;
        }
        return null;
    }

    @Override
    public ArrayList<CarModel> getAllCarModels() {
        return ListDataSource.carModelList;
    }

    @Override
    public ArrayList<Client> getAllClients() {
        return ListDataSource.clientList;
    }

    @Override
    public ArrayList<Branch> getAllBranches() {
        return ListDataSource.branchList;
    }

    @Override
    public ArrayList<Car> getAllCars() {
        return ListDataSource.carList;
    }
}
