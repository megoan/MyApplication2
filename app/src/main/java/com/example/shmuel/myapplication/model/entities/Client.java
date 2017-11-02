package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class Client {
    private String name;
    private String lastName;
    private int id;
    private String phoneNum;
    private String emailAddress;
    private String creditCardNum;


    public Client() {
    }

    public Client(String name, String lastName, int id, String phoneNum, String emailAddress, String creditCardNum) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.phoneNum = phoneNum;
        this.emailAddress = emailAddress;
        this.creditCardNum = creditCardNum;
    }

    public Client(Client other) {
        this.name = other.name;
        this.lastName = other.lastName;
        this.id = other.id;
        this.phoneNum = other.phoneNum;
        this.emailAddress = other.emailAddress;
        this.creditCardNum = other.creditCardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }
}
