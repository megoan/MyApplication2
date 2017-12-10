package com.example.shmuel.myapplication.model.backend;

import android.content.ContentValues;

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
    public boolean addCar(Car car) {
        return false;
    }

    @Override
    public boolean addCar(Car car, int branchID) {
        return false;
    }

    @Override
    public boolean addBranch(Branch branch) {
        ContentValues contentValues = TakeNGoConst.BranchToContentValues(branch);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewbranch.php", contentValues);
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
    public boolean updateClient(Client client) {
        ContentValues contentValues = TakeNGoConst.ClientToContentValues(client);
        try {
            String result = PHPtools.POST(WEB_URL + "/updateclient.php", contentValues);
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
    public boolean updateCarModel(CarModel carModel) {
        ContentValues contentValues = TakeNGoConst.CarModelToContentValues(carModel);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatecarmodel.php", contentValues);
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
    public boolean updateCar(Car car) {
        return false;
    }

    @Override
    public void updateCar(Car car, int originalCarModel) {

    }

    @Override
    public boolean updateBranch(Branch branch) {
        ContentValues contentValues = TakeNGoConst.BranchToContentValues(branch);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatebranch.php", contentValues);
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
    public boolean deleteClient(int clientID) {
        ContentValues contentValues = TakeNGoConst.ClientIdToContentValues(clientID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deleteclient.php", contentValues);
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
    public boolean deleteCarModel(int carModelID) {
        ContentValues contentValues = TakeNGoConst.CarModelIdToContentValues(carModelID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletecarmodel.php", contentValues);
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
    public boolean deleteCar(int carID) {
        ContentValues contentValues = TakeNGoConst.CarIdToContentValues(carID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletecar.php", contentValues);
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
    public boolean deleteBranch(int branchID) {
        ContentValues contentValues = TakeNGoConst.BranchIdToContentValues(branchID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deletebranch.php", contentValues);
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
    public Client getClient(int id) {
        List<Client> result = new ArrayList<Client>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findoneclient.php");
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
        return  result.get(0);
    }

    @Override
    public CarModel getCarModel(int carModelNumber) {
        List<CarModel> result = new ArrayList<CarModel>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findonecarmodel.php");
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
        return result.get(0);
    }

    @Override
    public Car getCar(int carNumber) {
        return null;
    }

    @Override
    public Branch getBranch(int branchNumber) {
        List<Branch> result = new ArrayList<Branch>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findonebranch.php");
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
        return null;
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
        return updateBranch(branch1) ;
    }

}
