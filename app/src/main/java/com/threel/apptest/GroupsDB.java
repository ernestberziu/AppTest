package com.threel.apptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupsDB extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "grous.db";
    static String data = Demo.getGroup_name();
    public static String TABLE_NAME = data;


    GroupsDB(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TABLE_NAME = Demo.getTable_name();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "id text primary key, backgroundColor text);");

    }
    public void addData(SQLiteDatabase db, String id, String backgroundColor, String table) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("backgroundColor", backgroundColor);
        try {
            db.insertOrThrow(table, null, contentValues);
        } catch (SQLiteConstraintException e) {
            System.out.println("exist");
        }


    }
    public void updategroupList(SQLiteDatabase db, String backgroundColor, String id) {
        TABLE_NAME = Demo.getGroup_name();
        ContentValues contentValues = new ContentValues();
        contentValues.put("backgroundColor", backgroundColor);
        try {
            db.update(TABLE_NAME, contentValues, "id=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }
}
