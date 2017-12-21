package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class MyAddress {
   //private String city;
   // private String street;
   // private String number;
    String country;
    private String addressName;
    private double latitude;
    private double longitude;

    public MyAddress() {
    }

    public MyAddress(String addressName, String country, double latitude, double longitude) {
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country=country;
    }


    /*public MyAddress(String city, String street, String number) {
        this.city = city;
        this.street = street;
        this.number = number;
    }*/


    public MyAddress(MyAddress other) {
        this.country = other.country;

        this.addressName = other.addressName;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
    }

   /* public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }*/


    /* public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }*/
    public String compare()
    {
        return getAddressName();
    }

    @Override
    public String toString() {
        return addressName+"~~"+country+"~~"+latitude+"~~"+longitude;
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
}
