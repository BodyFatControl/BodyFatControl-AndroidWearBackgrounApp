package bodyfatcontrol.github;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Map;
import java.util.Set;

public final class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener {

    private Context mContext = MainActivity.this;
    SensorHR sensorHR;
    private BroadcastReceiver mBroadcastReceiver;

    private static final String MOBILE_APP_CAPABILITY_NAME = "capability_mobile_app";
    public static final String MESSAGE_PATH = "/message_path";
    private String mMobileAppNodeId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Starting background app", Toast.LENGTH_LONG).show();

        // will setup and detect if the app is installed and the wear is connected to Android Wear mobile app
        setupDetectMobileApp();

        // start HR sensor
        sensorHR = new SensorHR(mContext);

        // receive the value of HR sensor
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String HRValue = intent.getStringExtra("HR_VALUE");
                Log.i("onReceive", HRValue);
                if (mMobileAppNodeId != null) {
                    Task<Integer> sendTask = Wearable.getMessageClient(MainActivity.this).sendMessage(
                            mMobileAppNodeId, MESSAGE_PATH, HRValue.getBytes());
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver,
                new IntentFilter("HR_VALUE"));

        // Enables Always-on
        setAmbientEnabled();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getMessageClient(this).addListener(this);
        Wearable.getCapabilityClient(this)
                .addListener(this, MOBILE_APP_CAPABILITY_NAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getMessageClient(this).removeListener(this);
        Wearable.getCapabilityClient(this).removeListener(this);
    }


    @Override
    protected void onStop()
    {
        super.onStop();
//        alarmMgr.cancel(alarmIntent); // stop alarm
//        sensorHR.stopHRSensor(); // stop HR sensor
    }

    public Context getContext() {
        return mContext;
    }

    public void setupDetectMobileApp() {
        Task<Map<String, CapabilityInfo>> capabilitiesTask =
                Wearable.getCapabilityClient(this)
                        .getAllCapabilities(CapabilityClient.FILTER_REACHABLE);

        capabilitiesTask.addOnSuccessListener(new OnSuccessListener<Map<String, CapabilityInfo>>() {
            @Override
            public void onSuccess(Map<String, CapabilityInfo> capabilityInfoMap) {
                if (capabilityInfoMap.isEmpty()) {
                    return;
                }

                CapabilityInfo capabilityInfo = capabilityInfoMap.get(MOBILE_APP_CAPABILITY_NAME);
                if (capabilityInfo != null) {
                    Set<Node> connectedNodes = capabilityInfo.getNodes();

                    // Find a nearby node
                    mMobileAppNodeId = null;
                    for (Node node : connectedNodes) {
                        if (node.isNearby()) {
                            mMobileAppNodeId = node.getId();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {
        if (capabilityInfo != null) {
            Set<Node> connectedNodes = capabilityInfo.getNodes();

            // Find a nearby node
            mMobileAppNodeId = null;
            for (Node node : connectedNodes) {
                if (node.isNearby()) {
                    mMobileAppNodeId = node.getId();
                }
            }
        }
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {

    }
}


