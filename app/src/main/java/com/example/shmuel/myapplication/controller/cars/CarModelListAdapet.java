package com.example.shmuel.myapplication.controller.cars;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;

/**
 * Created by User on 19/11/2017.
 */

class CarModelListAdapet extends RecyclerView.Adapter<CarModelListAdapet.ViewHolder> {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ArrayList<CarModel> objects;
    private Context mContext;
    public int selectedPosition=-1;
    boolean clicked=true;
    private static RecyclerViewClickListener itemListener;





    public CarModelListAdapet(ArrayList<CarModel> objects, Context context,RecyclerViewClickListener itemListener) {
        this.objects=objects;
        this.mContext=context;
        this.itemListener=itemListener;
    }

    @Override
    public CarModelListAdapet.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carmodel_for_list_layout, parent, false);
        return new CarModelListAdapet.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarModelListAdapet.ViewHolder holder, final int position) {
        final CarModel carModel = objects.get(position);
        if(selectedPosition==position){
            holder.itemView.setBackgroundColor(Color.parseColor("#a3a3a3"));
            }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        }
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedPosition=position;
                itemListener.recyclerViewListClicked(v, position,true);
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=-1;
                itemListener.recyclerViewListClicked(v, position,false);
                notifyDataSetChanged();

            }
        });



        int defaultImage = mContext.getResources().getIdentifier(carModel.getImgURL(),null,mContext.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(mContext, defaultImage);
        holder.imageView.setImageDrawable(drawable);

        holder.carModelName.setText(carModel.getCompanyName()+" "+carModel.getCarModelName());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carModelName;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.carModelCardImage);
            carModelName=(TextView)itemView.findViewById(R.id.carModelCardCompany);
        }
    }
}
