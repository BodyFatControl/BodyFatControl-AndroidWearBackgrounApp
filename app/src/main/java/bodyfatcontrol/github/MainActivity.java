package bodyfatcontrol.github;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

public final class MainActivity extends WearableActivity {

    private Context mContext = MainActivity.this;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SensorHR sensorHR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Starting background app", Toast.LENGTH_LONG).show();

//        // setup an alarm at every 1m, to wakeup the system read HR sensor, etc
//        alarmMgr = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
//        Intent intent = new Intent(getContext(), MyReceiver.class);
//        intent.setAction("com.github.bodyfatcontrol.bodyfatcontrol_androidwearbackgrounapp");
//        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
//                1000 * 60, alarmIntent);

        sensorHR = new SensorHR(mContext);

        // Enables Always-on
        setAmbientEnabled();

        finish();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
//        alarmMgr.cancel(alarmIntent); // stop alarm
        sensorHR.stopHR(); // stop HR sensor
    }

    public Context getContext() {
        return mContext;
    }
}


