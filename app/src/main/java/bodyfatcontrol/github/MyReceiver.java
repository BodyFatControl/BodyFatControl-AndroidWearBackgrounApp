package bodyfatcontrol.github;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("bodyfatcontrol.github")) {

            // send the message TIMER_FIRED
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TIMER_FIRED"));
        }
    }
}
