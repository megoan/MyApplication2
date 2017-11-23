package com.example.shmuel.myapplication.model.entities;

/**
 * Created by User on 27/10/2017.
 */

public class MyDate {
    private int day;
    private String month;
    private int year;

    public MyDate() {
    }

    public MyDate(int day, String month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public MyDate(MyDate other) {
        this.day = other.day;
        this.month = other.month;
        this.year = other.year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }
    public int getMonth(int stam){return convertMonthToNumber(month);}

    public void setMonth(String month) {
        this.month = month;
    }
    public void setMonth(int month) {
        this.month = convertNumberToMonth(month);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {

        return month+" "+day+", "+year;
    }
    public String compare()
    {
        int mon=0;
        if(month=="January"||month=="january")mon=0;
        else if(month=="February"||month=="february")mon=1;
        else if(month=="March"||month=="march")mon=2;
        else if(month=="April"||month=="April")mon=3;
        else if(month=="May"||month=="may")mon=4;
        else if(month=="June"||month=="june")mon=5;
        else if(month=="July"||month=="july")mon=6;
        else if(month=="August"||month=="august")mon=7;
        else if(month=="September"||month=="september")mon=8;
        else if(month=="October"||month=="october")mon=9;
        else if(month=="November"||month=="november")mon=10;
        else if(month=="December"||month=="december")mon=11;
        else mon=12;
        return year+String.valueOf(mon)+day;
    }
    public int convertMonthToNumber(String month)
    {
        if(month.equals("January")||month.equals("january"))return 0;
        else if(month.equals("February")||month.equals("february"))return 1;
        else if(month.equals("March")||month.equals("march"))return 2;
        else if(month.equals("April")||month.equals("April"))return 3;
        else if(month.equals("May")||month.equals("may"))return 4;
        else if(month.equals("June")||month.equals("june"))return 5;
        else if(month.equals("July")||month.equals("july"))return 6;
        else if(month.equals("August")||month.equals("august"))return 7;
        else if(month.equals("September")||month.equals("september"))return 8;
        else if(month.equals("October")||month.equals("october"))return 9;
        else if(month.equals("November")||month.equals("november"))return 10;
        else if(month.equals("December")||month.equals("december"))return 11;
        else return 12;
    }
    public String convertNumberToMonth(int month)
    {
        switch (month)
        {
            case 0:{
                return "January";
            }
            case 1:{
                return "February";
            }
            case 2:{
                return "March";
            }
            case 3:{
                return "April";
            }
            case 4:{
                return "May";
            }
            case 5:{
                return "June";
            }
            case 6:{
                return "July";
            }
            case 7:{
                return "August";
            }
            case 8:{
                return "September";
            }
            case 9:{
                return "October";
            }
            case 10:{
                return "November";
            }
            case 11:{
                return "December";
            }
        }
        return "no month";
    }
}

