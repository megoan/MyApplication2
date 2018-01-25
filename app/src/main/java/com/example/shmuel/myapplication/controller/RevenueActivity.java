package com.example.shmuel.myapplication.controller;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shmuel.myapplication.R;
import com.example.shmuel.myapplication.model.backend.BackEndFunc;
import com.example.shmuel.myapplication.model.backend.DataSourceType;
import com.example.shmuel.myapplication.model.backend.FactoryMethod;
import com.example.shmuel.myapplication.model.datasource.MySqlDataSource;
import com.example.shmuel.myapplication.model.entities.Order;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Collections;
import java.util.Comparator;

public class RevenueActivity extends AppCompatActivity {
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        Collections.sort(MySqlDataSource.orderList, new Comparator<Order>(){
            public int compare(Order o1, Order o2){

                return new Integer(o1.getOrderNumber()).compareTo(new Integer(o2.getOrderNumber()));
            }
        });

        int i=0;
        for(Order order: MySqlDataSource.orderList)
        {
           i++;
            series.appendData(new DataPoint(i,order.getPayment()),false,10000);
        }
        Collections.sort(MySqlDataSource.orderList, new Comparator<Order>(){
            public int compare(Order o1, Order o2){

                return new Double(o1.getPayment()).compareTo(new Double(o2.getPayment()));
            }
        });
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);
        graph.addSeries(series);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(MySqlDataSource.orderList.size());
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(MySqlDataSource.orderList.get((MySqlDataSource.orderList.size()-1)).getPayment());

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " $";
                }
            }
        });
    }


}
