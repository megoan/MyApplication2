package com.example.shmuel.myapplication.branches;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.entities.Branch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by shmuel on 23/10/2017.
 */

public class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Branch> objects;
    private Context mContext;

    public BranchRecyclerViewAdapter(ArrayList<Branch> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.branch_card_layout, parent, false);
        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Branch branch = objects.get(position);

        int defaultImage = mContext.getResources().getIdentifier(branch.getImgURL(),null,mContext.getPackageName());

        Drawable drawable=ContextCompat.getDrawable(mContext, defaultImage);

        holder.branchCity.setText(branch.getAddress().getCity());
        holder.branchStreet.setText(branch.getAddress().getStreet());
        holder.branchAddressNumber.setText(branch.getAddress().getNumber());
        holder.revenue.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(branch.getBranchRevenue())));
        holder.numberOfCars.setText(String.valueOf(branch.getParkingSpotsNum()));
        holder.establishedDate.setText(branch.getEstablishedDate().toString());
        holder.branchNumber.setText("#"+String.valueOf(branch.getBranchNum()));
        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchCity;
        TextView branchStreet;
        TextView branchAddressNumber;
        ImageView imageView;
        TextView revenue;
        TextView numberOfCars;
        TextView establishedDate;
        TextView branchNumber;


        public ViewHolder(View itemView) {
            super(itemView);
            branchCity=(TextView)itemView.findViewById(R.id.cardBranchCity);
            revenue=(TextView)itemView.findViewById(R.id.cardBranchRevenue);
            numberOfCars=(TextView)itemView.findViewById(R.id.cardBranchCarNum);
            branchStreet=(TextView)itemView.findViewById(R.id.cardBranchStreet);
            branchNumber=(TextView)itemView.findViewById(R.id.cardBranchNumber);
            establishedDate=(TextView)itemView.findViewById(R.id.cardBranchDate);
            branchAddressNumber=(TextView)itemView.findViewById(R.id.cardBranchAddressNumber);
            imageView=(ImageView)itemView.findViewById(R.id.imageView2);

        }
    }
}
