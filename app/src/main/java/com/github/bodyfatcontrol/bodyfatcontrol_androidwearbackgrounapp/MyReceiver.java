package com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp")) {

            // send the message TIMER_FIRED
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TIMER_FIRED"));
        }
    }
}
