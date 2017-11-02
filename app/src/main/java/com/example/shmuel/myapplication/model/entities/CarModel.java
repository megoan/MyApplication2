package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class CarModel {
    private int carModelCode;
    private String companyName;
    private String carModelName;
    private double engineDisplacement;//nephach manoa
    private Transmission transmission;
    private int passengers;
    private int luggage;
    private boolean ac;
    private String imgURL;
    private boolean inUse;

    public CarModel() {
    }

    public CarModel(int carModelCode, String companyName, String carModelName, double engineDisplacement, Transmission transmission, int passengers, int luggage, boolean ac, String imgURL,boolean inUse) {
        this.carModelCode = carModelCode;
        this.companyName = companyName;
        this.carModelName = carModelName;
        this.engineDisplacement = engineDisplacement;
        this.transmission = transmission;
        this.passengers = passengers;
        this.luggage = luggage;
        this.ac = ac;
        this.imgURL=imgURL;
        this.inUse=inUse;
    }

    public CarModel(CarModel other) {
        this.carModelCode = other.carModelCode;
        this.companyName = other.companyName;
        this.carModelName = other.carModelName;
        this.engineDisplacement = other.engineDisplacement;
        this.transmission = other.transmission;
        this.passengers = other.passengers;
        this.luggage = other.luggage;
        this.ac = other.ac;
        this.imgURL=other.imgURL;
        this.inUse=other.inUse;
    }

    public int getCarModelCode() {
        return carModelCode;
    }

    public void setCarModelCode(int carModelCode) {
        this.carModelCode = carModelCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public double getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(double engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getLuggage() {
        return luggage;
    }

    public void setLuggage(int luggage) {
        this.luggage = luggage;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}


