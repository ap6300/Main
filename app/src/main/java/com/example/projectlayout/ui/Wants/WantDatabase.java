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

    private static final String DB_NAME = "Want";
    private static final String DB_TABLE = "Want";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (item VARCHAR, listOrder INTERGER)";
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

    Cursor getData(String sql) {
        db = helper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }



    ArrayList<want> getWantItem(){
        ArrayList<want> item = new ArrayList<>();
        db= helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Want ORDER BY listOrder", null);
        while (cursor.moveToNext()) {
            String task = cursor.getString(0);
            int checked = cursor.getInt(1);

            item.add(new want( task, checked));
        }
        cursor.close();
        return item;
    }


    public void close() {
        helper.close();
    }

    public boolean addRow( String f, int c) {
        synchronized(this.db) {

            ContentValues newFriend = new ContentValues();
            newFriend.put("item", f);
            newFriend.put("listOrder", c);

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

    String getListOrder(int position){

        db= helper.getReadableDatabase();
        String sql = "SELECT * FROM Want where listOrder = \""+position+"\";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        String task = cursor.getString(0);
        cursor.close();
        return task;
    }

    void updateListOrder(String item, int newPosition){

        db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("UPDATE Want SET listOrder = "+newPosition+ " WHERE item = \""+item+"\"", null);
        cursor.moveToNext();
        cursor.close();

        db.close();

    }

    void update(String item,String newitem){

        db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("UPDATE Want SET  item = \""+newitem +"\" WHERE item = \""+item+"\"", null);
        cursor.moveToNext();
        cursor.close();

        db.close();

    }
    int count(){
        db= helper.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }


    public void clear()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
    }


    public void clearRecords(String name)
    {
        db = helper.getWritableDatabase();
        String sql = "DELETE FROM Want WHERE item = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);

        statement.execute();
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

