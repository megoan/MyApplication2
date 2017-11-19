package com.example.shmuel.myapplication.controller.Clients;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Client;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class ClientRecyclerViewAdapter extends RecyclerView.Adapter<ClientRecyclerViewAdapter.ViewHolder> implements Filterable{
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    private ArrayList<Client> objects;
    private Context mContext;
    public ActionMode actionMode;
    private int selectedPosition=-1;
    MyFilter myFilter;

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
    public void onBindViewHolder(ClientRecyclerViewAdapter.ViewHolder holder,final int position) {
        final Client client = objects.get(position);
        if(selectedPosition==position){
            if(((MainActivity)mContext).client_is_in_action_mode==true){
                holder.itemView.setBackgroundColor(Color.parseColor("#a3a3a3"));
            }
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClientRecyclerViewAdapter.MyActionModeCallbackClient callback=new ClientRecyclerViewAdapter.MyActionModeCallbackClient();
                actionMode=((Activity)mContext).startActionMode(callback);
                actionMode.setTitle("delete client");
                Toast.makeText(mContext,
                        "long click", Toast.LENGTH_SHORT).show();
                selectedPosition=position;
                ((MainActivity)mContext).client_is_in_action_mode=true;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);

                if(((MainActivity)mContext).client_is_in_action_mode==false){
                    Intent intent=new Intent(mContext,ClientActivity.class);
                    Client client1=objects.get(position);
                    intent.putExtra("name",client.getName());
                    intent.putExtra("lastName",client.getLastName());
                    intent.putExtra("id",client.getId());
                    intent.putExtra("phone",client.getPhoneNum());
                    intent.putExtra("email",client.getEmailAddress());
                    intent.putExtra("credit",client.getCreditCardNum());

                    ((Activity)mContext).startActivity(intent);
                }

                if (actionMode!=null) {
                    actionMode.finish();
                }
                selectedPosition=-1;
            }
        });
        holder.firstName.setText(client.getName());
        holder.lastName.setText(client.getLastName());
        holder.id.setText(String.valueOf(client.getId()));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public Filter getFilter() {
        if(myFilter==null)myFilter=new MyFilter();
        return myFilter;
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
    public class MyActionModeCallbackClient implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.delete_item:{

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Delete Client");

                    builder.setMessage("are you sure?");

                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stubnhnbnhbghg
                            int objectsLengh=objects.size();
                            backEndFunc.deleteClient(objects.get(selectedPosition).getId());

                            if (objectsLengh==objects.size()) {
                                objects.remove(selectedPosition);
                            }
                            notifyDataSetChanged();
                            Toast.makeText(mContext,
                                    "client deleted", Toast.LENGTH_SHORT).show();

                            selectedPosition=-1;
                            notifyItemChanged(selectedPosition);
                            actionMode.finish();
                        }
                    });

                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            return true;
        }



        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectedPosition=-1;
            notifyItemChanged(selectedPosition);
            ((MainActivity)mContext).client_is_in_action_mode=false;
            notifyDataSetChanged();
        }

    }

    private class MyFilter extends Filter {
        FilterResults results;
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = ListDataSource.clientList;
                results.count = ListDataSource.clientList.size();
            }
            else
            {
                ArrayList<Client> filteredClients = new ArrayList<Client>();
                for (Client c : backEndFunc.getAllClients()) {

                    String s=(c.getName()+" "+c.getLastName()).toLowerCase();
                    if (s.contains( charSequence.toString().toLowerCase())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredClients.add(c);
                    }
                }
                results.values =filteredClients;
                results.count = filteredClients.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            objects=new ArrayList<Client>((ArrayList<Client>)results.values);

            notifyDataSetChanged();
        }
    }
}
