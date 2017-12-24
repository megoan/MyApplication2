package com.example.shmuel.myapplication.model.backend;

import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.BranchImage;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.CarModelImage;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shmuel on 19/10/2017.
 */

public interface BackEndFunc {


    public boolean addClient(Client client);
    public boolean addCarModel(CarModel carModel);
    public boolean addCar(Car car);
    public boolean addCar(Car car, int branchID);
    public boolean addBranch(Branch branch);
    public boolean addBranchImage(BranchImage branchImage);
    public boolean addCarModelImage(CarModelImage carModelImage);

    public boolean updateClient(Client client);
    public boolean updateCarModel(CarModel carModel);
    public boolean updateCar(Car car);
    public void updateCar(Car car,int originalCarModel);
    public boolean updateBranch(Branch branch);
    public boolean updateBranchImage(BranchImage branchImage );
    public boolean updateCarModelImage(CarModelImage carModelImage);

    public boolean deleteClient(int clientID);
    public boolean deleteCarModel(int carModelID);
    public boolean deleteCar(int carID);
    public boolean deleteBranch(int branchID);

    public Client getClient(int id);
    public CarModel getCarModel(int carModelNumber);
    public Car getCar(int carNumber);
    public Branch getBranch(int branchNumber);
    public BranchImage getBranchImage(int _branchID);
    public CarModelImage getCarModelImage(int carModelID);

    public ArrayList<CarModel> getAllCarModels();
    public ArrayList<Client> getAllClients();
    public ArrayList<Branch> getAllBranches();
    public ArrayList<Car>getAllCars();

    public boolean removeCarFromBranch(int carID, int branch);
    public boolean addCarToBranch(int carID, int branch);



}
