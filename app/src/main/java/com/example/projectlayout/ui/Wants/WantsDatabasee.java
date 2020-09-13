package com.example.projectlayout.ui.Wants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;

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

    public ArrayList<String> retrieveRows() {
        ArrayList<String> productRows = new ArrayList<String>();
        String[] columns = new String[] {"", "", "", ""};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            productRows.add( cursor.getString(0) );
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return productRows;
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
