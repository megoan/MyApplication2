package com.example.shmuel.myapplication.controller.branches;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by shmuel on 23/10/2017.
 */

public class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.ViewHolder> implements Filterable{
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    BackEndFunc backEndForSql=FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);

    public ArrayList<Branch> objects;
    private Context mContext;
    public ActionMode actionMode;
    private int selectedPosition=-1;
    MyFilter myFilter;
    ViewHolder viewHolder2;
    private ProgressDialog progDailog;

    Branch branch;

    public BranchRecyclerViewAdapter(ArrayList<Branch> objects, Context context) {
        this.objects=objects;
        this.mContext=context;
    }

    public void removeitem(int position) {
        objects.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.branch_card_layout, parent, false);
        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        branch=objects.get(position);
        viewHolder2=holder;

        if(selectedPosition==position){
            if(((MainActivity)mContext).branch_is_in_action_mode==true){
                holder.itemView.setBackgroundColor(Color.parseColor("#a3a3a3"));
                if(!branch.isInUse())
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
            if(!branch.isInUse())
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
                MyActionModeCallbackBranch callback=new MyActionModeCallbackBranch();
                actionMode=((Activity)mContext).startActionMode(callback);
                actionMode.setTitle("delete branch");
                selectedPosition=position;
                ((MainActivity)mContext).branch_is_in_action_mode=true;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                if(((MainActivity)mContext).branch_is_in_action_mode==false){
                    Intent intent=new Intent(mContext,BranchActivity.class);
                    Branch branch1=objects.get(position);
                    intent.putExtra("country",branch1.getMyAddress().getCountry());
                    intent.putExtra("addressName",branch1.getMyAddress().getAddressName());
                    intent.putExtra("latitude",branch1.getMyAddress().getLatitude());
                    intent.putExtra("longitude",branch1.getMyAddress().getLongitude());
                    intent.putExtra("id",branch1.getBranchNum());
                    intent.putExtra("established",branch1.getEstablishedDate().toString());
                    intent.putExtra("parkingSpotsNum",branch1.getParkingSpotsNum());
                    intent.putExtra("numOfCars",branch1.getCarIds().size());
                    intent.putExtra("available",branch1.numberOfParkingSpotsAvailable());
                    intent.putExtra("imgUrl",branch1.getImgURL());
                    intent.putExtra("inUse",branch1.isInUse());
                    intent.putExtra("revenue",branch1.getBranchRevenue());
                    intent.putExtra("country",branch1.getMyAddress().getCountry());
                    intent.putExtra("year",branch1.getEstablishedDate().getYear());
                    intent.putExtra("month",branch1.getEstablishedDate().getMonth());
                    intent.putExtra("day",branch1.getEstablishedDate().getDay());
                    intent.putExtra("carList",branch1.getCarIds());
                    mContext.startActivity(intent);
                }
                if (actionMode!=null) {
                    actionMode.finish();
                }
                selectedPosition=-1;
            }
        });

        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(branch.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
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

        holder.branchCity.setText(branch.getMyAddress().getAddressName());
        holder.revenue.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(branch.getBranchRevenue())));
        holder.numberOfCars.setText(String.valueOf(branch.getCarIds().size()));
        holder.branchNumber.setText("#"+String.valueOf(branch.getBranchNum()));




        if(!branch.isInUse())
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

    @Override
    public Filter getFilter() {
        if(myFilter==null)myFilter=new MyFilter();
        return myFilter;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchCity;
        ImageView imageView;
        TextView revenue;
        TextView numberOfCars;
        TextView branchNumber;
        ImageButton inUse;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            branchCity=(TextView)itemView.findViewById(R.id.cardBranchCity);
            revenue=(TextView)itemView.findViewById(R.id.cardBranchRevenue);
            numberOfCars=(TextView)itemView.findViewById(R.id.cardBranchCarNum);
            branchNumber=(TextView)itemView.findViewById(R.id.cardBranchNumber);
            imageView=(ImageView)itemView.findViewById(R.id.imageView2);
            inUse=(ImageButton)itemView.findViewById(R.id.cardBranchInUse);
            progressBar=itemView.findViewById(R.id.downloadProgressBar);

        }
    }

    public class MyActionModeCallbackBranch implements ActionMode.Callback{

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
                    if(selectedPosition>-1 && objects.get(selectedPosition).isInUse()){
                        Toast.makeText(mContext,
                                "cannot delete branch, branch is in use!!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        builder.setTitle("Delete Branch");

                        builder.setMessage("are you sure?");

                        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                int objectsLengh=objects.size();
                                new BackGroundDeleteBranch().execute();

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
            }
            return true;
        }



        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectedPosition=-1;
            notifyItemChanged(selectedPosition);
            ((MainActivity)mContext).branch_is_in_action_mode=false;
            notifyDataSetChanged();
        }

    }

    private class MyFilter extends Filter {
        FilterResults results;
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = ListDataSource.branchList;
                results.count = ListDataSource.branchList.size();
            }
            else
            {
                ArrayList<Branch> filteredBranches = new ArrayList<Branch>();
                for (Branch branch : backEndFunc.getAllBranches()) {

                    String s=(branch.getMyAddress().getCountry());
                    if (s.contains( charSequence.toString().toLowerCase() )|| charSequence.toString().toLowerCase().contains((branch.getMyAddress().getCountry().toLowerCase()))) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredBranches.add(branch);
                    }
                }
                results.values = filteredBranches;
                results.count = filteredBranches.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            objects=new ArrayList<Branch>((ArrayList<Branch>)results.values);

            notifyDataSetChanged();
        }
    }
    public class BackGroundDeleteBranch extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(mContext);
            progDailog.setMessage("Updating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseStorage mFirebaseStorage=FirebaseStorage.getInstance();

            Branch branch = objects.get(selectedPosition);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef;
            imageRef = storageRef.child("branch"+"/"+branch.getBranchNum()+".jpg");
            backEndForSql.deleteCarModel(branch.getBranchNum());
            imageRef.delete();
            MySqlDataSource.branchList=backEndForSql.getAllBranches();
            int objectsLengh = objects.size();
            if (objectsLengh == objects.size()) {
                objects.remove(selectedPosition);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            selectedPosition=-1;
            notifyItemChanged(selectedPosition);
            notifyDataSetChanged();
            TabFragments.branchesTab.updateView();
            Toast.makeText(mContext,
                    "branch deleted from source", Toast.LENGTH_SHORT).show();
            actionMode.finish();
            progDailog.dismiss();


        }
    }

}
