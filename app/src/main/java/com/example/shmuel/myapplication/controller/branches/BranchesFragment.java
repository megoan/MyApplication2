package com.example.shmuel.myapplication.controller.branches;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shmuel.myapplication.controller.DividerItemDecoration;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class BranchesFragment extends Fragment {
    private RecyclerView recyclerView;
    public static BranchRecyclerViewAdapter mAdapter;
    BackEndFunc backEndFunc;
    public static ArrayList <Branch>branches;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
        branches=backEndFunc.getAllBranches();
        View view1=inflater.inflate(R.layout.recycle_view_layout, container, false);
        recyclerView= view1.findViewById(R.id.recycleView);
        if (mAdapter==null) {
            mAdapter=new BranchRecyclerViewAdapter(branches,getActivity());
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view1;
    }

    public void filterByCompanyName(String[] carCompanies,boolean[] carCompaniesChecked)
    {
        branches=new ArrayList<>(backEndFunc.getAllBranches());
        ArrayList<Branch> tmp=new ArrayList<>();
        for (int j = 0; j < carCompanies.length; j++) {
            for (Branch branch:branches) {
                if(branch.getMyAddress().getCountry()==carCompanies[j])
                {
                    if (carCompaniesChecked[j]==false) {
                        tmp.add(branch);
                    }
                }
            }
        }
        branches.removeAll(tmp);
        updateView();
    }

    public void sortBranchByAddress()
    {
        Collections.sort(branches, new Comparator<Branch>(){
            public int compare(Branch o1, Branch o2){

                return o1.getMyAddress().compare().compareToIgnoreCase(o2.getMyAddress().compare());
            }
        });
        updateView();
    }
    public void sortBranchByEstablished()
    {
        Collections.sort(branches, new Comparator<Branch>(){
            public int compare(Branch o1, Branch o2){

                return o1.getEstablishedDate().compare().compareToIgnoreCase(o2.getEstablishedDate().compare());
            }
        });
        updateView();
    }
    public void sortBranchByRevenue()
    {
        Collections.sort(branches, new Comparator<Branch>(){
            public int compare(Branch o1, Branch o2){

                return Double.compare(o1.getBranchRevenue(),o2.getBranchRevenue());
            }
        });
        updateView();
    }
    public void sortBranchByNumberOfCars()
    {
        Collections.sort(branches, new Comparator<Branch>(){
            public int compare(Branch o1, Branch o2){
                Integer integer1=new Integer(o1.getCarIds().size());
                Integer integer2=new Integer(o2.getCarIds().size());
                return integer1.compareTo(integer2);
            }
        });
        updateView();
    }
    public void sortBranchByBranchNum()
    {
        Collections.sort(branches, new Comparator<Branch>(){
            public int compare(Branch o1, Branch o2){
                Integer integer1=new Integer(o1.getBranchNum());
                Integer integer2=new Integer(o2.getBranchNum());
                return integer1.compareTo(integer2);
            }
        });
        updateView();
    }
    public void updateView()
    {
        mAdapter.objects=branches;
        mAdapter.notifyDataSetChanged();
    }
    public void updateView2(int position)
    {
        mAdapter.removeitem(position);
        mAdapter.notifyDataSetChanged();
    }

    public String BranchSearchString(Branch branch)
    {
        return (branch.getMyAddress().getAddressName().toLowerCase());
    }
    public void filterCardsSearch(String string)
    {
        branches=new ArrayList<>(backEndFunc.getAllBranches());
        ArrayList<Branch> tmp=new ArrayList<>();
        for (int j = 0; j < branches.size(); j++) {
            for (Branch branch:branches) {

                if(!BranchSearchString(branch).contains(string.toLowerCase()))
                {
                    tmp.add(branch);
                }
            }
        }
        branches.removeAll(tmp);
        updateView();
    }
    public void setBranchesList()
    {
        branches=new ArrayList<>(backEndFunc.getAllBranches());
        updateView();
    }
}
