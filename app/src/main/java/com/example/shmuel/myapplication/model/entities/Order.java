package com.example.shmuel.myapplication.model.entities;

/**
 * Created by shmuel on 19/10/2017.
 */

public class Order {
    private int clientNum;
    private boolean orderOpen;
    private int carNum;
    private String dateStart;
    private String dateEnd;
    private double mileageStart;
    private double mileageEnd;
    private boolean gasRefill;
    private int litersFilled;
    private double payment;
    private int orderNumber;

    public Order() {
    }

    public Order(int clientNum, boolean orderOpen, int carNum, String dateStart, String dateEnd, double mileageStart, double mileageEnd, boolean gasRefill, int litersFilled, double payment, int orderNumber) {
        this.clientNum = clientNum;
        this.orderOpen = orderOpen;
        this.carNum = carNum;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.mileageStart = mileageStart;
        this.mileageEnd = mileageEnd;
        this.gasRefill = gasRefill;
        this.litersFilled = litersFilled;
        this.payment = payment;
        this.orderNumber = orderNumber;
    }

    public Order(Order other) {
        this.clientNum = other.clientNum;
        this.orderOpen = other.orderOpen;
        this.carNum = other.carNum;
        this.dateStart = other.dateStart;
        this.dateEnd = other.dateEnd;
        this.mileageStart = other.mileageStart;
        this.mileageEnd = other.mileageEnd;
        this.gasRefill = other.gasRefill;
        this.litersFilled = other.litersFilled;
        this.payment = other.payment;
        this.orderNumber = other.orderNumber;
    }

    public int getClientNum() {
        return clientNum;
    }

    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }

    public boolean isOrderOpen() {
        return orderOpen;
    }

    public void setOrderOpen(boolean orderOpen) {
        this.orderOpen = orderOpen;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public double getMileageStart() {
        return mileageStart;
    }

    public void setMileageStart(double mileageStart) {
        this.mileageStart = mileageStart;
    }

    public double getMileageEnd() {
        return mileageEnd;
    }

    public void setMileageEnd(double mileageEnd) {
        this.mileageEnd = mileageEnd;
    }

    public boolean isGasRefill() {
        return gasRefill;
    }

    public void setGasRefill(boolean gasRefill) {
        this.gasRefill = gasRefill;
    }

    public int getLitersFilled() {
        return litersFilled;
    }

    public void setLitersFilled(int litersFilled) {
        this.litersFilled = litersFilled;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
