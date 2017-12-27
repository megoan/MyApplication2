package com.example.shmuel.myapplication.controller.cars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.carmodels.CarModelActivity;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.CarModelImage;

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
    CarModelImage carModelImage=new CarModelImage();



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
        carModelImage.set_carModelID(carModel.getCarModelCode());
        viewHolder=holder;
        new DownloadImage().execute();
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

            }
        });





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

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.carModelCardImage);
            carModelName=(TextView)itemView.findViewById(R.id.carModelCardCompany);
            cardView =(CardView)itemView.findViewById(R.id.card);
        }
    }
    public class DownloadImage extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            carModelImage=backEndForSql.getCarModelImage(carModelImage.get_carModelID());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (carModelImage.getImgURL()==null|| carModelImage.getImgURL().equals("@drawable/default_car_image")) {
                int defaultImage = mContext.getResources().getIdentifier("@drawable/default_car_image", null, mContext.getPackageName());
                Drawable drawable = ContextCompat.getDrawable(mContext, defaultImage);
                viewHolder.imageView.setImageDrawable(drawable);
            } else {
                byte[] imageBytes= Base64.decode(carModelImage.getImgURL(),Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                viewHolder.imageView.setImageBitmap(bitmap);
            }
        }
    }
}
