package com.example.FinanceManagementProject;




import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMSSIONS_RROUEST_RECRIVE_SMS = 0;
    private static final String SMS = "android.permission.RECEIVE_SMS";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


    DataBaseHelper myDb;
    RadioButton totalIncome;
    RadioButton totalOutcome;
    RadioButton balance;
    RadioGroup radioGroup;
    RadioGroup radioGroup1;
    RadioButton criteria;
    RadioButton period;
    Button generate;
    RadioButton d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Finance Management");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myDb = new DataBaseHelper(this);
        totalIncome = findViewById(R.id.totalIncome);
        totalOutcome = findViewById(R.id.totalOutcome);
        balance = findViewById(R.id.balance);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup1 = findViewById(R.id.radioGroup1);
        generate = findViewById(R.id.generate);
        registerReceiver(receiver, new IntentFilter(SMS_RECEIVED));
        viewAll();
        d = (RadioButton) findViewById(R.id.daily);

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



    public void viewAll() {
        // retrieve from the database
        Cursor res = myDb.getData();
        if (res.getCount() == 0) {
            totalIncome.setText(0  );
            totalOutcome.setText(0 );
            SharedPreferences sh = getSharedPreferences("balance", MODE_PRIVATE);
            double b = sh.getFloat("balance", -1);
            balance.setText("Balance\n" + b );
            return;
        }


        double income = 0;
        double outcome = 0;
        res.moveToFirst();
        while (!(res.isAfterLast())) {
            if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("income")) {
                income = income + res.getDouble(res.getColumnIndex("Amount"));
            } else if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("outcome")) {
                outcome = outcome + res.getDouble(res.getColumnIndex("Amount"));
            }
            res.moveToNext();
        }

        totalIncome.setText(""+income);
        totalOutcome.setText(""+outcome);

        SharedPreferences sh = getSharedPreferences("balance", MODE_PRIVATE);
        double b = sh.getFloat("balance", 0) + income - outcome;
        balance.setText("Balance\n" + b );
    }

    public void onClick(View v) {
        int selectedCriteria = radioGroup.getCheckedRadioButtonId();
        criteria = (RadioButton) findViewById(selectedCriteria);
        int selectedPeriod = radioGroup1.getCheckedRadioButtonId();
        period = (RadioButton) findViewById(selectedPeriod);
        Cursor dataset = myDb.getData();

        if (selectedCriteria == -1 && selectedPeriod == -1) {
            showMessage("Nothing Selected", "Please select a category and period");
            return;
        }
        else {
            if (selectedCriteria == -1) {
                showMessage("No Category Selected", "Please select a category");
                return;
            } else if (selectedPeriod == -1) {
                showMessage("Nothing Period Selected", "Please select a period");
                return;
            }
        }

        if (dataset.getCount() == 0) {
            showMessage("Nothing in the database", "the database is empty");
            return;
        } else {
            Cursor res = myDb.getData();
            boolean foundCriteria = false;

            if (criteria.getText().toString().equalsIgnoreCase("All Incomes")) {
                while (res.moveToNext()) {
                    if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("income")) {
                        foundCriteria = true;
                    }
                }
            } else if (criteria.getText().toString().equalsIgnoreCase("All Outcomes")) {
                while (res.moveToNext()) {
                    if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("outcome")) {
                        foundCriteria = true;
                    }
                }
            } else if (criteria.getText().toString().equalsIgnoreCase("All")) {
                foundCriteria = true;
            } else {
                while (res.moveToNext()) {
                    if (res.getString(res.getColumnIndex("Category")).equalsIgnoreCase(criteria.getText().toString())) {
                        foundCriteria = true;

                    }
                }
            }


            if (!foundCriteria) {
                showMessage("Category", "The selected category is not Found");
                return;
            }
            if(d.isChecked()){
                menu(criteria.getText().toString());
            }
            else{
            if (criteria.getText().toString().equalsIgnoreCase("All Incomes") || criteria.getText().toString().equalsIgnoreCase("All outComes") || criteria.getText().toString().equalsIgnoreCase("All")) {
                Intent next = new Intent(this, thirdActivitypt2.class);
                next.putExtra("category", criteria.getText().toString());
                next.putExtra("period", period.getText().toString());
                startActivity(next);
            } else {
                Intent next = new Intent(this, thirdActivitypt1.class);
                next.putExtra("category", criteria.getText().toString());
                next.putExtra("period", period.getText().toString());
                startActivity(next);
            }
        }}

    }
