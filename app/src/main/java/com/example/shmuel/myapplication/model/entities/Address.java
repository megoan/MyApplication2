package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class Address {
    private String city;
    private String street;
    private String number;

    public Address() {
    }

    public Address(String city, String street, String number) {
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public Address(Address other) {
        this.city = other.city;
        this.street = other.street;
        this.number = other.number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
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
    }

    public String compare()
    {
        return city+street+number;
    }

    @Override
    public String toString() {
        return city+" "+street+" "+number;
    }
}
