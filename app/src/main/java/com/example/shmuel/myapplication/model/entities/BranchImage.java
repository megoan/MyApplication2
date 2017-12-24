package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 24/12/2017.
 */

public class BranchImage {
    private int _branchID;
    private String imgURL="@drawable/rental";

    public BranchImage() {
    }

    public BranchImage(BranchImage other) {
        this._branchID = other._branchID;
        this.imgURL = other.imgURL;
    }

    public int getBranchID() {
        return _branchID;
    }

    public void setBranchID(int branchID) {
        this._branchID = branchID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
