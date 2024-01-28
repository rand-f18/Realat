package com.example.FinanceManagementProject;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class thirdActivitypt1 extends AppCompatActivity {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;
    String category;
    String period;
    String monthm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_pt1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializing variable for bar chart.
        barChart = findViewById(R.id.idBarChart);
        // calling method to get bar entries.

        XAxis xAxis = barChart.getXAxis();


        Intent i = getIntent();
        category = i.getStringExtra("category");
        period = i.getStringExtra("period");
        monthm = i.getStringExtra("month");
        setTitle("Statistics: "+category+" - "+period);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, period, Toast.LENGTH_SHORT).show();
        getBarEntries();

        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, category+" in "+period+" basis" );
        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        // setting text color.qq
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        barChart.animateY(3000);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBarEntries() {

        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("January", 1);
        map.put("February", 2);
        map.put("March", 3);
        map.put("April", 4);
        map.put("May", 5);
        map.put("June", 6);
        map.put("July", 7);
        map.put("August", 8);
        map.put("September", 9);
        map.put("October", 10);
        map.put("November", 11);
        map.put("December", 12);
        DataBaseHelper db = new DataBaseHelper(this);
        Cursor res = db.getData();

        if (period.equalsIgnoreCase("Daily")) {
            double[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String type = res.getString(res.getColumnIndex("Category"));
                if (type.equalsIgnoreCase(category)) {
                    double Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                    String date = res.getString(res.getColumnIndex("Date"));
                    String day = date.substring(0, 2);
                    String month = date.substring(3, 5);
                    int d = Integer.parseInt(day);
                    int m = Integer.parseInt(month);
                    String year = date.substring(6, 8);
                    int y = Integer.parseInt(year);
                    if(y==23) {
                        if ((m == map.get(monthm))) {
                            for (int i = 0; i < arr.length; i++) {
                                if (d == i + 1) {
                                    arr[i] += Amount1;
                                }
                            }
                        }
                    }
                }
                res.moveToNext();
            }

            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != 0) {
                    barEntriesArrayList.add(new BarEntry(i + 1, (float) arr[i]));
                }
            }
        } else {
            if (period.equalsIgnoreCase("Monthly")) {
                double Amount2 = 0;
                double[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Category"));
                    if (type.equalsIgnoreCase(category)) {
                        Amount2 = res.getDouble(res.getColumnIndex("Amount"));
                        String date = res.getString(res.getColumnIndex("Date"));
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        if (y == 23) {
                            for (int i = 0; i < arr.length; i++) {
                                if (m == i + 1) {
                                    arr[i] += Amount2;
                                }
                            }
                        }
                    }
                    res.moveToNext();
                }
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != 0) {
                        barEntriesArrayList.add(new BarEntry(i + 1, (float) arr[i]));
                    }
                }
            }
        }
    }
        }

