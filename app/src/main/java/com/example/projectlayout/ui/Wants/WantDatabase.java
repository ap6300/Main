package com.example.projectlayout.ui.Wants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class WantDatabase {

    private static final String DB_NAME = "NewTask";
    private static final String DB_TABLE = "NewTask";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (item VARCHAR, checked INTERGER)";
    private WantDatabase.SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public WantDatabase(Context c) {
        this.context = c;
        helper = new WantDatabase.SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public WantDatabase openReadable() throws android.database.SQLException {
        helper = new WantDatabase.SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }



    protected ArrayList<want> getWantItem(){
        ArrayList<want> item = new ArrayList<>();
        db= helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NewTask", null);
        while (cursor.moveToNext()) {
            String task = cursor.getString(0);
            int checked = cursor.getInt(1);

            item.add(new want( task, checked));
        }
        return item;
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
                db.insert(DB_TABLE,null,newFriend);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public void clear()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
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

