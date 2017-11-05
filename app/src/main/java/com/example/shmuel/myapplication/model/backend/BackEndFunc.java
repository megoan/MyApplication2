package com.example.shmuel.myapplication.model.backend;

import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shmuel on 19/10/2017.
 */

public interface BackEndFunc {
    public boolean clientExists(int clientID);

    public void addClient(Client client);
    public void addCarModel(CarModel carModel);
    public void addCar(Car car);
    public void addBranch(Branch branch);

    public void updateClient(Client client);
    public void updateCarModel(CarModel carModel);
    public void updateCar(Car car);
    public void updateBranch(Branch branch);

    public void deleteClient(int clientID);
    public void deleteCarModel(int carModelID);
    public void deleteCar(int carID);
    public void deleteBranch(int branchID);

    public Client getClient(int id);
    public CarModel getCarModel(int carModelNumber);
    public Car getCar(int carNumber);
    public Branch getBranch(int branchNumber);

    public ArrayList<CarModel> getAllCarModels();
    public ArrayList<Client> getAllClients();
    public ArrayList<Branch> getAllBranches();
    public ArrayList<Car>getAllCars();



}
