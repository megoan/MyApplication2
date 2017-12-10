package com.example.shmuel.myapplication.model.entities;

import android.content.ContentValues;


/**
 * Created by fotij on 03/12/2017.
 */

public class TakeNGoConst {
    public static class ClientConst {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LASTNAME = "lastName";
        public static final String PHONE = "phoneNum";
        public static final String EMAILADDRESS = "emailAddress";
        public static final String CREDITCARDNUM = "creditCardNum";
    }

    public static class CarModelConst {
        public static final String CARMODELCODE = "_carModelCode";
        public static final String COMPANYNAME = "companyName";
        public static final String CARMODELNAME = "carModelName";
        public static final String ENGINEDISPLACMENT = "engineDisplacement";
        public static final String TRANSMISSION = "transmission";
        public static final String PASSENGERS = "passengers";
        public static final String LUGGAGE = "luggage";
        public static final String AC = "ac";
        public static final String IMAGEURL = "imgURL";
        public static final String INUSE = "inUse";
    }

    public static class BranchConst {
        public static final String ID = "_branchNum";
        public static final String NAME = "address";
        public static final String PARKINGSPOTSNUM = "parkingSpotsNum";
        public static final String IMAGEURL = "imgURL";
        public static final String BRANCHREVENUE = "branchRevenue";
        public static final String ESTABLISHEDDATE = "establishedDate";
        public static final String INUSE = "inUse";
        public static final String CARIDSLIST = "carIdsList";
    }

    public static class CarConst {
        public static final String CARNUM = "_carNum";
        public static final String BRANCHNUM = "branchNum";
        public static final String CARMODEL = "carModel";
        public static final String MILEAGE = "mileage";
        public static final String RATING = "rating";
        public static final String NUMOFRATINGS = "numOfRatings";
        public static final String ONEDAYCOST = "oneDayCost";
        public static final String ONEKILOMETERCOST = "oneKilometerCost";
        public static final String IMAGEURL = "imgURL";
        public static final String YEAR = "year";
        public static final String INUSE = "inUse";
    }

    public static ContentValues ClientToContentValues(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientConst.ID, client.getId());
        contentValues.put(ClientConst.NAME, client.getName());
        contentValues.put(ClientConst.LASTNAME, client.getLastName());
        contentValues.put(ClientConst.PHONE, client.getPhoneNum());
        contentValues.put(ClientConst.EMAILADDRESS, client.getEmailAddress());
        contentValues.put(ClientConst.CREDITCARDNUM, client.getCreditCardNum());
        return contentValues;
    }

    public static Client ContentValuesToClient(ContentValues contentValues) {
        Client client = new Client();
        client.setId(contentValues.getAsInteger(ClientConst.ID));
        client.setName(contentValues.getAsString(ClientConst.NAME));
        client.setLastName(contentValues.getAsString(ClientConst.LASTNAME));
        client.setPhoneNum(contentValues.getAsString(ClientConst.PHONE));
        client.setEmailAddress(contentValues.getAsString(ClientConst.EMAILADDRESS));
        client.setCreditCardNum((contentValues.getAsString(ClientConst.CREDITCARDNUM)));
        return client;
    }

    public static ContentValues CarModelToContentValues(CarModel carModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarModelConst.CARMODELCODE, carModel.getCarModelCode());
        contentValues.put(CarModelConst.COMPANYNAME, carModel.getCompanyName());
        contentValues.put(CarModelConst.CARMODELNAME, carModel.getCarModelName());
        contentValues.put(CarModelConst.ENGINEDISPLACMENT, carModel.getEngineDisplacement());
        contentValues.put(CarModelConst.TRANSMISSION, String.valueOf(carModel.getTransmission()));
        contentValues.put(CarModelConst.PASSENGERS, carModel.getPassengers());
        contentValues.put(CarModelConst.LUGGAGE, carModel.getLuggage());
        contentValues.put(CarModelConst.AC, carModel.isAc());
        contentValues.put(CarModelConst.IMAGEURL, carModel.getImgURL());
        contentValues.put(CarModelConst.INUSE, carModel.isInUse());
        return contentValues;
    }

    public static CarModel ContentValuesToCarModel(ContentValues contentValues) {
        CarModel carModel = new CarModel();
        carModel.setCarModelCode(contentValues.getAsInteger(CarModelConst.CARMODELCODE));
        carModel.setCompanyName(contentValues.getAsString(CarModelConst.COMPANYNAME));
        carModel.setCarModelName(contentValues.getAsString(CarModelConst.CARMODELNAME));
        carModel.setEngineDisplacement(contentValues.getAsDouble(CarModelConst.ENGINEDISPLACMENT));
        carModel.setTransmission(Transmission.valueOf(contentValues.getAsString(CarModelConst.TRANSMISSION)));
        carModel.setPassengers(contentValues.getAsInteger(CarModelConst.PASSENGERS));
        carModel.setLuggage((contentValues.getAsInteger(CarModelConst.LUGGAGE)));
        carModel.setAc(contentValues.getAsBoolean(CarModelConst.AC));
        carModel.setImgURL(contentValues.getAsString(CarModelConst.IMAGEURL));
        carModel.setInUse(contentValues.getAsBoolean(CarModelConst.INUSE));
        return carModel;
    }





    public static ContentValues BranchToContentValues(Branch branch) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BranchConst.ID, branch.getBranchNum());
        contentValues.put(BranchConst.NAME, branch.getMyAddress().toString());
        contentValues.put(BranchConst.PARKINGSPOTSNUM, branch.getParkingSpotsNum());
        contentValues.put(BranchConst.IMAGEURL, branch.getImgURL());
        contentValues.put(BranchConst.BRANCHREVENUE, branch.getBranchRevenue());
        contentValues.put(BranchConst.ESTABLISHEDDATE, branch.getEstablishedDate().toString());
        contentValues.put(BranchConst.INUSE, branch.isInUse());
        contentValues.put(BranchConst.CARIDSLIST, branch.getCarIds().toString());
        return contentValues;
    }

    public static Branch ContentValuesToBranch(ContentValues contentValues) {
        Branch branch = new Branch();
        branch.setBranchNum(contentValues.getAsInteger(BranchConst.ID));
        //branch.setAddress(contentValues.getAsString(BranchConst.NAME));
        branch.setParkingSpotsNum(contentValues.getAsInteger(BranchConst.PARKINGSPOTSNUM));
        branch.setImgURL(contentValues.getAsString(BranchConst.IMAGEURL));
        branch.setBranchRevenue(contentValues.getAsDouble(BranchConst.BRANCHREVENUE));
       // branch.setEstablishedDate((contentValues.getAsString(BranchConst.ESTABLISHEDDATE)));
        branch.setInUse(contentValues.getAsBoolean(BranchConst.INUSE));
       // branch.setCarIds((contentValues.getAsString(BranchConst.CARIDSLIST)));
        return branch;
    }












    public static ContentValues ClientIdToContentValues(int clientID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientConst.ID, clientID);
        return contentValues;
    }

    public static ContentValues CarModelIdToContentValues(int carModelID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarModelConst.CARMODELCODE, carModelID);
        return contentValues;
    }

    public static ContentValues CarIdToContentValues(int carID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarConst.CARNUM, carID);
        return contentValues;
    }
    public static ContentValues BranchIdToContentValues(int branchID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BranchConst.ID, branchID);
        return contentValues;
    }
}
