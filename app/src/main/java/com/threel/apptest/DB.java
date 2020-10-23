package com.threel.apptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    public static final int DB_VERSION=1;
    public static final String DB_NAME="info.db";
    public static final String CREATE_QUERT="CREATE TABLE IF NOT EXISTS "+ "UsersById"+"("
            +"id text primary key,"+ "name text,"+"surname text," +"email text,"+"avatar blob,"+"color text,"+"pseudonim text,"+"changed boolean,"+"backgroundColor text);";
    public static final String CREATE_QUERY="CREATE TABLE IF NOT EXISTS "+ "UsersByPage"+"("
            +"id text primary key,"+ "name text,"+"surname text," +"email text,"+"avatar blob,"+"color text,"+"pseudonim text,"+"changed text,"+"backgroundColor text);";
    public static final String CREATE_QUERY_GROUP="CREATE TABLE " + "Groups"+ "("+"name text primary key)";

    DB(Context c){
        super(c,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(CREATE_QUERT);
        sqLiteDatabase.execSQL(CREATE_QUERY);
        sqLiteDatabase.execSQL(CREATE_QUERY_GROUP);
//
    }
    public void addData(SQLiteDatabase db,String id, String name, String surname, String email, byte[] avatar , String color, String pseudonim, String changed,String backgroundColor){

            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("name", name);
            contentValues.put("surname", surname);
            contentValues.put("email", email);
            contentValues.put("avatar", avatar);
            contentValues.put("color", color);
            contentValues.put("pseudonim", pseudonim);
            contentValues.put("changed", changed);
        contentValues.put("backgroundColor",backgroundColor);
            try {
                db.insertOrThrow("UsersById", null, contentValues);
            } catch (SQLiteConstraintException e) {
                System.out.println("exist");
            }
        db.close();


    }
    public void addUsersData(SQLiteDatabase db,String id, String name, String surname, String email, byte[] avatar , String color, String pseudonim, String changed,String backgroundColor){

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        contentValues.put("avatar", avatar);
        contentValues.put("color", color);
        contentValues.put("pseudonim", pseudonim);
        contentValues.put("changed", changed);
        contentValues.put("backgroundColor",backgroundColor);
        try {
            db.insertOrThrow("UsersByPage", null, contentValues);
        } catch (SQLiteConstraintException e) {
            System.out.println("exist");
        }
        db.close();


    }
    public void updateUsersList(SQLiteDatabase db,String backgroundColor,String id){
        ContentValues contentValues=new ContentValues();
        contentValues.put("backgroundColor",backgroundColor);
        db.update("UsersByPage", contentValues, "id=" + id, null);
//

    }
    public void updateUsers(SQLiteDatabase db,String name,String surname,String email,byte[] avatar, String color, String pseudonim, String id){
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("surname",surname);
        contentValues.put("email",email);
        contentValues.put("color",color);
        contentValues.put("avatar", avatar);
        contentValues.put("pseudonim",pseudonim);
        db.update("UsersByPage", contentValues, "id=" + id, null);
    }
    public void adgrElement(SQLiteDatabase db,String name){

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        try {
            db.insertOrThrow("Groups", null, contentValues);
        } catch (SQLiteConstraintException e) {
            System.err.println("exist");
        }
        db.close();


    }
    public Cursor getTables(SQLiteDatabase db){

        String[] information = {"name"};
        Cursor cursor = db.query("Groups", information, null, null, null, null, null, null);
        return cursor;
    }
    public Cursor getId(SQLiteDatabase db){
        String[] information={"id","backgroundColor"};
        Cursor cursor= db.query("UsersByPage",information,null,null,null,null,null,null);
        return cursor;
    }
    public Cursor getUserData(SQLiteDatabase db){
        String [] information={"id","name","surname","email","avatar","color","pseudonim","changed","backgroundColor"};
        Cursor cursor=db.query("UsersByPage",information, null,null,null,null,null,null);
        return cursor ;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UsersById");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UsersByPage");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Groups");
    }
}
