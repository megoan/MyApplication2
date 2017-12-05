package com.example.shmuel.myapplication.model.backend;

/**
 * Created by shmuel on 19/10/2017.
 * gets one instance of back end functions
 */

public class FactoryMethod {
    private static BackEndFunc backEndFunc=null;

    private FactoryMethod(DataSourceType dataSourceType) {

    }
    public static BackEndFunc getBackEndFunc(DataSourceType dataSourceType)
    {
        switch (dataSourceType){
            case DATA_INTERNET:
            {

            }
            case DATA_LIST:
            {
                if(backEndFunc==null)
                {
                    backEndFunc=new BackEndForList();
                }
            }
        }
        return backEndFunc;
    }
}