public void menu(String c){
 if (period.getText().toString().equalsIgnoreCase("daily")) {
        final int[] checkedItem = {-1};
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Choose Month");
            final String[] listItems = new String[]{"January", "February", "March","April","May","June","July","August","September","October","November","December"};
            alertDialog.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                checkedItem[0] = which;
                dialog.dismiss();
                Cursor res = myDb.getData();
                boolean foundCriteriaI = false;
                boolean foundCriteriaS = false;
                boolean foundCriteriaP = false;
                boolean foundCriteriaT = false;
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

                while (res.moveToNext()) {
                    String type = res.getString(res.getColumnIndex("Type"));
                    String category = res.getString(res.getColumnIndex("Category"));
                    if (type.equalsIgnoreCase("income")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        if (m == map.get(listItems[which])) {
                            if (category.equalsIgnoreCase("salary")) {
                                foundCriteriaS = true;
                            } else if ((category.equalsIgnoreCase("investments"))) {
                                foundCriteriaI = true;
                            }
                        }
                    } else if (type.equalsIgnoreCase("outcome")) {
                        String date = res.getString(res.getColumnIndex("Date"));
                        String month = date.substring(3, 5);
                        int m = Integer.parseInt(month);
                        if (m == map.get(listItems[which])) {
                            if (category.equalsIgnoreCase("purchases")) {
                                foundCriteriaP = true;
                            } else if ((category.equalsIgnoreCase("transfers"))) {
                                foundCriteriaT = true;
                            }
                        }
                    }
                }

                if (c.equalsIgnoreCase("All Incomes")) {
                    if (foundCriteriaS || foundCriteriaI) {
                        Intent next = new Intent(this, thirdActivitypt2.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }

                }
                else if (c.equalsIgnoreCase("Salary")) {
                    if (foundCriteriaS) {
                        Intent next = new Intent(this, thirdActivitypt1.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    } else {
                        showMessage("Category", "The selected month is empty");
                    }
                }else if (c.equalsIgnoreCase("investments")) {
                    if (foundCriteriaI) {
                        Intent next = new Intent(this, thirdActivitypt1.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }

                }
                else
                if (c.equalsIgnoreCase("All outcomes")) {
                    if (foundCriteriaP || foundCriteriaT) {
                        Intent next = new Intent(this, thirdActivitypt2.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }
                } else if (c.equalsIgnoreCase("purchases")) {
                    if (foundCriteriaP) {
                        Intent next = new Intent(this, thirdActivitypt1.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }
                } else if (c.equalsIgnoreCase("Transfers")) {
                    if (foundCriteriaT) {
                        Intent next = new Intent(this, thirdActivitypt1.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }
                }
                else if (c.equalsIgnoreCase("All")) {
                    if (foundCriteriaT || foundCriteriaP || foundCriteriaS || foundCriteriaI) {
                        Intent next = new Intent(this, thirdActivitypt2.class);
                        next.putExtra("category", c);
                        next.putExtra("period", period.getText().toString());
                        next.putExtra("month", listItems[which]);
                        startActivity(next);
                    }
                }
                    else {
                        showMessage("Category", "The selected month is empty");
                    }

                    });


            alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
            });
            AlertDialog customAlertDialog = alertDialog.create();

            // show the alert dialog when the button is clicked
            customAlertDialog.show();

    }}
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void resetAll(View v){
        myDb.clear();
        SharedPreferences sh= getSharedPreferences("balance",MODE_PRIVATE);
        SharedPreferences.Editor myEdit =sh.edit();
        myEdit.putFloat("balance",-1);
        myEdit.apply();
        Intent i = new Intent(this,FirstActivity.class);
        startActivity(i);


    }

    final SmsBroadcastReceiver receiver = new SmsBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            viewAll();
        }
    };

}




