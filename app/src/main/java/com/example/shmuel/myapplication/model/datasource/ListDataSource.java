package com.example.shmuel.myapplication.model.datasource;

import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;
import com.example.shmuel.myapplication.model.entities.MyDate;
import com.example.shmuel.myapplication.model.entities.Order;
import com.example.shmuel.myapplication.model.entities.Transmission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shmuel on 19/10/2017.
 */

public class ListDataSource {
    public static ArrayList<CarModel> carModelList;
    public static ArrayList<Client>clientList;
    public static ArrayList<Branch>branchList;
    public static ArrayList<Car>carList;
    public static ArrayList<Order>orderList;

    public ListDataSource() {
        carList=new ArrayList<>();
        carModelList=new ArrayList<>();
        branchList=new ArrayList<>();
        orderList=new ArrayList<>();
        clientList=new ArrayList<>();
        initialize();
    }
    public void initialize()
    {
        carModelList.add(new CarModel(111,"Audi","R8",32.1, Transmission.AUTOMATIC,2,2,true,"@drawable/audi8_model",true));
        carModelList.add(new CarModel(112,"Ford","Fusion Sedan",32.1, Transmission.AUTOMATIC,4,3,true,"@drawable/ford_2018",false));
        carModelList.add(new CarModel(113,"Jeep","Compass",32.1, Transmission.MANUAL,5,4,true,"@drawable/jeep_compass",true));
        carModelList.add(new CarModel(114,"lamborghini","aventador",32.1, Transmission.MANUAL,2,1,true,"@drawable/lambergini_aventador",true));
        carModelList.add(new CarModel(115,"Mazda","2",32.1, Transmission.AUTOMATIC,4,2,true,"@drawable/mazda2",true));
        carModelList.add(new CarModel(116,"Mazda","3 Sedan",32.1, Transmission.AUTOMATIC,5,2,true,"@drawable/mazda3_sedan",true));
        carModelList.add(new CarModel(117,"Mazda","CX5",32.1, Transmission.AUTOMATIC,4,2,true,"@drawable/mazda_cx5_2017",true));
        carModelList.add(new CarModel(118,"Mazda","MPV",32.1, Transmission.AUTOMATIC,7,5,true,"@drawable/mazda_mpv",true));
        carModelList.add(new CarModel(119,"Mitsubishi","Montero Sport",32.1, Transmission.MANUAL,5,2,true,"@drawable/mitsubishi_montero_sport",true));
        carModelList.add(new CarModel(120,"Skoda","Superb",32.1, Transmission.MANUAL,4,2,true,"@drawable/skoda_supberb",true));
        carModelList.add(new CarModel(121,"Toyota","Corolla",32.1, Transmission.AUTOMATIC,5,2,true,"@drawable/toyota_corola",true));




        carModelList.add(new CarModel(11,"Audi","R8",32.1, Transmission.AUTOMATIC,1,4,true,"@drawable/ferrari_812",true));
        carList.add(new Car(11,11,50,1,3.5,30,74,35,"@drawable/audi8",1997,false));
        branchList.add(new Branch(new Address("Hifa","sanhedria","12a"),12,11,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("abudaba","zuhabi",311,"999","s@e.com","111"));

        carModelList.add(new CarModel(12,"Farrari","R8",33.1, Transmission.MANUAL,2,3,false,"@drawable/ferrari_812",false));
        carList.add(new Car(12,12,50,2,4.5,45,20,45,"@drawable/car",1992,true));
        branchList.add(new Branch(new Address("Bet Shemesh","Hatziporen","12a"),12,12,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("Shmuel","Soibelman",312,"999","s@e.com","111"));

        carModelList.add(new CarModel(13,"Audi","R8",32.1, Transmission.AUTOMATIC,1,4,true,"@drawable/ferrari_812",true));
        carList.add(new Car(13,13,50,3,1.5,48,10,62,"@drawable/car",2005,true));
        branchList.add(new Branch(new Address("Elat","harambam","12a"),13,13,"@drawable/rental",15000,new MyDate(2,"july",2055),false));
        clientList.add(new Client("Elomaka","koyoso",313,"999","s@e.com","111"));

        carModelList.add(new CarModel(14,"Toyota","R8",32.1, Transmission.MANUAL,7,4,false,"@drawable/ferrari_812",false));
        carList.add(new Car(14,14,50,4,3,5,70,70,"@drawable/car",1994,false));
        branchList.add(new Branch(new Address("Beer Sheva","hacharutzim","12a"),14,14,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("avi","blala",314,"999","s@e.com","111"));

        carModelList.add(new CarModel(15,"Ford","R8",35.1, Transmission.AUTOMATIC,2,6,true,"@drawable/ferrari_812",false));
        carList.add(new Car(15,15,50,5,5,200,70,70,"@drawable/car",2994,true));
        branchList.add(new Branch(new Address("Hifa","alenbyz","12a"),15,15,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("avi","golo",315,"999","s@e.com","111"));

        carModelList.add(new CarModel(16,"Delete","D",32.1, Transmission.MANUAL,3,3,false,"@drawable/ferrari_812",false));
        carList.add(new Car(16,15,50,6,2.5,33,70,70,"@drawable/car",1894,false));
        branchList.add(new Branch(new Address("Hifa","alenbyf","12a"),16,16,"@drawable/rental",15000,new MyDate(2,"july",2055),false));
        clientList.add(new Client("lambde","melon",316,"999","s@e.com","111"));
    }
}
