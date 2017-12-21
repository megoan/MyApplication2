package com.example.shmuel.myapplication.controller.carmodels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shmuel.myapplication.controller.MainActivity;
import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.controller.TabFragments;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.backend.SelectedDataSource;
import com.example.shmuel.myapplication.model.datasource.ListDataSource;
import com.example.shmuel.myapplication.model.entities.CarModel;
import com.example.shmuel.myapplication.model.entities.Transmission;

import java.util.ArrayList;

/**
 * Created by shmuel on 23/10/2017.
 */

public class CarCompaniesInnerRecyclerViewAdapter extends RecyclerView.Adapter<CarCompaniesInnerRecyclerViewAdapter.ViewHolder> implements Filterable {
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(SelectedDataSource.dataSourceType);
    public ArrayList<CarModel> objects;
    private Context mContext;
    public ActionMode actionMode;
    private int selectedPosition = -1;
    private MyFilter myFilter;
    private ProgressDialog progDailog;
    CarModel carModel;


    public CarCompaniesInnerRecyclerViewAdapter(ArrayList<CarModel> objects, Context context) {
        this.objects = objects;
        this.mContext = context;
    }

    public void removeitem(int position) {
        objects.remove(position);
    }

    @Override
    public CarCompaniesInnerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_model_card_layout, parent, false);
        return new CarCompaniesInnerRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarCompaniesInnerRecyclerViewAdapter.ViewHolder holder, final int position) {
        carModel = objects.get(position);


        if (selectedPosition == position) {
            if (((MainActivity) mContext).car_model_is_in_action_mode == true) {
                holder.itemView.setBackgroundColor(Color.parseColor("#a3a3a3"));
                if (!carModel.isInUse()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
                    } else {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use, mContext.getTheme()));
                    } else {
                        holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use));
                    }
                }
            }
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            if (!carModel.isInUse()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
                } else {
                    holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
                }
            } else {
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
                MyActionModeCallbackCarModel callback = new MyActionModeCallbackCarModel();
                actionMode = ((Activity) mContext).startActionMode(callback);
                actionMode.setTitle("delete car model");
                selectedPosition = position;
                ((MainActivity) mContext).car_model_is_in_action_mode = true;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                if (((MainActivity) mContext).car_model_is_in_action_mode == false) {
                    Intent intent = new Intent(mContext, CarModelActivity.class);
                    //CarModel carModel = objects.get(position);
                    intent.putExtra("companyName", carModel.getCompanyName());
                    intent.putExtra("modelName", carModel.getCarModelName());
                    intent.putExtra("id", carModel.getCarModelCode());
                    intent.putExtra("engine", carModel.getEngineDisplacement());
                    intent.putExtra("transmission", carModel.getTransmission());
                    intent.putExtra("passengers", carModel.getPassengers());
                    intent.putExtra("luggage", carModel.getLuggage());
                    intent.putExtra("ac", carModel.isAc());
                    intent.putExtra("imgUrl", carModel.getImgURL());
                    intent.putExtra("inUse", carModel.isInUse());
                    intent.putExtra("position", position);


                    ((Activity) mContext).startActivity(intent);
                }
                if (actionMode != null) {
                    actionMode.finish();
                }
                selectedPosition = -1;
            }
        });



        if (carModel.getImgURL().equals("@drawable/default_car_image")) {
            int defaultImage = mContext.getResources().getIdentifier("@drawable/default_car_image", null, mContext.getPackageName());
            Drawable drawable = ContextCompat.getDrawable(mContext, defaultImage);
            holder.imageView.setImageDrawable(drawable);
        } else {
            byte[] imageBytes= Base64.decode(carModel.getImgURL(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imageView.setImageBitmap(bitmap);
        }


        holder.carModel.setText(carModel.getCarModelName());
        holder.companyName.setText(carModel.getCompanyName());

        holder.passengers.setText("+" + String.valueOf(carModel.getPassengers()));
        holder.luggage.setText("+" + String.valueOf(carModel.getLuggage()));

        if (!carModel.isInUse()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use, mContext.getTheme()));
            } else {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_not_in_use));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use, mContext.getTheme()));
            } else {
                holder.inUse.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_in_use));
            }
        }
        if (carModel.getTransmission() == Transmission.MANUAL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_manual, mContext.getTheme()));
            } else {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_manual));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_auto, mContext.getTheme()));
            } else {
                holder.auto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_auto));
            }
        }
        if (carModel.isAc() == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ac.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            } else {
                holder.ac.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ac.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ac, mContext.getTheme()));
            } else {
                holder.ac.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ac));
            }
        }
    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) myFilter = new MyFilter();
        return myFilter;
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
            companyName = (TextView) itemView.findViewById(R.id.carModelCardCompany);
            carModel = (TextView) itemView.findViewById(R.id.carModelCardModelName);
            imageView = (ImageView) itemView.findViewById(R.id.carModelCardImage);
            passengers = (TextView) itemView.findViewById(R.id.badge_notification_1);
            luggage = (TextView) itemView.findViewById(R.id.badge_notification_2);
            ac = (ImageButton) itemView.findViewById(R.id.carModelCardAc);
            auto = (ImageButton) itemView.findViewById(R.id.carModelCardAuto);
            inUse = (ImageButton) itemView.findViewById(R.id.carModelCardInUseButton);
        }
    }

    public class MyActionModeCallbackCarModel implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_item: {
                    if (selectedPosition > -1 && objects.get(selectedPosition).isInUse()) {
                        Toast.makeText(mContext,
                                "cannot delete car model, car model in use", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        builder.setTitle("Delete Car Model");

                        builder.setMessage("are you sure?");

                        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                new BackGroundDeleteCarModel().execute();
                               /* int objectsLengh=objects.size();
                                backEndFunc.deleteCarModel(objects.get(selectedPosition).getCarModelCode());
                                if (objectsLengh==objects.size()) {
                                    objects.remove(selectedPosition);
                                }
                                notifyDataSetChanged();
                                Toast.makeText(mContext,
                                        "car model deleted", Toast.LENGTH_SHORT).show();

                                selectedPosition=-1;
                                notifyItemChanged(selectedPosition);
                                actionMode.finish();*/
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
            selectedPosition = -1;
            notifyItemChanged(selectedPosition);
            ((MainActivity) mContext).car_model_is_in_action_mode = false;
            notifyDataSetChanged();
        }

    }

    private class MyFilter extends Filter {
        FilterResults results;

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = ListDataSource.carModelList;
                results.count = ListDataSource.carModelList.size();
            } else {
                ArrayList<CarModel> filteredCars = new ArrayList<CarModel>();
                for (CarModel carModel : backEndFunc.getAllCarModels()) {

                    String s = (carModel.getCompanyName() + " " + carModel.getCarModelName());
                    if (s.contains(charSequence.toString().toLowerCase()) || charSequence.toString().toLowerCase().contains(carModel.getCompanyName().toLowerCase())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredCars.add(carModel);
                    }
                }
                results.values = filteredCars;
                results.count = filteredCars.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            objects = new ArrayList<CarModel>((ArrayList<CarModel>) results.values);

            notifyDataSetChanged();
        }
    }

    public class BackGroundDeleteCarModel extends AsyncTask<Void, Void, Void> {

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
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CarModel carModel = objects.get(selectedPosition);
            backEndFunc.deleteCarModel(carModel.getCarModelCode());
            int objectsLengh = objects.size();
            if (objectsLengh == objects.size()) {
                objects.remove(selectedPosition);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int i = 1;
            selectedPosition = -1;
            notifyItemChanged(selectedPosition);
            notifyDataSetChanged();

            TabFragments.carModelsTab.updateView();
            Toast.makeText(mContext,
                    "car model deleted from source", Toast.LENGTH_SHORT).show();
            actionMode.finish();
            progDailog.dismiss();


        }
    }
}
