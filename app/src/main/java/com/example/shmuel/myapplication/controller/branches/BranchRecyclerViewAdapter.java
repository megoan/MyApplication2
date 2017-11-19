package com.example.shmuel.myapplication.controller.branches;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.Branch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by shmuel on 23/10/2017.
 */

public class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.ViewHolder> implements Filterable{
    private ArrayList<Branch> objects;
    private Context mContext;
    public ActionMode actionMode;
    private int selectedPosition=-1;
    MyFilter myFilter;
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Branch branch = objects.get(position);


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
                if (actionMode!=null) {
                    actionMode.finish();
                }
                selectedPosition=-1;
            }
        });
        
        int defaultImage = mContext.getResources().getIdentifier(branch.getImgURL(),null,mContext.getPackageName());

        Drawable drawable=ContextCompat.getDrawable(mContext, defaultImage);

        holder.branchCity.setText(branch.getAddress().getCity());
        holder.branchStreet.setText(branch.getAddress().getStreet());
        holder.branchAddressNumber.setText(branch.getAddress().getNumber());
        holder.revenue.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(branch.getBranchRevenue())));
        holder.numberOfCars.setText(String.valueOf(branch.getParkingSpotsNum()));
        holder.branchNumber.setText("#"+String.valueOf(branch.getBranchNum()));
        holder.imageView.setImageDrawable(drawable);

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
        TextView branchStreet;
        TextView branchAddressNumber;
        ImageView imageView;
        TextView revenue;
        TextView numberOfCars;
        TextView branchNumber;
        ImageButton inUse;

        public ViewHolder(View itemView) {
            super(itemView);
            branchCity=(TextView)itemView.findViewById(R.id.cardBranchCity);
            revenue=(TextView)itemView.findViewById(R.id.cardBranchRevenue);
            numberOfCars=(TextView)itemView.findViewById(R.id.cardBranchCarNum);
            branchStreet=(TextView)itemView.findViewById(R.id.cardBranchStreet);
            branchNumber=(TextView)itemView.findViewById(R.id.cardBranchNumber);
            branchAddressNumber=(TextView)itemView.findViewById(R.id.cardBranchAddressNumber);
            imageView=(ImageView)itemView.findViewById(R.id.imageView2);
            inUse=(ImageButton)itemView.findViewById(R.id.cardBranchInUse);

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
                                backEndFunc.deleteBranch(objects.get(selectedPosition).getBranchNum());
                                if (objects.size()==objectsLengh) {
                                    objects.remove(selectedPosition);
                                }
                                notifyDataSetChanged();
                                Toast.makeText(mContext,
                                        "branch deleted", Toast.LENGTH_SHORT).show();
                                int i=1;
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
            }
            return true;
        }



        @Override
        public void onDestroyActionMode(ActionMode mode) {
            int i=0;
            selectedPosition=-1;
            notifyItemChanged(selectedPosition);
            ((MainActivity)mContext).branch_is_in_action_mode=false;
            notifyDataSetChanged();
            i++;

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

                    String s=(branch.getAddress().getCity()+" "+branch.getAddress().getStreet()).toLowerCase();
                    if (s.contains( charSequence.toString().toLowerCase() )|| charSequence.toString().toLowerCase().contains((branch.getAddress().getCity().toLowerCase()))) {
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
}
