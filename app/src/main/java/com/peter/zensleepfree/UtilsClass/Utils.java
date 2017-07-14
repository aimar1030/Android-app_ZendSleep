package com.peter.zensleepfree.UtilsClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by peter on 6/16/16.
 */
public class Utils {

    public static final String PREFERENCE_NAME = "pref_zen_sleep";

    public static float randomFloat() {
        Random rand = new Random();
        return rand.nextFloat();
    }

    public static int randomInt(int base) {
        Random rand = new Random();
        return Math.abs(rand.nextInt() % base);
    }

    public static float randomFloatBetween (float start, float end) {
        return start + (end - start) * randomFloat();
    }

    public static boolean is12(Context context) {
        int time_format = 0;
        try {
            time_format = Settings.System.getInt(context.getContentResolver(), Settings.System.TIME_12_24);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (time_format == 12) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDateFromSec(int seconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)seconds * 1000);
        return formatter.format(calendar.getTime());
    }

    public static String getHoursMinutes(int duration) {
        int hour = duration / 3600;
        int min = (duration % 3600)/60;
        return String.valueOf(hour) + "h " + String.valueOf(min) + "min";
    }

    public static int getColorForEfficienty(int efficienty) {
        if( efficienty >= 90 )
            return R.drawable.circle_background;
        else if( efficienty >= 80 )
            return R.drawable.circle_back_2;
//            c = [UIColor colorWithRed:32.f/255.0f green:177.0f/255.0f blue:153.0f/255.0f alpha:1];
        else if( efficienty >= 70 )
            return R.drawable.circle_back_3;
//            c = [UIColor colorWithRed:253.0f/255.0f green:185.0f/255.0f blue:20.0f/255.0f alpha:1];
        else
            return R.drawable.circle_back_4;
//        efficienty = [UIColor colorWithRed:240.0f/255.0f green:75.0f/255.0f blue:95.0f/255.0f alpha:1];
    }


    public static int getTopColor(float timeValue) {
        float r1 = 0.0f, g1 = 0.0f, b1 = 0.0f, t1 = 0.0f;
        float r2 = 0.0f, g2 = 0.0f, b2 = 0.0f, t2 = 0.0f;

        boolean flag = false;

        for( int i = 0 ; i < Constant.NUM_TIME_INDEX - 1 ; i++ )
        {
            if( Constant.TIME_INDEX_TABLE[i] <= timeValue && timeValue < Constant.TIME_INDEX_TABLE[i+1] )
            {
                t1 = Constant.TIME_INDEX_TABLE[i];           t2 = Constant.TIME_INDEX_TABLE[i+1];
                r1 = Constant.TIME_COLOR_TABLE[i][0][0];     r2 = Constant.TIME_COLOR_TABLE[i+1][0][0];
                g1 = Constant.TIME_COLOR_TABLE[i][0][1];     g2 = Constant.TIME_COLOR_TABLE[i+1][0][1];
                b1 = Constant.TIME_COLOR_TABLE[i][0][2];     b2 = Constant.TIME_COLOR_TABLE[i+1][0][2];

                flag = true;
                break;
            }
        }

        if( flag )
        {
            float ratio = (timeValue-t1)/(t2-t1);
            float r = r1 + (r2-r1)*ratio;
            float g = g1 + (g2-g1)*ratio;
            float b = b1 + (b2-b1)*ratio;

            return Color.argb(255, (int)r, (int)g, (int)b);
        }
        return 0;
    }

    public static int getBottomColor(float timeValue) {
        float r1 = 0.0f, g1 = 0.0f, b1 = 0.0f, t1 = 0.0f;
        float r2 = 0.0f, g2 = 0.0f, b2 = 0.0f, t2 = 0.0f;

        boolean flag = false;

        for( int i = 0 ; i < Constant.NUM_TIME_INDEX-1 ; i++ )
        {
            if( Constant.TIME_INDEX_TABLE[i] <= timeValue && timeValue < Constant.TIME_INDEX_TABLE[i+1] )
            {
                t1 = Constant.TIME_INDEX_TABLE[i];           t2 = Constant.TIME_INDEX_TABLE[i+1];
                r1 = Constant.TIME_COLOR_TABLE[i][1][0];     r2 = Constant.TIME_COLOR_TABLE[i+1][1][0];
                g1 = Constant.TIME_COLOR_TABLE[i][1][1];     g2 = Constant.TIME_COLOR_TABLE[i+1][1][1];
                b1 = Constant.TIME_COLOR_TABLE[i][1][2];     b2 = Constant.TIME_COLOR_TABLE[i+1][1][2];

                flag = true;
                break;
            }
        }

        if( flag )
        {
            float ratio = (timeValue-t1)/(t2-t1);
            float r = r1 + (r2-r1)*ratio;
            float g = g1 + (g2-g1)*ratio;
            float b = b1 + (b2-b1)*ratio;

            return Color.argb(255, (int)r, (int)g, (int)b);
        }
        return 0;
    }

