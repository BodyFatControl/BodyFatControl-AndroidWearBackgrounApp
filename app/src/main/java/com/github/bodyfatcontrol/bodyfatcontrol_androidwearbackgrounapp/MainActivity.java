package com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    private Context mContext = MainActivity.this;

    private TextView mTextView;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        alarmMgr = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), MyReceiver.class);
        intent.setAction("com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp");
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60, alarmIntent);

        // Enables Always-on
        setAmbientEnabled();

        finish();
    }

    public Context getContext() {
        return mContext;
    }
}

