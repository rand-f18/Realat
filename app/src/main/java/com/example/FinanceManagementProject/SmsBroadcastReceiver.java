package com.example.FinanceManagementProject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS ="android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"Intent Received:" +intent.getAction ());

        if (intent.getAction().equals(SMS)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {



                Object[] objects = (Object[]) bundle.get("pdus");


                SmsMessage[] messages = new SmsMessage[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) objects[i]);
                    msg = messages[i].getMessageBody();

                }

            }

        }
    }
}
