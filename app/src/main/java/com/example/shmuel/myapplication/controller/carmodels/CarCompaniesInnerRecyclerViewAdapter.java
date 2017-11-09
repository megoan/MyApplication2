package com.example.shmuel.myapplication.controller.carmodels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarCompaniesInnerRecyclerViewAdapter extends RecyclerView.Adapter<CarCompaniesInnerRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CarModel> objects;
    private Context mContext;
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    public ActionMode actionMode;
    private int selectedPosition=-1;


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
    public void onBindViewHolder(CarCompaniesInnerRecyclerViewAdapter.ViewHolder holder,final int position) {
        CarModel carModel = objects.get(position);


        if(selectedPosition==position){
            if(((MainActivity)mContext).car_model_is_in_action_mode==true){
                holder.itemView.setBackgroundColor(Color.parseColor("#a3a3a3"));
                if(!carModel.isInUse())
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
                    } else {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
                    }
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use, mContext.getTheme()));
                    } else {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use));
                    }
                }
            }
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            if(!carModel.isInUse())
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
                } else {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
                }
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use, mContext.getTheme()));
                } else {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use));
                }
            }
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CarCompaniesInnerRecyclerViewAdapter.MyActionModeCallbackCarModel callback=new CarCompaniesInnerRecyclerViewAdapter.MyActionModeCallbackCarModel();
                actionMode=((Activity)mContext).startActionMode(callback);
                actionMode.setTitle("delete car model");
                Toast.makeText(mContext,
                        "long click", Toast.LENGTH_SHORT).show();
                selectedPosition=position;
                ((MainActivity)mContext).car_model_is_in_action_mode=true;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                if (actionMode!=null) {
                    actionMode.finish();
                }
                selectedPosition=-1;
            }
        });


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

    public class MyActionModeCallbackCarModel implements ActionMode.Callback{

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
                    if(selectedPosition>-1 && objects.get(selectedPosition).isInUse())
                    {
                        Toast.makeText(mContext,
                                "cannot delete car model, car model in use", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                   else{
                        backEndFunc.deleteCarModel(objects.get(selectedPosition).getCarModelCode());
                        notifyDataSetChanged();

                        selectedPosition=-1;
                        notifyItemChanged(selectedPosition);
                        actionMode.finish();
                        Toast.makeText(mContext,
                                "car model deleted", Toast.LENGTH_SHORT).show();
                    }

                }
                case android.R.id.closeButton:
                {
                    selectedPosition=-1;
                }
            }
            return true;
        }



        @Override
        public void onDestroyActionMode(ActionMode mode) {
            int i=0;
            selectedPosition=-1;
            notifyItemChanged(selectedPosition);
            ((MainActivity)mContext).car_model_is_in_action_mode=false;
            notifyDataSetChanged();
            i++;

        }

    }
}
