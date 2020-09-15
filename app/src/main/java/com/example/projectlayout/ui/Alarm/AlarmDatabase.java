package com.example.projectlayout.ui.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDatabase {


    private static final String DB_NAME = "Alarm";
    private static final String DB_TABLE = "Alarm";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, hour INTEGER, min INTEGER, description VARCHAR, alarmOn INTEGER, recurring INTEGER," +
            "mon INTEGER, tue INTEGER, wed INTEGER, thur INTEGER, fri INTEGER, sat INTEGER, sun INTEGER, image BLOB);";

    private AlarmDatabase.SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public AlarmDatabase(Context c) {
        this.context = c;
        helper = new AlarmDatabase.SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public AlarmDatabase openReadable() throws android.database.SQLException {
        helper = new AlarmDatabase.SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    Cursor getData(String sql) {
        db = helper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public boolean addRow(int hour, int min, String description, int alarmOn, int recurring, int mon, int tue, int wed, int thur, int fri, int sat, int sun, byte[] image) {
        synchronized(this.db) {

            ContentValues newAlarm = new ContentValues();
            newAlarm.put("hour",hour);
            newAlarm.put("min",min);
            newAlarm.put("description", description);
            newAlarm.put("alarmOn",alarmOn);
            newAlarm.put("recurring",recurring);
            newAlarm.put("mon",mon);
            newAlarm.put("tue",tue);
            newAlarm.put("wed",wed);
            newAlarm.put("thur",thur);
            newAlarm.put("fri",fri);
            newAlarm.put("sat",sat);
            newAlarm.put("sun",sun);
            newAlarm.put("image", image);
            try {
                db.insertOrThrow(DB_TABLE, null, newAlarm);

            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            db.close();
            return true;
        }
    }


    boolean updateData(int id, int hour, int min, String description, int alarmOn, int recurring, int mon, int tue, int wed, int thur, int fri, int sat, int sun, byte[] image) {
        db = helper.getReadableDatabase();

        ContentValues newAlarm = new ContentValues();
        newAlarm.put("hour",hour);
        newAlarm.put("min",min);
        newAlarm.put("description", description);
        newAlarm.put("alarmOn",alarmOn);
        newAlarm.put("recurring",recurring);
        newAlarm.put("mon",mon);
        newAlarm.put("tue",tue);
        newAlarm.put("wed",wed);
        newAlarm.put("thur",thur);
        newAlarm.put("fri",fri);
        newAlarm.put("sat",sat);
        newAlarm.put("sun",sun);
        newAlarm.put("image", image);
        try {
            db.update(DB_TABLE, newAlarm,  "id  = '" + id + "'",null);

        } catch (Exception e) {
            Log.e("Error in inserting rows", e.toString());
            e.printStackTrace();
            return false;
        }
        db.close();
        return true;

    }
    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
    }

    public void close() {
        helper.close();
    }


    public  void deleteData(int id) {
        db = helper.getReadableDatabase();

        db = helper.getWritableDatabase();
        db.delete(DB_TABLE,   "id = \"" + id + "\"", null);

    }



    public static class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }




        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Products table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }



    }


}
