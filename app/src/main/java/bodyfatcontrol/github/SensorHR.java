package bodyfatcontrol.github;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;

import static android.content.Context.SENSOR_SERVICE;

public class SensorHR implements SensorEventListener {

    Context mContext;
    SensorManager mSensorManager;
    Sensor mHeartRateSensor;
        private BroadcastReceiver mBroadcastReceiver;

    public SensorHR (Context context) {
        mContext = context;
        mSensorManager = ((SensorManager) context.getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // register the action for whe receiving the message TIMER_FIRED
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startHR();
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mBroadcastReceiver,
                new IntentFilter("TIMER_FIRED"));
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(
                    new Intent("HR_VALUE").putExtra(
                            "HR_VALUE", String.valueOf(event.values[0])));

//            Intent intent = new Intent(mContext, MainActivity.class);
//            intent.putExtra("HR_VALUE", String.valueOf(event.values[0]));
//            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    public void startHR() {
        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopHR () {
        mSensorManager.unregisterListener(this);
    }


}
