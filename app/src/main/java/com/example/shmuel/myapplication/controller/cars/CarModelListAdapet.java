package com.example.shmuel.myapplication.controller.cars;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;

/**
 * Created by User on 19/11/2017.
 */

class CarModelListAdapet extends RecyclerView.Adapter<CarModelListAdapet.ViewHolder> {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    BackEndFunc backEndForSql=FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    public ArrayList<CarModel> objects;
    private Context mContext;
    public int selectedPosition=-1;
    boolean clicked=true;
    private static RecyclerViewClickListener itemListener;
    ViewHolder viewHolder;




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
    public void onBindViewHolder(final CarModelListAdapet.ViewHolder holder, final int position) {
        final CarModel carModel = objects.get(position);
        viewHolder=holder;
        if(selectedPosition==position){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#a3a3a3"));
            holder.carModelName.setTextColor(Color.parseColor("#ffffff"));
            }
        else
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.carModelName.setTextColor(Color.parseColor("#000000"));

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
                return;
            }
        });




        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(carModel.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.default_car_image)
                .placeholder(R.drawable.default_car_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
        holder.carModelName.setText(carModel.getCompanyName()+" "+carModel.getCarModelName());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carModelName;
        ImageView imageView;
        CardView cardView;
        ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.carModelCardImage);
            carModelName=(TextView)itemView.findViewById(R.id.carModelCardCompany);
            cardView =(CardView)itemView.findViewById(R.id.card);
            progressBar=(ProgressBar)itemView.findViewById(R.id.downloadProgressBar);
        }
    }

}
