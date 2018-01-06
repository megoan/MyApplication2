package com.example.shmuel.myapplication.model.backend;

import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;

/**
 * Created by shmuel on 19/10/2017.
 */

public interface BackEndFunc {


    public Updates addClient(Client client);
    public Updates addCarModel(CarModel carModel);
    public Updates addCar(Car car);
    public Updates addBranch(Branch branch);



    public boolean updateClient(Client client);
    public boolean updateCarModel(CarModel carModel);
    public Updates updateCar(Car car);
    public Updates updateCar(Car car,int originalCarModel, int originalBranch);
    public boolean updateBranch(Branch branch);



    public boolean deleteClient(int clientID);
    public boolean deleteCarModel(int carModelID);
    public Updates deleteCar(int carID, int branchID);
    public boolean deleteBranch(int branchID);

    public Client getClient(int id);
    public CarModel getCarModel(int carModelNumber);
    public Car getCar(int carNumber);
    public Branch getBranch(int branchNumber);



    public ArrayList<CarModel> getAllCarModels();
    public ArrayList<Client> getAllClients();
    public ArrayList<Branch> getAllBranches();
    public ArrayList<Car>getAllCars();

    public boolean removeCarFromBranch(int carID, int branch);
    public boolean addCarToBranch(int carID, int branch);



}
