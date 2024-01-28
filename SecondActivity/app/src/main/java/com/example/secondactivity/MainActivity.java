package com.example.secondactivity;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMSSIONS_RROUEST_RECRIVE_SMS = 0;
    private static final String SMS ="android.permission.RECEIVE_SMS";
    private static final String SMS_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
    String Type,Category,Amount,Date;
    int t1,t2,c1,c2,a1,a2,d1;

    DataBaseHelper myDb;
    RadioButton totalIncome;
    RadioButton totalOutcome;
    RadioButton balance;
    RadioGroup radioGroup;
    RadioGroup radioGroup1;
    RadioButton criteria;
    RadioButton period;
    Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DataBaseHelper(this);
        totalIncome = findViewById(R.id.totalIncome);
        totalOutcome = findViewById(R.id.totalOutcome);
        balance = findViewById(R.id.balance);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup1 = findViewById(R.id.radioGroup1);
        generate = findViewById(R.id.generate);
         //AddData();
        viewAll();
    }

    public void AddData() {


        boolean isinsrted = myDb.insertData("income",
                "Purchases", 120.67, "12-02-2021");
        //remove it
        if (isinsrted == true)
            Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
    }


    public void viewAll() {
        // retrieve from the database
        Cursor res = myDb.getData();
        if (res.getCount() == 0) {
            totalIncome.setText(0 + " SAR");
            totalOutcome.setText(0 + " SAR");
            return;
        }


        double income = 0;
        double outcome = 0;
        while (res.moveToNext()) {
            if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("Income")) {
                income = income + res.getDouble(res.getColumnIndex("Amount"));
            }
            if (res.getString(res.getColumnIndex("Type")).equalsIgnoreCase("Outcome")) {
                outcome = outcome + res.getDouble(res.getColumnIndex("Amount"));
            }

        }
        totalIncome.setText(income + " SAR");
        totalOutcome.setText(outcome + " SAR");

        SharedPreferences sh =getSharedPreferences("balance",MODE_PRIVATE);
        double b= sh.getFloat("balance",0)+income-outcome;
        balance.setText("Balance\n"+b+"SAR");
    }

    public void onClick(View v) {
        int selectedCriteria = radioGroup.getCheckedRadioButtonId();
        criteria = (RadioButton) findViewById(selectedCriteria);
        int selectedPeriod = radioGroup1.getCheckedRadioButtonId();
        period = (RadioButton) findViewById(selectedPeriod);
        Cursor dataset = myDb.getData();

        if (selectedCriteria == -1 || selectedPeriod == -1) {
        showMessage("Nothing Selected","Please select a category and a period");
        return;}

        if (dataset.getCount() == 0) {
            showMessage("Nothing in the database","the database is empty");
            return;
        }

        else {
            boolean foundCriteria = false;
            if (criteria.getText().toString().equals("All Incomes")||criteria.getText().toString().equals("All OutComes")){
                foundCriteria = true;

            }

            else {
                Cursor res = myDb.getData();
                while (res.moveToNext()) {
                    if (res.getString(res.getColumnIndex("Category")).equalsIgnoreCase(criteria.getText().toString())) {
                        foundCriteria = true;

                    }
                }
            }
            if(!foundCriteria){
        showMessage("Category","The selected category is not Found");
        return;
            }

        }
Intent next =new Intent(this, thirdActivity.class);
        next.putExtra("category",criteria.getText().toString());
        next.putExtra("period",period.getText().toString());
        startActivity(next);
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    final SmsBroadcastReceiver receiver = new SmsBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            try {
                //Type:income
//Category:salary
//Amount:800
//Date:2022/02/1
                t1 = msg.indexOf("Type") + 5;
                if (t1 < 0) {
                    Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                } else {
                    t2 = msg.indexOf("Category");
                    if (t2 < 0) {
                        Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                    } else {
                        c1 = msg.indexOf("Category") + 9;
                        if (c1 < 0) {
                            Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                        } else {
                            c2 = msg.indexOf("Amount");
                            if (c2 < 0) {
                                Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                            } else {
                                a1 = msg.indexOf("Amount") + 7;
                                if (a1 < 0) {
                                    Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                                } else {
                                    a2 = msg.indexOf("Date");
                                    if (a2 < 0) {
                                        Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                                    } else {
                                        d1 = msg.indexOf("Date") + 5;
                                        if (d1 < 0) {
                                            Toast.makeText(MainActivity.this, "Wrong message", LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Type = msg.substring(t1, t2);
                Category = msg.substring(c1, c2);
                Amount = msg.substring(a1, a2);
                Date = msg.substring(d1);
                double amount = Double.parseDouble(Amount);
//                all = "" + Type + Category + Amount + Date;
//                mmodel = new model(-1, Type, Amount, Category, Date);
//                Message.setText(mmodel.toString());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean b = dataBaseHelper.insertData(Type, Category, amount, Date);
                Toast.makeText(MainActivity.this, "Message has been received" + b, LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Type:" + Type, LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Category:" + Category, LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Amount:" + Amount, LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Date:" + Date, LENGTH_SHORT).show();
                viewAll();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    //broadcastReceiver
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,new IntentFilter(SMS_RECEIVED));

    }
    //broadcastReceiver
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    //broadcastReceiver
    public void onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMSSIONS_RROUEST_RECRIVE_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Thank you for permitting!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "Can't Access Messages", Toast.LENGTH_SHORT).show();
                }

        }


    }
}




