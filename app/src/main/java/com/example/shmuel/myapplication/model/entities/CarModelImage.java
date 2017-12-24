package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 24/12/2017.
 */

public class CarModelImage {
    private int _carModelID;
    private String imgURL="@drawable/default_car_image";

    public CarModelImage() {
    }

    public CarModelImage(CarModelImage other) {
        this._carModelID = other._carModelID;
        this.imgURL = other.imgURL;
    }

    public int get_carModelID() {
        return _carModelID;
    }

    public void set_carModelID(int _carModelID) {
        this._carModelID = _carModelID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
