package com.peter.zensleepfree.UtilsClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.peter.zensleepfree.Model.SleepDBModel;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by peter on 6/25/16.
 */
public class SleepDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sleepdata";
    private static final String TABLE_NAME = "allsleep";
    private static final String KEY_ID = "identifier";
    private static final String KEY_STATUS = "status";
    private static final String KEY_START = "starttime";
    private static final String KEY_ELAPSED = "elaspedsec";
    private static final String KEY_STAGES_LOT = "stageslotsec";
    private static final String KEY_MOOD = "mood";
    private static final String KEY_DREAM = "dream";
    private static final String KEY_NOTE = "note";
    private static final String KEY_STAGES = "stages";
    private static final String[] COLUMNS = { KEY_ID, KEY_STATUS, KEY_START, KEY_ELAPSED, KEY_STAGES_LOT, KEY_MOOD, KEY_DREAM, KEY_NOTE, KEY_STAGES };


    public SleepDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //    identifier text, status integer, starttime double, elaspedsec integer, stageslotsec integer, mood integer, dream integer, note text, stages blob
        String CREATE_DB = "CREATE TABLE " + TABLE_NAME + "( " + "identifier TEXT PRIMARY KEY, " + "status INTEGER, " + "starttime INTEGER, " + "elaspedsec INTEGER, " + "stageslotsec INTEGER, "
                + "mood INTEGER, " + "dream INTEGER, " + "note TEXT, " + "stages BLOB )";
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addSleepData(SleepDBModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, data.getId());
        values.put(KEY_STATUS, data.getStatus());
        values.put(KEY_START, data.getStartTimeSec());
        values.put(KEY_ELAPSED, data.getElapsedSec());
        values.put(KEY_STAGES_LOT, data.getStagesLotSec());
        values.put(KEY_MOOD, data.getMood());
        values.put(KEY_DREAM, data.getDream());
        values.put(KEY_NOTE, data.getNote());
        values.put(KEY_STAGES, convertFloatToByte(data.getStages()));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public SleepDBModel getSleepData(String dbID) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " identifier = ? ", new String[]{dbID}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        SleepDBModel sleepDBModel = new SleepDBModel();
        sleepDBModel.setId(cursor.getString(0));
        sleepDBModel.setStatus(cursor.getInt(1));
        sleepDBModel.setStartTimeSec(cursor.getInt(2));
        sleepDBModel.setElapsedSec(cursor.getInt(3));
        sleepDBModel.setStagesLotSec(cursor.getInt(4));
        sleepDBModel.setMood(cursor.getInt(5));
        sleepDBModel.setDream(cursor.getInt(6));
        sleepDBModel.setNote(cursor.getString(7));
        sleepDBModel.setStages(convertByteToFloat(cursor.getBlob(8)));
        db.close();

        return sleepDBModel;
    }

    public ArrayList<SleepDBModel> getAllData() {
        ArrayList<SleepDBModel> results = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        SleepDBModel sleepDBModel = null;
        if (cursor.moveToFirst()) {
            do {
                sleepDBModel = new SleepDBModel();
                sleepDBModel.setId(cursor.getString(0));
                sleepDBModel.setStatus(cursor.getInt(1));
                sleepDBModel.setStartTimeSec(cursor.getInt(2));
                sleepDBModel.setElapsedSec(cursor.getInt(3));
                sleepDBModel.setStagesLotSec(cursor.getInt(4));
                sleepDBModel.setMood(cursor.getInt(5));
                sleepDBModel.setDream(cursor.getInt(6));
                sleepDBModel.setNote(cursor.getString(7));
                sleepDBModel.setStages(convertByteToFloat(cursor.getBlob(8)));
                results.add(sleepDBModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return results;
    }

    public int updateData(SleepDBModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, data.getId());
        values.put(KEY_STATUS, data.getStatus());
        values.put(KEY_START, data.getStartTimeSec());
        values.put(KEY_ELAPSED, data.getElapsedSec());
        values.put(KEY_STAGES_LOT, data.getStagesLotSec());
        values.put(KEY_MOOD, data.getMood());
        values.put(KEY_DREAM, data.getDream());
        values.put(KEY_NOTE, data.getNote());
        values.put(KEY_STAGES, convertFloatToByte(data.getStages()));

        int i = db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{data.getId()});
        db.close();
        return i;
    }

    public void deleteSleepData(SleepDBModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{data.getId()});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    private byte[] convertFloatToByte(float[] raw) {
        ByteBuffer result = ByteBuffer.allocate(4 * raw.length);

        for (int i = 0; i < raw.length; i++) {
            result.putFloat(4 * i, raw[i]);
        }
        return result.array();
    }

    private float[] convertByteToFloat(byte[] raw) {
        float[] result = new float[raw.length/4];
        for (int i = 0; i < raw.length/4; i++) {
            float temp = ByteBuffer.wrap(raw, i * 4, 4).getFloat();
            result[i] = temp;
        }
        return result;
    }
}
