package com.example.FinanceManagementProject;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FirstActivity extends AppCompatActivity {

    EditText numberEtv;
    Button submit;
    //broadcastReceiver
    private static final int MY_PERMSSIONS_RROUEST_RECRIVE_SMS = 0;
    private static final String SMS = "android.permission.RECEIVE_SMS";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String Type, Category, Amount, Date;
    int t1, t2, c1, c2, a1, a2, d1;
    //broadcastReceiver
    final SmsBroadcastReceiver receiver = new SmsBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Toast.makeText(FirstActivity.this, msg, Toast.LENGTH_SHORT).show();
            try {
                t1 = msg.indexOf("Type") + 5;
                if (t1 < 0) {
                    Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                } else {
                    t2 = msg.indexOf("Category");
                    if (t2 < 0) {
                        Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                    } else {
                        c1 = msg.indexOf("Category") + 9;
                        if (c1 < 0) {
                            Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                        } else {
                            c2 = msg.indexOf("Amount");
                            if (c2 < 0) {
                                Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                            } else {

                                a1 = msg.indexOf("Amount") + 7;
                                if (a1 < 0) {
                                    Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                                } else {
                                    a2 = msg.indexOf("Date");
                                    if (a2 < 0) {
                                        Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                                    } else {
                                        d1 = msg.indexOf("Date") + 5;
                                        if (d1 < 0) {
                                            Toast.makeText(FirstActivity.this, "Wrong message", LENGTH_SHORT).show();
                                        }

                                    }

                                }

                            }

                        }

                    }

                }


                int t3 = msg.indexOf(" ");
                int c3 = msg.indexOf(" ", c1);
                int a3 = msg.indexOf(" ", a1);
                int d3 = msg.indexOf(" ", d1);
                Type = msg.substring(t1, t3);
                Category = msg.substring(c1, c3);
                Amount = msg.substring(a1, a3);
                Date = msg.substring(d1, d3);
                double amount = Double.parseDouble(Amount);
                DataBaseHelper dataBaseHelper = new DataBaseHelper(FirstActivity.this);
                boolean b = dataBaseHelper.insertData(Type, Category, amount, Date);
                Toast.makeText(FirstActivity.this, "Message has been received", LENGTH_SHORT).show();
                Toast.makeText(FirstActivity.this, "Type:" + Type, LENGTH_SHORT).show();
                Toast.makeText(FirstActivity.this, "Category:" + Category, LENGTH_SHORT).show();
                Toast.makeText(FirstActivity.this, "Amount:" + Amount, LENGTH_SHORT).show();
                Toast.makeText(FirstActivity.this, "Date:" + Date, LENGTH_SHORT).show();

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        setTitle("Finance Management");
        numberEtv = findViewById(R.id.numberEtv);
        submit = findViewById(R.id.submit);

//******************sharedPrefrences****************************
        SharedPreferences sh=getSharedPreferences("balance", MODE_PRIVATE);
        double b=sh.getFloat("balance",-1);
        if(b!=-1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


//broadcastReceiver
        if(ContextCompat.checkSelfPermission(this, SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,SMS)){
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{SMS},MY_PERMSSIONS_RROUEST_RECRIVE_SMS);
            }
        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberEtv.getText().toString();

                boolean check = validation(number);
                if (check == true){
                    Toast.makeText(getApplicationContext(),"data is valid", Toast.LENGTH_SHORT).show();
                    shared(numberEtv);
                } else{
                    Toast.makeText(getApplicationContext(),"Sorry check your information again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void shared(View v){
        SharedPreferences sh= getSharedPreferences("balance",MODE_PRIVATE);
        SharedPreferences.Editor myEdit =sh.edit();
        myEdit.putFloat("balance", (float) parseDouble(numberEtv.getText().toString()));
        myEdit.apply();
        Toast.makeText(getApplicationContext(),"balance is saved", Toast.LENGTH_SHORT).show();
        Toast.makeText(this,numberEtv.getText().toString() , LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i); }


    private boolean validation(String number) {

        if (number.length() == 0){
            numberEtv.requestFocus();
            numberEtv.setError("FIELD CAN NOT BE EMPTY");
            return false;
        }
        else if (!number.matches("-?[0-9]{1,9}(\\.[0-9]{1,2})?")){
            numberEtv.requestFocus();
            numberEtv.setError("ENTER ONLY NUMBERS IN FORMAT xxxxxxxxxxxx.xx (12digit.2digit)");
            return false;
        }
        else{
            return true;
        }
    }


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

//TestCases
//salaries

//#1
//Type:income
//Category:salary
//Amount:100.70
//Date:20-01-23
//#2
//Type:income
//Category:salary
//Amount:18
//Date:02-04-23
//#3
//Type:income
//Category:salary
//Amount:40
//Date:08-12-23
//#4
//Type:income
//Category:salary
//Amount:550
//Date:25-12-23

//investments
//#1
//Type:income
//Category:investments
//Amount:16.90
//Date:27-01-23
//#2
//Type:income
//Category:investments
//Amount:700
//Date:02-05-23
//#3
//Type:income
//Category:investments
//Amount:45
//Date:10-11-23

//purchases
//#1
//Type:outcome
//Category:purchases
//Amount:7.45
//Date:05-02-23
//#2
//Type:outcome
//Category:purchases
//Amount:90.25
//Date:02-07-23
//#3
//Type:outcome
//Category:purchases
//Amount:67
//Date:04-12-23

//transfers
//#1
//Type:outcome
//Category:transfers
//Amount:79
//Date:09-01-23
//#2
//Type:outcome
//Category:transfers
//Amount:230
//Date:02-04-23
//#3
//Type:outcome
//Category:transfers
//Amount:10
//Date:04-11-23











