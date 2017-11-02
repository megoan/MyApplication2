package com.example.shmuel.myapplication.Clients;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shmuel.myapplication.DividerItemDecoration;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.cars.CarRecyclerViewAdapter;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by shmuel on 23/10/2017.
 */

public class ClientTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private BackEndFunc backEndFunc;
    ClientRecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Client> clients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
        clients=new ArrayList<>(backEndFunc.getAllClients());
        View view1=inflater.inflate(R.layout.recycle_view_layout, container, false);
        recyclerView= view1.findViewById(R.id.recycleView);
        mAdapter=new ClientRecyclerViewAdapter(backEndFunc.getAllClients(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view1;
    }
    public void SortCliantByFirstName()
    {
        //clients=new ArrayList<>(backEndFunc.getAllClients());
        Collections.sort(clients, new Comparator<Client>(){
            public int compare(Client o1, Client o2){
               return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        updateView();
    }
    public void SortCliantByLastName()
    {
        //clients=new ArrayList<>(backEndFunc.getAllClients());
        Collections.sort(clients, new Comparator<Client>(){
            public int compare(Client o1, Client o2){
                return o1.getLastName().compareToIgnoreCase(o2.getLastName());
            }
        });
        updateView();
    }
    public void SortCliantById()
    {
        //clients=new ArrayList<>(backEndFunc.getAllClients());
        Collections.sort(clients, new Comparator<Client>(){
            public int compare(Client o1, Client o2){
                Integer i1=new Integer(o1.getId());
                Integer i2=new Integer(o2.getId());
                return i1.compareTo(i2);
            }
        });
        updateView();
    }

    public void filterByName(String s)
    {
        //clients=new ArrayList<>(backEndFunc.getAllClients());
        ArrayList<Client>tmp=new ArrayList<>();
        for (Client client:clients
             ) {
            if(client.getName()!=s)
            {
                tmp.add(client);
            }
        }
        clients.removeAll(tmp);
        updateView();
    }
    public void filterByLastName(String s)
    {
        //clients=new ArrayList<>(backEndFunc.getAllClients());
        ArrayList<Client>tmp=new ArrayList<>();
        for (Client client:clients
                ) {
            if(client.getLastName()!=s)
            {
                tmp.add(client);
            }
        }
        clients.removeAll(tmp);
        updateView();
    }
    public void updateView()
    {
        mAdapter=new ClientRecyclerViewAdapter(clients,getActivity());
        recyclerView.setAdapter(mAdapter);
    }

    public String ClientSearchString(Client client)
    {
        return (client.getName()+" "+client.getLastName()).toLowerCase();
    }
    public void filterCardsSearch(String string)
    {
        clients=new ArrayList<>(backEndFunc.getAllClients());
        ArrayList<Client> tmp=new ArrayList<>();
        for (int j = 0; j < clients.size(); j++) {
            for (Client client:clients) {

                if(!ClientSearchString(client).contains(string.toLowerCase()))
                {
                    tmp.add(client);
                }
            }
        }
        clients.removeAll(tmp);
        updateView();
    }
}
