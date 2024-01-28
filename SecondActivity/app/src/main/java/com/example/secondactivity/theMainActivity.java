package com.example.secondactivity;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class theMainActivity extends AppCompatActivity {

    EditText numberEtv;
    Button submit;
    //broadcastReceiver
    private static final int MY_PERMSSIONS_RROUEST_RECRIVE_SMS = 0;
    private static final String SMS ="android.permission.RECEIVE_SMS";
    private static final String SMS_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
    String Type,Category,Amount,Date;
    int t1,t2,c1,c2,a1,a2,d1;
    //take the message that has been receveied and display it in the text view
    //broadcastReceiver
    final SmsBroadcastReceiver receiver = new SmsBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Toast.makeText(theMainActivity.this, msg, Toast.LENGTH_SHORT).show();
            try {
                //Type:income
//Category:salary
//Amount:800
//Date:2022/02/1
                t1 = msg.indexOf("Type") + 5;
                if (t1 < 0) {
                    Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                } else {
                    t2 = msg.indexOf("Category");
                    if (t2 < 0) {
                        Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                    } else {
                        c1 = msg.indexOf("Category") + 9;
                        if (c1 < 0) {
                            Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                        } else {
                            c2 = msg.indexOf("Amount");
                            if (c2 < 0) {
                                Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                            } else {
                                a1 = msg.indexOf("Amount") + 7;
                                if (a1 < 0) {
                                    Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                                } else {
                                    a2 = msg.indexOf("Date");
                                    if (a2 < 0) {
                                        Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
                                    } else {
                                        d1 = msg.indexOf("Date") + 5;
                                        if (d1 < 0) {
                                            Toast.makeText(theMainActivity.this, "Wrong message", LENGTH_SHORT).show();
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
                DataBaseHelper dataBaseHelper = new DataBaseHelper(theMainActivity.this);
                boolean b = dataBaseHelper.insertData(Type, Category, amount, Date);
                Toast.makeText(theMainActivity.this, "Message has been received" + b, LENGTH_SHORT).show();
                Toast.makeText(theMainActivity.this, "Type:" + Type, LENGTH_SHORT).show();
                Toast.makeText(theMainActivity.this, "Category:" + Category, LENGTH_SHORT).show();
                Toast.makeText(theMainActivity.this, "Amount:" + Amount, LENGTH_SHORT).show();
                Toast.makeText(theMainActivity.this, "Date:" + Date, LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themainactivity);

        numberEtv = findViewById(R.id.numberEtv);
        submit = findViewById(R.id.submit);
        //DataBaseHelper db=new DataBaseHelper(this);
//        db.clear();
//        reset();
//SharedPreferences sh=getSharedPreferences("balance", MODE_PRIVATE);
//double b=sh.getFloat("balance",-1);
//if(b!=-1){
//    Intent intent=new Intent(this, MainActivity.class);
//    startActivity(intent);
//}


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
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i); }


    private boolean validation(String number) {

        if (number.length() == 0){
            numberEtv.requestFocus();
            numberEtv.setError("FIELD CAN NOT BE EMPTY");
            return false;
        }
        else if (!number.matches("-?[0-9]*(\\.[0-9]*)?")){
            numberEtv.requestFocus();
            numberEtv.setError("ENTER ONLY NUMBERS");
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
    public void reset(){
        SharedPreferences sh=getSharedPreferences("balance", MODE_PRIVATE);
        SharedPreferences.Editor e= sh.edit();
        e.putFloat("balance", -1);
        e.apply();
    }
}



//all income, all outcome, all-> barchart,pie chart
//invesment or salary or purchases or transfers->line chart







