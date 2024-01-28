package com.example.FinanceManagementProject;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class thirdActivitypt2 extends AppCompatActivity {


    // variable for our bar chart
    BarChart barChart;
    PieChart pieChart;

    // variable for our bar data.
    BarData barData;
    PieData pieData;

    // variable for our bar data set.
    BarDataSet barDataSet;
    PieDataSet pieDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;
    ArrayList pieEntriesArrayList;
    String category;
    String period;
    String monthm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_pt2);
        Intent i = getIntent();
        category = i.getStringExtra("category");
        period = i.getStringExtra("period");
        monthm = i.getStringExtra("month");
        setTitle("Statistics: " + category + " - " + period);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, period, Toast.LENGTH_SHORT).show();
        pieChart = findViewById(R.id.idPieChart);
        // calling method to get bar entries.

        getPieEntries();
        pieDataSet = new PieDataSet(pieEntriesArrayList, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(5000, 5000);


        barChart = findViewById(R.id.idBarChart);
        getBarEntries();
        barDataSet = new BarDataSet(barEntriesArrayList, category+" in "+period+" basis" );
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        XAxis xAxis = barChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);


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
        if (category.equalsIgnoreCase(("All Incomes"))) {

            if (period.equalsIgnoreCase("Daily")) {
                double[] arrS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                double[] arrI = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("income")) {
                        String category = res.getString(res.getColumnIndex("Category"));
                        double Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                        String date = res.getString(res.getColumnIndex("Date"));
                        String day = date.substring(0, 2);
                        String month = date.substring(3, 5);
                        int d = Integer.parseInt(day);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                if (category.equalsIgnoreCase("salary")) {
                                    for (int i = 0; i < arrS.length; i++) {
                                        if (d == i + 1) {
                                            arrS[i] += Amount1;
                                        }
                                    }
                                } else if (category.equalsIgnoreCase("investments")) {
                                    for (int i = 0; i < arrI.length; i++) {
                                        if (d == i + 1) {
                                            arrI[i] += Amount1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    res.moveToNext();
                }
                for (int i = 0; i < arrS.length; i++) {
                    if (arrS[i] != 0) {
                        barEntriesArrayList.add(new BarEntry(i + 1, (float) arrS[i]));
                    }
                }
                for (int i = 0; i < arrI.length; i++) {
                    if (arrI[i] != 0) {
                        barEntriesArrayList.add(new BarEntry(i + 1, (float) arrI[i]));
                    }
                }
            }
            if (period.equalsIgnoreCase("Monthly")) {
                double Amount2 = 0;
                double[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("income")) {
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
        if (category.equalsIgnoreCase(("All Outcomes"))) {

            if (period.equalsIgnoreCase("Daily")) {
                double[] arrP = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                double[] arrT = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("outcome")) {
                        String category = res.getString(res.getColumnIndex("Category"));
                        double Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                        String date = res.getString(res.getColumnIndex("Date"));
                        String day = date.substring(0, 2);
                        String month = date.substring(3, 5);
                        int d = Integer.parseInt(day);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                if (category.equalsIgnoreCase("purchases")) {
                                    for (int i = 0; i < arrP.length; i++) {
                                        if (d == i + 1) {
                                            arrP[i] += Amount1;
                                        }
                                    }
                                } else if (category.equalsIgnoreCase("transfers")) {
                                    for (int i = 0; i < arrT.length; i++) {
                                        if (d == i + 1) {
                                            arrT[i] += Amount1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    res.moveToNext();
                }
                for (int i = 0; i < arrP.length; i++) {
                    if (arrP[i] != 0) {
                        barEntriesArrayList.add(new BarEntry(i + 1, (float) arrP[i]));
                    }
                }
                for (int i = 0; i < arrT.length; i++) {
                    if (arrT[i] != 0) {
                        barEntriesArrayList.add(new BarEntry(i + 1, (float) arrT[i]));
                    }
                }
            }
            if (period.equalsIgnoreCase("Monthly")) {
                double Amount2 = 0;
                double[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("outcome")) {
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

    private void getPieEntries() {
        // creating a new array list
        pieEntriesArrayList = new ArrayList<>();
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
        if (category.equalsIgnoreCase(("All Incomes"))) {
            double Amount1 = 0;
            double incomesD = 0;
            double incomesM = 0;
            double salaryD=0;
            double investD=0;
            if (period.equalsIgnoreCase("Daily")) {

                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    String category = res.getString(res.getColumnIndex("Category"));
                    if (type.equalsIgnoreCase("income")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                if (category.equalsIgnoreCase("salary")) {
                                    Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                    incomesD += Amount1;
                                    salaryD += Amount1;
                                } else if (category.equalsIgnoreCase("investments")) {
                                    Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                    incomesD += Amount1;
                                    investD += Amount1;
                                }
                            }
                        }
                    }
                    res.moveToNext();
                }
//
                double percentS = salaryD / incomesD * 100;
                double percentI = investD / incomesD * 100;
                percentS = Math.round(percentS);
                percentI = Math.round(percentI);
                pieEntriesArrayList.add(new PieEntry((float) salaryD, "Salary\n" + (int) percentS + "%"));
                pieEntriesArrayList.add(new PieEntry((float) investD, "Investments\n" + (int) percentI + "%"));
            }
            if (period.equalsIgnoreCase("Monthly")) {
                double a1 = 0, a2 = 0, a3 = 0, a4 = 0, a5 = 0, a6 = 0, a7 = 0, a8 = 0, a9 = 0, a10 = 0, a11 = 0, a12 = 0;
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("income")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String year = date.substring(6, 8);
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        int y = Integer.parseInt(year);
                        Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                        if (y == 23) {
                            incomesM += Amount1;
                            switch (m) {
                                case 1:
                                    a1 += Amount1;
                                    break;
                                case 2:
                                    a2 += Amount1;
                                    break;
                                case 3:
                                    a3 += Amount1;
                                    break;
                                case 4:
                                    a4 += Amount1;
                                    break;
                                case 5:
                                    a5 += Amount1;
                                    break;
                                case 6:
                                    a6 += Amount1;
                                    break;
                                case 7:
                                    a7 += Amount1;
                                    break;
                                case 8:
                                    a8 += Amount1;
                                    break;
                                case 9:
                                    a9 += Amount1;
                                    break;
                                case 10:
                                    a10 += Amount1;
                                    break;
                                case 11:
                                    a11 += Amount1;
                                    break;
                                case 12:
                                    a12 += Amount1;
                                    break;
                            }
                        }
                    }
                    res.moveToNext();
                }
                double percent = 0;
                if (a1 != 0) {
                    percent = a1 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a1, "January " + (int) percent + "%"));
                }
                if (a2 != 0) {
                    percent = a2 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a2, "February " + (int) percent + "%"));
                }
                if (a3 != 0) {
                    percent = a3 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a3, "March " + (int) percent + "%"));
                }
                if (a4 != 0) {
                    percent = a4 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a4, "April " + (int) percent + "%"));
                }

                if (a5 != 0) {
                    percent = a5 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a5, "May " + (int) percent + "%"));
                }
                if (a6 != 0) {
                    percent = a6 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a6, "June " + (int) percent + "%"));
                }
                if (a7 != 0) {
                    percent = a7 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a7, "July " + (int) percent + "%"));
                }

                if (a8 != 0) {
                    percent = a8 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a8, "August " + (int) percent + "%"));
                }
                if (a9 != 0) {

                    percent = a9 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a9, "September " + (int) percent + "%"));
                }
                if (a10 != 0) {
                    percent = a10 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a10, "October " + (int) percent + "%"));
                }
                if (a11 != 0) {

                    percent = a11 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a11, "November" + (int) percent + "%"));
                }
                if (a12 != 0) {
                    percent = a12 / incomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a12, "December " + (int) percent + "%"));
                }


            }
        }
        if (category.equalsIgnoreCase(("All Outcomes"))) {
            double Amount1 = 0;
            double outcomesD = 0;
            double outcomesM = 0;
            double purchaseD=0;
            double transferD=0;
            if (period.equalsIgnoreCase("Daily")) {
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("outcome")) {
                        String category = res.getString(res.getColumnIndex("Category"));
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                if (category.equalsIgnoreCase("Purchases")) {
                                    Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                    outcomesD += Amount1;
                                    purchaseD += Amount1;
                                } else if (category.equalsIgnoreCase("Transfers")) {
                                    Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                    outcomesD += Amount1;
                                    transferD += Amount1;
                                }
                            }
                        }
                    }
                    res.moveToNext();
                }

                double percentS = purchaseD / outcomesD * 100;
                double percentI = transferD / outcomesD * 100;
                percentS = Math.round(percentS);
                percentI = Math.round(percentI);
                pieEntriesArrayList.add(new PieEntry((float) purchaseD, "Purchases\n" + (int) percentS + "%"));
                pieEntriesArrayList.add(new PieEntry((float) transferD, "Transfers\n" + (int) percentI + "%"));
            }

            if (period.equalsIgnoreCase("Monthly")) {
                double a1 = 0, a2 = 0, a3 = 0, a4 = 0, a5 = 0, a6 = 0, a7 = 0, a8 = 0, a9 = 0, a10 = 0, a11 = 0, a12 = 0;
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    if (type.equalsIgnoreCase("outcome")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                        if (y == 23) {
                            outcomesM += Amount1;
                            switch (m) {
                                case 1:
                                    a1 += Amount1;
                                    break;
                                case 2:
                                    a2 += Amount1;
                                    break;
                                case 3:
                                    a3 += Amount1;
                                    break;
                                case 4:
                                    a4 += Amount1;
                                    break;
                                case 5:
                                    a5 += Amount1;
                                    break;
                                case 6:
                                    a6 += Amount1;
                                    break;
                                case 7:
                                    a7 += Amount1;
                                    break;
                                case 8:
                                    a8 += Amount1;
                                    break;
                                case 9:
                                    a9 += Amount1;
                                    break;
                                case 10:
                                    a10 += Amount1;
                                    break;
                                case 11:
                                    a11 += Amount1;
                                    break;
                                case 12:
                                    a12 += Amount1;
                                    break;
                            }
                        }


                    }
                    res.moveToNext();
                }
                double percent = 0;
                if (a1 != 0) {
                    percent = a1 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a1, "January " + (int) percent + "%"));
                }
                if (a2 != 0) {
                    percent = a2 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a2, "February " + (int) percent + "%"));
                }
                if (a3 != 0) {
                    percent = a3 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a3, "March " + (int) percent + "%"));
                }
                if (a4 != 0) {
                    percent = a4 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a4, "April " + (int) percent + "%"));
                }

                if (a5 != 0) {
                    percent = a5 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a5, "May " + (int) percent + "%"));
                }
                if (a6 != 0) {
                    percent = a6 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a6, "June " + (int) percent + "%"));
                }
                if (a7 != 0) {
                    percent = a7 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a7, "July " + (int) percent + "%"));
                }

                if (a8 != 0) {
                    percent = a8 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a8, "August " + (int) percent + "%"));
                }
                if (a9 != 0) {

                    percent = a9 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a9, "September " + (int) percent + "%"));
                }
                if (a10 != 0) {
                    percent = a10 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a10, "October " + (int) percent + "%"));
                }
                if (a11 != 0) {

                    percent = a11 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a11, "November" + (int) percent + "%"));
                }
                if (a12 != 0) {
                    percent = a12 / outcomesM * 100;
                    percent = Math.round(percent);
                    pieEntriesArrayList.add(new PieEntry((float) a12, "December " + (int) percent + "%"));
                }


            }
        }
        if (category.equalsIgnoreCase(("All"))) {
            if (period.equalsIgnoreCase("Daily")) {
                double incomes = 0;
                double outcomes = 0;
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    double Amount1 = 0;
                    if (type.equalsIgnoreCase("outcome")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                outcomes += Amount1;
                            }
                        }
                    }
                    res.moveToNext();
                }
                res.moveToFirst();
                while (!res.isAfterLast()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    double Amount1 = 0;
                    if (type.equalsIgnoreCase("income")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        String year = date.substring(6, 8);
                        int y = Integer.parseInt(year);
                        if (y == 23) {
                            if ((m == map.get(monthm))) {
                                Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                incomes += Amount1;
                            }
                        }
                    }
                    res.moveToNext();
                }
                double percenti = incomes / (outcomes + incomes) * 100;
                double percento = outcomes / (outcomes + incomes) * 100;
                percenti = Math.round(percenti);
                percento = Math.round(percento);
                pieEntriesArrayList.add(new PieEntry((float) incomes, "incomes\n" + (int) percenti + "%"));
                pieEntriesArrayList.add(new PieEntry((float) outcomes, "outcomes\n" + (int) percento + "%"));
            } else {

                if (period.equalsIgnoreCase("Monthly")) {
                    double incomes = 0;
                    double outcomes = 0;
                    res.moveToFirst();
                    while (!res.isAfterLast()) {
                        String type = res.getString(res.getColumnIndex("Type"));
                        double Amount1 = 0;
                        if (type.equalsIgnoreCase("outcome")) {
                            String date = res.getString(res.getColumnIndex("Date"));
                            String year = date.substring(6, 8);
                            int y = Integer.parseInt(year);
                            if ((y == 23)) {
                                Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                outcomes += Amount1;
                            }
                        }
                        res.moveToNext();
                    }
                    res.moveToFirst();
                    while (!res.isAfterLast()) {
                        String type = res.getString(res.getColumnIndex("Type"));
                        double Amount1 = 0;
                        if (type.equalsIgnoreCase("income")) {
                            String date = res.getString(res.getColumnIndex("Date"));
                            String year = date.substring(6, 8);
                            int y = Integer.parseInt(year);
                            if ((y == 23)) {
                                Amount1 = res.getDouble(res.getColumnIndex("Amount"));
                                incomes += Amount1;

                            }
                        }
                        res.moveToNext();
                    }
                    double percenti = incomes / (outcomes + incomes) * 100;
                    double percento = outcomes / (outcomes + incomes) * 100;
                    percenti = Math.round(percenti);
                    percento = Math.round(percento);
                    pieEntriesArrayList.add(new PieEntry((float) incomes, "incomes\n" + (int) percenti + "%"));
                    pieEntriesArrayList.add(new PieEntry((float) outcomes, "outcomes\n" + (int) percento + "%"));
                }


            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (category.equalsIgnoreCase(("All"))) {
            return false;
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BarOption:
                barChart.setVisibility(View.VISIBLE);
                barChart.animateY(3000);
                pieChart.setVisibility(View.INVISIBLE);

                return true;

            case R.id.PieOption:
                barChart.setVisibility(View.INVISIBLE);
                pieChart.animateXY(1000, 500);
                pieChart.setVisibility(View.VISIBLE);

        return true;
        case android.R.id.home:
        this.finish();
        return true;
    }
    return true;
}

}

