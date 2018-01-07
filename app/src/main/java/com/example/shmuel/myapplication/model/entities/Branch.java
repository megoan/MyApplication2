package com.example.shmuel.myapplication.model.entities;

import java.util.ArrayList;

/**
 * Created by shmuel on 19/10/2017.
 */

public class Branch {
    private MyAddress myAddress;
    private int parkingSpotsNum;
    private int branchNum;
    private String imgURL;
    private double branchRevenue;
    private MyDate establishedDate;
    private boolean inUse;
    private ArrayList<Integer>carIdsList;

    public Branch() {
    }

    public Branch(MyAddress myAddress, int parkingSpotsNum, int branchNum, String imgURL, double branchRevenue, MyDate establishedDate, boolean inUse) {
        this.myAddress = myAddress;
        this.parkingSpotsNum = parkingSpotsNum;
        this.branchNum = branchNum;
        this.imgURL=imgURL;
        this.branchRevenue=branchRevenue;
        this.establishedDate=new MyDate(establishedDate);
        this.inUse=inUse;
        carIdsList=new ArrayList<>();
    }

    public Branch(Branch other) {
        this.myAddress = new MyAddress(other.myAddress);
        this.parkingSpotsNum = other.parkingSpotsNum;
        this.branchNum = other.branchNum;
        this.imgURL=other.imgURL;
        this.branchRevenue=other.branchRevenue;
        this.establishedDate=new MyDate(other.establishedDate);
        this.inUse=other.inUse;
        this.carIdsList=other.carIdsList;
    }

    public MyAddress getMyAddress() {
        return myAddress;
    }

    public void setMyAddress(MyAddress myAddress) {
        this.myAddress = new MyAddress(myAddress);
    }

    public int getParkingSpotsNum() {
        return parkingSpotsNum;
    }

    public void setParkingSpotsNum(int parkingSpotsNum) {
        this.parkingSpotsNum = parkingSpotsNum;
    }

    public int getBranchNum() {
        return branchNum;
    }

    public void setBranchNum(int branchNum) {
        this.branchNum = branchNum;
    }

    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public double getBranchRevenue() {
        return branchRevenue;
    }

    public void setBranchRevenue(double branchRevenue) {
        this.branchRevenue = branchRevenue;
    }

    public MyDate getEstablishedDate() {
        return new MyDate(establishedDate);
    }

    public void setEstablishedDate(MyDate establishedDate) {
        this.establishedDate = new MyDate(establishedDate);
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public ArrayList<Integer> getCarIds() {
        return carIdsList;
    }

    public void setCarIds(ArrayList<Integer> carIds) {
        this.carIdsList = carIds;
    }

    public int numberOfParkingSpotsAvailable()
    {
        return parkingSpotsNum-carIdsList.size();
    }

    public void addCar(int carID)
    {
        if(!carIdsList.contains(carID))carIdsList.add(carID);
    }
    public void removeCar(int carID){
        if(carIdsList.contains(carID))carIdsList.remove(carID);
    }

    public String convertCarIDtoString()
    {
        String carIDs="";
        for(Integer id:carIdsList)
        {
            carIDs+=id+"~~";
        }
        return carIDs;
    }
}
