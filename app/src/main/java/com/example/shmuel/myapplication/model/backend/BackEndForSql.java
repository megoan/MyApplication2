package com.example.shmuel.myapplication.model.backend;

import android.content.ContentValues;

import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.datasource.PHPtools;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;
import com.example.shmuel.myapplication.model.entities.TakeNGoConst;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fotij on 03/12/2017.
 */

public class BackEndForSql implements BackEndFunc {
    private static final String WEB_URL = "http://ymehrzad.vlab.jct.ac.il";

    @Override
    public boolean addClient(Client client) {
        ContentValues contentValues = TakeNGoConst.ClientToContentValues(client);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewclient.php", contentValues);
            long id = Long.parseLong(result);
            if (id > 0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public boolean addCarModel(CarModel carModel) {
        ContentValues contentValues = TakeNGoConst.CarModelToContentValues(carModel);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewcarmodel.php", contentValues);
            long id = Long.parseLong(result);
            if (id > 0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public Updates addCar(Car car) {
        Updates updates=Updates.BRANCH;
        ContentValues contentValues = TakeNGoConst.CarToContentValues(car);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewcar.php", contentValues);
            long id = Long.parseLong(result);
            if (id > 0) {
                CarModel carModel = getCarModel(car.getCarModel());
                if (carModel.isInUse() == false) {
                    carModel.setInUse(true);
                    updateCarModel(carModel);
                    return Updates.CARMODEL_AND_BRANCH;
                }
            }
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return updates;
    }



    @Override
    public boolean addBranch(Branch branch) {
        ContentValues contentValues = TakeNGoConst.BranchToContentValues(branch);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewbranch.php", contentValues);
            if(result.contains("Duplicate entry"))return false;
            long id = 0;
            try {
                id = Long.parseLong(result);
            } catch (NumberFormatException e) {
                return false;
            }
            if (id > 0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }





    @Override
    public boolean updateClient(Client client) {
        ContentValues contentValues = TakeNGoConst.ClientToContentValues(client);
        try {
            String result = PHPtools.POST(WEB_URL + "/updateclient.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public boolean updateCarModel(CarModel carModel) {
        ContentValues contentValues = TakeNGoConst.CarModelToContentValues(carModel);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatecarmodel.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public Updates updateCar(Car car, int originalCarModel,int originalBranch) {
       if(!updateCar(car))return Updates.ERROR;
       Updates updates=Updates.NOTHING;
       boolean checkIfCarModelInUse=false;
       if(originalCarModel!=car.getCarModel())
       {
           CarModel carModel = getCarModel(car.getCarModel());
           if (carModel.isInUse() == false) {
               carModel.setInUse(true);
               updateCarModel(carModel);
           }

           CarModel originalCarModelTmp = getCarModel(originalCarModel);
           ArrayList<Car> carArrayList = MySqlDataSource.carList;
           for (Car car1 : carArrayList) {
               if (car1.getCarModel() == carModel.getCarModelCode()) {
                   checkIfCarModelInUse=true;
               }
           }
           if (checkIfCarModelInUse==false) {
               carModel.setInUse(false);
               updateCarModel(originalCarModelTmp);
               updates=Updates.CARMODEL;
           }
       }
       if(originalBranch!=car.getBranchNum())
       {
           removeCarFromBranch(car.getCarNum(),originalBranch);
           addCarToBranch(car.getCarNum(), car.getBranchNum());
           if(updates==Updates.CARMODEL){
               updates=Updates.CARMODEL_AND_BRANCH;
           }
           else {
               updates=Updates.BRANCH;
           }
       }

        return updates;
    }

    @Override
    public boolean updateCar(Car car) {
        ContentValues contentValues = TakeNGoConst.CarToContentValues(car);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatecar.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }


    @Override
    public boolean updateBranch(Branch branch) {
        ContentValues contentValues = TakeNGoConst.BranchToContentValues(branch);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatebranch.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }





    @Override
    public boolean deleteClient(int clientID) {
        ContentValues contentValues = TakeNGoConst.ClientIdToContentValues(clientID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deleteclient.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteCarModel(int carModelID) {
        ContentValues contentValues = TakeNGoConst.CarModelIdToContentValues(carModelID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletecarmodel.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public Updates deleteCar(int carID, int branchID) {
        Updates updates=Updates.BRANCH;
        int carModelCode=0;
        Car carTmp=null;
        boolean dontneedToUpdateModel=false;
        for (Car car: MySqlDataSource.carList
                ) {
            if(car.getCarNum()==carID){
                carTmp=car;
                carModelCode=(car.getCarModel());
                break;
            }
        }
        MySqlDataSource.carList.remove(carTmp);

        for (Car car: MySqlDataSource.carList)
        {
            if(car.getCarModel()==carModelCode)
            {
                dontneedToUpdateModel=true;
            }
        }

        ContentValues contentValues = TakeNGoConst.CarIdToContentValues(carID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletecar.php", contentValues);
            if (result.compareTo("DONE") ==0)
            {
                if (!dontneedToUpdateModel) {
                    CarModel carModel=getCarModel(carModelCode);
                    carModel.setInUse(false);
                    updateCarModel(carModel);
                    updates=Updates.CARMODEL_AND_BRANCH;
                }
                removeCarFromBranch(carID,branchID);
                return updates;
            }
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }

        return updates;
    }

    @Override
    public boolean deleteBranch(int branchID) {
        ContentValues contentValues = TakeNGoConst.BranchIdToContentValues(branchID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletebranch.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public Client getClient(int id) {
        List<Client> result = new ArrayList<Client>();
        try {
            ContentValues contentValuesid = TakeNGoConst.ClientIdToContentValues(id);
            String str = PHPtools.POST(WEB_URL + "/findoneclient.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Client");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = TakeNGoConst.ContentValuesToClient(contentValues);
                result.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }

    @Override
    public CarModel getCarModel(int carModelNumber) {
        List<CarModel> result = new ArrayList<CarModel>();
        try {
            ContentValues contentValuesid = TakeNGoConst.CarModelIdToContentValues(carModelNumber);
            String str = PHPtools.POST(WEB_URL + "/findonecarmodel.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("CarModel");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                CarModel carModel = TakeNGoConst.ContentValuesToCarModel(contentValues);
                result.add(carModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }

    @Override
    public Car getCar(int carNumber) {
        List<Car> result = new ArrayList<Car>();
        try {
            ContentValues contentValuesid = TakeNGoConst.CarIdToContentValues(carNumber);
            String str = PHPtools.POST(WEB_URL + "/findonecar.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Car");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = TakeNGoConst.ContentValuesToCar(contentValues);
                result.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }

    @Override
    public Branch getBranch(int branchNumber) {
        List<Branch> result = new ArrayList<Branch>();
        try {
            ContentValues contentValuesid = TakeNGoConst.BranchIdToContentValues(branchNumber);
            String str = PHPtools.POST(WEB_URL + "/findonebranch.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Branch");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Branch branch = TakeNGoConst.ContentValuesToBranch(contentValues);
                result.add(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }





    @Override
    public ArrayList<CarModel> getAllCarModels() {
        List<CarModel> result = new ArrayList<CarModel>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallcarmodels.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("CarModels");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                CarModel carModel = TakeNGoConst.ContentValuesToCarModel(contentValues);
                result.add(carModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<CarModel>) result;
    }

    @Override
    public ArrayList<Client> getAllClients() {
        List<Client> result = new ArrayList<Client>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallclients.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Clients");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = TakeNGoConst.ContentValuesToClient(contentValues);
                result.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (ArrayList<Client>) result;
    }

    @Override
    public ArrayList<Branch> getAllBranches() {
        List<Branch> result = new ArrayList<Branch>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallbranches.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Branches");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Branch branch = TakeNGoConst.ContentValuesToBranch(contentValues);
                result.add(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Branch>) result;
    }

    @Override
    public ArrayList<Car> getAllCars() {
        List<Car> result = new ArrayList<Car>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallcars.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = TakeNGoConst.ContentValuesToCar(contentValues);
                result.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Car>) result;
    }

    @Override
    public boolean removeCarFromBranch(int carID, int branch) {
        Branch branch1 = getBranch(branch);
        branch1.getCarIds().remove(new Integer(carID));
        if (branch1.getCarIds().size() == 0) {
            branch1.setInUse(false);
            updateBranch(branch1);
        }
        return true;
    }

    @Override
    public boolean addCarToBranch(int carID, int branch) {
        Branch branch1 = getBranch(branch);
        if (branch1.getCarIds().size() == 0) {
            branch1.setInUse(true);
        }
        branch1.getCarIds().add(new Integer(carID));
        return updateBranch(branch1);
    }

}
