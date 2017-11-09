package com.example.shmuel.myapplication.controller.carmodels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.entities.Car;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarCompaniesOuterRecyclerViewAdapter extends RecyclerView.Adapter<CarCompaniesOuterRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Car> objects;
    private Context mContext;

    public CarCompaniesOuterRecyclerViewAdapter(ArrayList<Car> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    @Override
    public CarCompaniesOuterRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_model_item_outer_layout, parent, false);
        return new CarCompaniesOuterRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarCompaniesOuterRecyclerViewAdapter.ViewHolder holder, int position) {
        Car car = objects.get(position);
        int defaultImage = mContext.getResources().getIdentifier(car.getImgURL(),null,mContext.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(mContext, defaultImage);
        //holder.companyName.setText(car.getCarModel().getCompanyName());
        holder.imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            companyName=(TextView)itemView.findViewById(R.id.carCompanyName);
            imageView=(ImageView)itemView.findViewById(R.id.carCompanyLogo);
        }
    }
}
