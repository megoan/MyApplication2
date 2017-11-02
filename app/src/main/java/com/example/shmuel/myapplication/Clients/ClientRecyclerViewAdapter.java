package com.example.shmuel.myapplication.Clients;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class ClientRecyclerViewAdapter extends RecyclerView.Adapter<ClientRecyclerViewAdapter.ViewHolder> {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    private ArrayList<Client> objects;
    private Context mContext;

    public ClientRecyclerViewAdapter(ArrayList<Client> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    @Override
    public ClientRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item_layout, parent, false);
        return new ClientRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientRecyclerViewAdapter.ViewHolder holder, int position) {
        Client client = objects.get(position);

        holder.firstName.setText(client.getName());
        holder.lastName.setText(client.getLastName());
        holder.id.setText(String.valueOf(client.getId()));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            firstName=(TextView)itemView.findViewById(R.id.firstName);
            lastName=(TextView)itemView.findViewById(R.id.lastName);
            id=(TextView)itemView.findViewById(R.id.id);
        }
    }
}
