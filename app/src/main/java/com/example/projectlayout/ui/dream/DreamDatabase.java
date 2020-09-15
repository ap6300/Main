package com.example.projectlayout.ui.dream;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


public class DreamDatabase {

    private static final String DB_NAME = "Dreamboard";
    private static final String DB_TABLE = "Dreamboard";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (description VARCHAR, image BLOB);";

    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public DreamDatabase(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public DreamDatabase openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    Cursor getData(String sql) {
        db = helper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    void updateData(String name, byte[] image, String hold) {
        db = helper.getReadableDatabase();

        String sql = "UPDATE Dreamboard SET description = ?, image = ? WHERE description = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindString(3, hold);

        statement.execute();
        db.close();

    }


    public  void deleteData(String name) {
        db = helper.getReadableDatabase();

        String sql = "DELETE FROM Dreamboard WHERE description = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);

        statement.execute();
        db.close();

    }





    public boolean addRow(String d, byte[] image) {
        synchronized(this.db) {

            ContentValues newDream = new ContentValues();
            newDream.put("description", d);
            newDream.put("image", image);
            try {
                db.insertOrThrow(DB_TABLE, null, newDream);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            db.close();
            return true;
        }
    }


    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
    }

    public void close() {
        helper.close();
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




