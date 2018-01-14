package bodyfatcontrol.github;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public final class MainActivity extends WearableActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext = MainActivity.this;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SensorHR sensorHR;
    private BroadcastReceiver mBroadcastReceiver;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Starting background app", Toast.LENGTH_LONG).show();

        // setup an alarm at every 1m, to wakeup the system read HR sensor, etc
        alarmMgr = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), MyReceiver.class);
        intent.setAction("bodyfatcontrol.github");
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60, alarmIntent);

        sensorHR = new SensorHR(mContext);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String hr_value = intent.getStringExtra("HR_VALUE");

                if (mGoogleApiClient.isConnected()) {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/hr_value", hr_value.getBytes()).await();
                        if (!result.getStatus().isSuccess()) {
                            Log.e("sendMessage", "error");
                        } else {
                            Log.i("sendMessage", "success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver,
                new IntentFilter("HR_VALUE"));

        // Enables Always-on
        setAmbientEnabled();

//        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
//        alarmMgr.cancel(alarmIntent); // stop alarm
        sensorHR.stopHR(); // stop HR sensor
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MainActivity", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("MainActivity", "Failed to connect to Google API Client");
    }

    public Context getContext() {
        return mContext;
    }
}


