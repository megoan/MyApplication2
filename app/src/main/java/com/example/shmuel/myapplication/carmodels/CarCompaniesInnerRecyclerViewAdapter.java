package com.example.shmuel.myapplication.carmodels;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarCompaniesInnerRecyclerViewAdapter extends RecyclerView.Adapter<CarCompaniesInnerRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CarModel> objects;
    private Context mContext;

    public CarCompaniesInnerRecyclerViewAdapter(ArrayList<CarModel> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    @Override
    public CarCompaniesInnerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_model_card_layout, parent, false);
        return new CarCompaniesInnerRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarCompaniesInnerRecyclerViewAdapter.ViewHolder holder, int position) {
        CarModel carModel = objects.get(position);

        int defaultImage = mContext.getResources().getIdentifier(carModel.getImgURL(),null,mContext.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(mContext, defaultImage);
        holder.carModel.setText(carModel.getCarModelName());
        holder.companyName.setText(carModel.getCompanyName());
        holder.imageView.setImageDrawable(drawable);
        holder.passengers.setText("+"+String.valueOf(carModel.getPassengers()));
        holder.luggage.setText("+"+String.valueOf(carModel.getLuggage()));

        if(!carModel.isInUse())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
            } else {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
            }
        }
        if(carModel.getTransmission()== Transmission.MANUAL)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_manual, mContext.getTheme()));
            } else {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_manual));
            }
        }
        if(carModel.isAc()==false)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ac.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            } else {
                holder.ac.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }



    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        TextView carModel;
        ImageView imageView;
        TextView passengers;
        TextView luggage;
        ImageButton ac;
        ImageButton auto;
        ImageButton inUse;



        public ViewHolder(View itemView) {
            super(itemView);
            companyName=(TextView)itemView.findViewById(R.id.carModelCardCompany);
            carModel=(TextView)itemView.findViewById(R.id.carModelCardModelName);
            imageView=(ImageView)itemView.findViewById(R.id.carModelCardImage);
            passengers=(TextView)itemView.findViewById(R.id.badge_notification_1);
            luggage=(TextView)itemView.findViewById(R.id.badge_notification_2);
            ac=(ImageButton)itemView.findViewById(R.id.carModelCardAc);
            auto=(ImageButton)itemView.findViewById(R.id.carModelCardAuto);
            inUse=(ImageButton)itemView.findViewById(R.id.carModelCardInUseButton);
        }
    }
}
