package com.example.shmuel.myapplication.model.datasource;

import com.example.shmuel.myapplication.model.backend.BackEndForSql;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Client;
import com.example.shmuel.myapplication.model.entities.MyAddress;
import com.example.shmuel.myapplication.model.entities.MyDate;
import com.example.shmuel.myapplication.model.entities.Transmission;

/**
 * Created by fotij on 03/12/2017.
 */

public class MySqlDataSource extends DataSource {
    public MySqlDataSource() {
        super();
        initialize();
    }

    @Override
    public void initialize() {
        BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
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
        carModelList.add(new CarModel(121,"Toyota","Corolla",32.1, Transmission.AUTOMATIC,5,2,true,"@drawable/toyota_corola_2017",true));




        carModelList.add(new CarModel(11,"Audi","R8",32.1, Transmission.AUTOMATIC,1,4,true,"@drawable/ferrari_812",true));
        carList.add(new Car(11,5,50,1,3.5,30,74,35,"@drawable/audi8",1997,false));
        branchList.add(new Branch(new MyAddress("Hifa sanhedria 12a","israel",12,12),50,11,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("abudaba","zuhabi",311,"999","s@e.com","111"));

        carModelList.add(new CarModel(12,"Farrari","R8",33.1, Transmission.MANUAL,2,3,false,"@drawable/ferrari_812",false));
        carList.add(new Car(12,12,50,2,4.5,45,20,45,"@drawable/car",1992,true));
        branchList.add(new Branch(new MyAddress("Bet Shemesh Hatziporen 12a","israel",234,234),12,12,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("Shmuel","Soibelman",312,"999","s@e.com","111"));

        carModelList.add(new CarModel(13,"Audi","R8",32.1, Transmission.AUTOMATIC,1,4,true,"@drawable/ferrari_812",true));
        carList.add(new Car(13,564,50,3,1.5,48,10,62,"@drawable/car",2005,true));
        branchList.add(new Branch(new MyAddress("Elat harambam 12a","israel",234,234),13,13,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("Elomaka","koyoso",313,"999","s@e.com","111"));

        carModelList.add(new CarModel(14,"Toyota","R8",32.1, Transmission.MANUAL,7,4,false,"@drawable/ferrari_812",false));
        carList.add(new Car(14,4354,50,4,3,5,70,70,"@drawable/car",1994,false));
        branchList.add(new Branch(new MyAddress("Beer Sheva hacharutzim 12a","israel",12,12),14,14,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("avi","blala",314,"999","s@e.com","111"));

        carModelList.add(new CarModel(15,"Ford","R8",35.1, Transmission.AUTOMATIC,2,6,true,"@drawable/ferrari_812",false));
        carList.add(new Car(15,32423,50,5,5,200,70,70,"@drawable/car",2994,true));
        branchList.add(new Branch(new MyAddress("Hifa alenbyz 12a","israel",213,123),15,15,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("avi","golo",315,"999","s@e.com","111"));

        carModelList.add(new CarModel(16,"Delete","D",32.1, Transmission.MANUAL,3,3,false,"@drawable/ferrari_812",false));
        carList.add(new Car(16,546456,50,6,2.5,33,70,70,"@drawable/car",1894,false));
        branchList.add(new Branch(new MyAddress("Hifa alenbyf 12a","israel",324,324),16,16,"@drawable/rental",15000,new MyDate(2,"july",2055),true));
        clientList.add(new Client("lambde","melon",316,"999","s@e.com","111"));


        branchList.add(new Branch(new MyAddress("Hadera sanhedria 15b","israel",324,234),40,22,"@drawable/rental",3000,new MyDate(24,"June",1999),false));

        branchList.get(0).addCar(1);
        branchList.get(1).addCar(2);
        branchList.get(2).addCar(3);
        branchList.get(3).addCar(4);
        branchList.get(4).addCar(5);
        branchList.get(5).addCar(6);

        clientList = backEndFunc.getAllClients();
        carModelList=backEndFunc.getAllCarModels();
        //carList = backEndFunc.getAllCars();
        //carModelList = backEndFunc.getAllCarModels();
       // branchList = backEndFunc.getAllBranches();
    }
}
