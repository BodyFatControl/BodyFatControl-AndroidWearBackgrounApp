package com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public final class MainActivity extends WearableActivity {

    private Context mContext = MainActivity.this;

    SensorHR sensorHR;

    private TextView mTextView;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Starting background app", Toast.LENGTH_LONG).show();

        alarmMgr = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), MyReceiver.class);
        intent.setAction("com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp");
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60, alarmIntent);

        sensorHR = new SensorHR (mContext);

        // Enables Always-on
        setAmbientEnabled();

        finish();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        sensorHR.stopHR();
    }

    public Context getContext() {
        return mContext;
    }
}


