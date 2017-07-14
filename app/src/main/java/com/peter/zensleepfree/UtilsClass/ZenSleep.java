package com.peter.zensleepfree.UtilsClass;

import android.app.Application;
import android.util.Log;

import com.peter.zensleepfree.Model.SleepDBModel;

import java.util.ArrayList;
import java.util.LinkedList;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.KiipFragmentCompat;
import me.kiip.sdk.Poptart;

/**
 * Created by peter on 6/27/16.
 */
public class ZenSleep extends Application implements Kiip.OnContentListener {

    private static ZenSleep instance;
    public static ArrayList<Float> sleepStatus = new ArrayList<>();
    public static int sleepConditions = Constant.SLEEP_STATUS_NONE;
    public static SleepDBModel tempModel = new SleepDBModel();
    public static int alarmHour;
    public static int alarmMin;
    public static int smartHour;
    public static int smartMin;
    public static String alarmTimeText;
    public static boolean sleepFinished = false;
    public static boolean powerConnected = false;

    public static final String TAG = "kiip";
    private static final String APP_KEY = "149256fb10e4a483b9e97ea5d362334d";
    private static final String APP_SECRET = "121b097994a708262e90e687f7fdbb44";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Set a global poptart queue to persist poptarts across Activities
        KiipFragmentCompat.setDefaultQueue(new LinkedList<Poptart>());

        // Instantiate and set the shared Kiip instance
        Kiip kiip = Kiip.init(this, APP_KEY, APP_SECRET);
        Kiip.setInstance(kiip);
        // Listen for Kiip events
        kiip.setOnContentListener(this);
    }

    @Override
    public void onContent(Kiip kiip, String content, int quantity, String transactionId, String signature) {
        Log.d(TAG, "onContent content=" + content + " quantity=" + quantity + " transactionId=" + transactionId + " signature=" + signature);

    }
}
