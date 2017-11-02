package com.example.shmuel.myapplication.cars;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.entities.Address;
import com.example.shmuel.myapplication.model.entities.Car;
import com.example.shmuel.myapplication.model.entities.CarModel;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.ViewHolder> {
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    private ArrayList<Car> objects;
    private Context mContext;

    public CarRecyclerViewAdapter(ArrayList<Car> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    @Override
    public CarRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_card_layout, parent, false);
        return new CarRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarRecyclerViewAdapter.ViewHolder holder, int position) {
        Car car = objects.get(position);
        Address carAddress=backEndFunc.getBranch(car.getBranchNum()).getAddress();
        CarModel carModel=backEndFunc.getCarModel(car.getCarModel());

        int defaultImage = mContext.getResources().getIdentifier(car.getImgURL(),null,mContext.getPackageName());
        Drawable drawable= ContextCompat.getDrawable(mContext, defaultImage);

        holder.carNumber.setText("#"+String.valueOf(car.getCarNum()));
        holder.carYear.setText(String.valueOf(car.getYear()));
        holder.city.setText(carAddress.getCity());
        holder.carModel.setText(carModel.getCarModelName());
        holder.companyName.setText(carModel.getCompanyName());
        holder.imageView.setImageDrawable(drawable);
        holder.street.setText(carAddress.getStreet());

        holder.milage.setText(String.valueOf(car.getMileage()));
        holder.dailyPrice.setText("USD "+String.valueOf(car.getOneDayCost()));
        holder.milePrice.setText("USD "+String.valueOf(car.getOneKilometerCost()));

        holder.ratingBar.setRating((float) car.getRating());
        holder.rating.setText(String.valueOf(car.getRating()));
        holder.numberOfRatings.setText("("+String.valueOf(car.getNumOfRatings())+")");

       if(!car.isInUse())
       {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
           } else {
               holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
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
        TextView carNumber;
        TextView carYear;
        TextView city;
        TextView street;
        TextView milage;
        TextView dailyPrice;
        TextView milePrice;
        ImageView imageView;
        RatingBar ratingBar;
        TextView rating;
        TextView numberOfRatings;
        ImageButton inUse;

        public ViewHolder(View itemView) {
            super(itemView);
            companyName=(TextView)itemView.findViewById(R.id.carModelCardCompany);
            carModel=(TextView)itemView.findViewById(R.id.carModelCardModelName);
            carNumber=(TextView)itemView.findViewById(R.id.carCardId);
            imageView=(ImageView)itemView.findViewById(R.id.carModelCardImage);
            carYear=(TextView)itemView.findViewById(R.id.carCardYear);
            city=(TextView)itemView.findViewById(R.id.carCardCity);
            street=(TextView)itemView.findViewById(R.id.carCardStreet);
            milage=(TextView)itemView.findViewById(R.id.carCardMilage);
            dailyPrice=(TextView)itemView.findViewById(R.id.carCardDayPrice);
            milePrice=(TextView)itemView.findViewById(R.id.carCardMilePrice);

            ratingBar=(RatingBar)itemView.findViewById(R.id.carCardratingBar);
            rating=(TextView)itemView.findViewById(R.id.carCardRating);
            numberOfRatings=(TextView)itemView.findViewById(R.id.carCardNumberOfRatings);

            inUse=(ImageButton)itemView.findViewById(R.id.carCardInUdeButton);
        }
    }
}
