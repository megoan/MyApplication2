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
    public boolean addClient(Client client) {
       return ListDataSource.clientList.add(client);

    }

    @Override
    public boolean addCarModel(CarModel carModel) {
      return   ListDataSource.carModelList.add(carModel);
    }

    @Override
    public boolean addCar(Car car) {
      return   ListDataSource.carList.add(car);
    }
    
    @Override
    public boolean addCar(Car car, int branchID)
    {
        if (addCar(car)) {
            return getBranch(branchID).getCarIds().add(car.getCarNum()) ;
        }
        return false;
    }

    @Override
    public boolean addBranch(Branch branch) {
       return ListDataSource.branchList.add(branch);
    }

    @Override
    public boolean updateClient(Client client) {

        for(int i=0;i< ListDataSource.clientList.size();i++)
        {
            if(ListDataSource.clientList.get(i).getId()==client.getId())
            {
                ListDataSource.clientList.set(i,client);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCarModel(CarModel carModel) {
        for(int i=0;i< ListDataSource.carModelList.size();i++)
        {
            if(ListDataSource.carModelList.get(i).getCarModelCode()==carModel.getCarModelCode())
            {
                ListDataSource.carModelList.set(i,carModel);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCar(Car car) {
        for(int i=0;i< ListDataSource.carList.size();i++)
        {
            if(ListDataSource.carList.get(i).getCarNum()==car.getCarNum())
            {
                ListDataSource.carList.set(i,car);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateBranch(Branch branch) {
        for(int i=0;i< ListDataSource.branchList.size();i++)
        {
            if(ListDataSource.branchList.get(i).getBranchNum()==branch.getBranchNum())
            {
              ListDataSource.branchList.set(i,branch);
                return true;
            }
        }
return false;
    }

    @Override
    public boolean deleteClient(int clientID) {
        Client clientTmp=null;
        for (Client client: ListDataSource.clientList
             ) {
            if(client.getId()==clientID){
                clientTmp=client;
                break;
            }
        }
        return ListDataSource.clientList.remove(clientTmp);
    }

    @Override
    public boolean deleteCarModel(int carModelID) {
        CarModel carModelTmp=null;
        for (CarModel carModel: ListDataSource.carModelList
                ) {
            if(carModel.getCarModelCode()==carModelID){
                carModelTmp=carModel;
                break;

            }
        }
        return ListDataSource.carModelList.remove(carModelTmp);
    }

    @Override
    public boolean deleteCar(int carID) {
        Car carTmp=null;
        for (Car car: ListDataSource.carList
                ) {
            if(car.getCarNum()==carID){
                carTmp=car;
                break;

            }
        }
        return ListDataSource.carList.remove(carTmp);
    }

    @Override
    public boolean deleteBranch(int branchID) {
        Branch branchTmp=null;
        for (Branch branch: ListDataSource.branchList
                ) {
            if(branch.getBranchNum()==branchID){
                branchTmp=branch;
                break;
            }
        }
        return ListDataSource.branchList.remove(branchTmp);
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
        return (ArrayList<CarModel>) ListDataSource.carModelList;
    }

    @Override
    public ArrayList<Client> getAllClients() {
        return (ArrayList<Client>) ListDataSource.clientList;
    }

    @Override
    public ArrayList<Branch> getAllBranches() {
        return (ArrayList<Branch>) ListDataSource.branchList;
    }

    @Override
    public ArrayList<Car> getAllCars() {
        return (ArrayList<Car>) ListDataSource.carList;
    }

    @Override
    public boolean removeCarFromBranch(int carID, int branch)
    {
        Branch branch1=getBranch(branch);
        branch1.getCarIds().remove(new Integer(carID));
        if(branch1.getCarIds().size()==0){
            branch1.setInUse(false);
            updateBranch(branch1);
        }
       return true;
    }

    @Override
    public boolean addCarToBranch(int carID, int branch){
        Branch branch1=getBranch(branch);
        if(branch1.getCarIds().size()==0){
            branch1.setInUse(true);
            updateBranch(branch1);
        }
        return getBranch(branch).getCarIds().add(new Integer(carID));
    }
}
