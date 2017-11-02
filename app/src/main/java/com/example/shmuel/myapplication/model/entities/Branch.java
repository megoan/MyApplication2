package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class Branch {
    private Address address;
    private int parkingSpotsNum;
    private int branchNum;
    private String imgURL;
    private double branchRevenue;
    private MyDate establishedDate;

    public Branch() {
    }

    public Branch(Address address, int parkingSpotsNum, int branchNum, String imgURL, double branchRevenue, MyDate establishedDate) {
        this.address = address;
        this.parkingSpotsNum = parkingSpotsNum;
        this.branchNum = branchNum;
        this.imgURL=imgURL;
        this.branchRevenue=branchRevenue;
        this.establishedDate=new MyDate(establishedDate);
    }

    public Branch(Branch other) {
        this.address = new Address(other.address);
        this.parkingSpotsNum = other.parkingSpotsNum;
        this.branchNum = other.branchNum;
        this.imgURL=other.imgURL;
        this.branchRevenue=other.branchRevenue;
        this.establishedDate=new MyDate(other.establishedDate);
    }

    public Address getAddress() {
        return new Address(address);
    }

    public void setAddress(Address address) {
        this.address = new Address(address);
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
}
