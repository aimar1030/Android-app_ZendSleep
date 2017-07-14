package com.peter.zensleepfree.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.peter.zensleepfree.UtilsClass.ZenSleep;

/**
 * Created by peter on 7/8/16.
 */
public class PlugInActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            // Do something when power connected
            ZenSleep.powerConnected = true;
            Log.d("Power Action", "Power Plugged in");
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            // Do something when power disconnected
            ZenSleep.powerConnected = false;
            Log.d("Power Action", "Power Plugged out");
        }
    }
}