    // Shared Preferences
    public static void setHidePrepareSleep(Context context, boolean hide) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hide_prepare_sleep", hide);
        editor.commit();
    }

    public static boolean getHidePrepareSleep(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getBoolean("hide_prepare_sleep", false);
    }

    public static void setEnableAlarm(Context context, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("enable_alarm", isEnabled);
        editor.commit();
    }

    public static boolean getEnableAlarm(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getBoolean("enable_alarm", false);
    }

    public static void setAlarmVolume(Context context, int volume) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarm_volume", volume);
        editor.commit();
    }

    public static int getAlarmVolume(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("alarm_volume", 50);
    }

    public static void setAlarmSound(Context context, String sound) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("alarm_sound", sound);
        editor.commit();
    }

    public static String getAlarmSound(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getString("alarm_sound", Constant.ALARM_SOUNDS[0]);
    }

    public static void setAlarmSoundIndex(Context context, int index) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarm_sound_index", index);
        editor.commit();
    }

    public static int getAlarmSoundIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("alarm_sound_index", 0);
    }

    public static void setEnableSmartAlarm(Context context, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("smart_alarm_enable", isEnabled);
        editor.commit();
    }

    public static boolean getEnableSmartAlarm(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getBoolean("smart_alarm_enable", false);
    }

    public static void setSmartAlarmCoolTime(Context context, int coolTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("smart_alarm_cool_time", coolTime);
        editor.commit();
    }

    public static int getSmartAlarmCoolTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("smart_alarm_cool_time", 30);
    }

    public static void setEnableSnooze(Context context, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("snooze_alarm_enable", isEnabled);
        editor.commit();
    }

    public static boolean getEnableSnooze(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getBoolean("snooze_alarm_enable", false);
    }

    public static void setSnoozeCoolTime(Context context, int coolTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("snooze_alarm_cool_time", coolTime);
        editor.commit();
    }

    public static int getSnoozeCoolTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("snooze_alarm_cool_time", 10);
    }

    public static void setAlarmTimeHour(Context context, int hour) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarm_hour", hour);
        editor.commit();
    }

    public static int getAlarmTimeHour(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("alarm_hour", 6);
    }

    public static void setAlarmTimeMin(Context context, int min) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarm_min", min);
        editor.commit();
    }

    public static int getAlarmTimeMin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return sharedPreferences.getInt("alarm_min", 30);
    }

    public static void setPurchased(Context context, boolean purchased) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("purchased", purchased);
    }

//    public static boolean getPurchased(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
//        return sharedPreferences.getBoolean("purchased", false);
//    }

    public static boolean hasInternetConnection(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {

                return true;
            }

            NetworkInfo mobileNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {

                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {

                return true;
            }
        }
        return false;
    }

    public static String getMoreApps(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        String more_apps = settings.getString("more_apps", "");
        return more_apps;
    }

    public static void setMoreApps(Context context, String more_apps) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("more_apps", more_apps);
        editor.commit();
    }

    public static void setShowShareTip(Context context, boolean flag) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("show_share_tips", flag);
        editor.commit();
    }

    public static boolean getShowShareTip(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return settings.getBoolean("show_share_tips", false);
    }

    public static void setDriveSync(Context context, boolean flag) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("google_drive_sync", flag);
        editor.commit();
    }

    public static boolean getDriveSync(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return settings.getBoolean("google_drive_sync", false);
    }

    public static String readFile(String path) {

        String stringText = "";

        try {
            URLConnection conn = new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String StringBuffer;

            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return stringText;
    }

    public static boolean getBooleanFromSharedPreferences(Context context, String key, boolean defaultValue) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        boolean value = sharedPreferences.getBoolean(key, defaultValue);
        return value;
    }

    public static boolean getBooleanFromSharedPreferences(Context context, String key) {
        return getBooleanFromSharedPreferences(context, key, true);
    }

    public static String writeJSON(SleepDBModel sleepDBModel) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.jID, sleepDBModel.getId());
            jsonObject.put(Constant.jSTATUS, sleepDBModel.getStatus());
            jsonObject.put(Constant.jSTART_TIME, sleepDBModel.getStartTimeSec());
            jsonObject.put(Constant.jELAPSED_TIME, sleepDBModel.getElapsedSec());
            jsonObject.put(Constant.jSTAGES_LOT, sleepDBModel.getStagesLotSec());
            jsonObject.put(Constant.jMOOD, sleepDBModel.getMood());
            jsonObject.put(Constant.jDREAM, sleepDBModel.getDream());
            jsonObject.put(Constant.jNOTE, sleepDBModel.getNote());
            JSONArray stagesArray = new JSONArray();

            for (int i = 0; i < sleepDBModel.getStages().length; i++) {
                stagesArray.put(sleepDBModel.getStages()[i]);
            }
            jsonObject.put(Constant.jSTAGES, stagesArray);
            Log.d("JSON Object writing", jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}