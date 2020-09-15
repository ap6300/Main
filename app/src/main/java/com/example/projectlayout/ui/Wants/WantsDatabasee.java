package com.example.projectlayout.ui.Wants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class WantsDatabasee {

    public static final String DB_NAME = "ListInformation";
    public static final String DB_TABLE = "Task";
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (item VARCHAR, checked INTERGER)";
    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public WantsDatabasee(Context c) {
        this.context = c;
        helper = new WantsDatabasee.SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public WantsDatabasee openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    Cursor getData(String sql){
        db= helper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public void close() {
        helper.close();
    }

    public boolean addRow( String f, int c) {
        synchronized(this.db) {

            ContentValues newFriend = new ContentValues();
            newFriend.put("item", f);
            newFriend.put("checked", c);

            try {
                db.insertOrThrow(DB_TABLE, null, newFriend);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }



    public void clearRecords(String name)
    {
        db = helper.getWritableDatabase();
        String sql = "DELETE FROM Task WHERE item = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);

        statement.execute();
    }

    void updateIsChecked(String name) {
        db = helper.getReadableDatabase();
        ContentValues newAlarm = new ContentValues();
        newAlarm.put("item",name);
        newAlarm.put("checked",1);

        db.update(DB_TABLE, newAlarm,  "item  = '" + name + "'",null);


        db.close();

    }

    void updateIsNotChecked(String name) {
        db = helper.getReadableDatabase();

        ContentValues newAlarm = new ContentValues();
        newAlarm.put("item",name);
        newAlarm.put("checked",0);

        db.update(DB_TABLE, newAlarm,  "item  = '" + name + "'",null);
        db.close();

    }


    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Task table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

}
