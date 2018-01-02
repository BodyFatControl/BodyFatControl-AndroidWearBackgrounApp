package com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by cas on 02-01-2018.
 */

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

..

        if (intent.getAction().equalsIgnoreCase("com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp")){
            Toast.makeText(context, "timer",Toast.LENGTH_LONG).show();
        }
    }
}
