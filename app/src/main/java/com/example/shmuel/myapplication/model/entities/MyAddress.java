package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class MyAddress {
   //private String city;
   // private String street;
   // private String number;
    String country;
    String locality;
    private String addressName;
    private double latitude;
    private double longitude;

    public MyAddress() {
    }

    public MyAddress(String addressName,String locality, String country, double latitude, double longitude) {
        this.addressName = addressName;
        this.country=country;
        this.locality=locality;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public MyAddress(MyAddress other) {
        this.addressName = other.addressName;
        this.country = other.country;
        this.locality=other.locality;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
    }


    public String compare()
    {
        return getAddressName();
    }

    @Override
    public String toString() {
        return addressName+"~~"+country+"~~"+locality+"~~"+latitude+"~~"+longitude;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
