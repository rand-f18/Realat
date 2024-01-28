package com.example.secondactivity;


import static android.widget.Toast.LENGTH_SHORT;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="financeManager";
    public static final String TABLE_NAME = "financeTable";
    public static final String col_1 = "ID";
    public static final String col_2 = "Type";
    public static final String col_3 = "Category";
    public static final String col_4 = "Amount";
    public static final String col_5 = "Date";
    public DataBaseHelper(@Nullable Context context) {
        super(context ,DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Type TEXT, Category TEXT, Amount real,Date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String type , String category ,double amount, String date ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, type);
        contentValues.put(col_3, category);
        contentValues.put(col_4, amount);
        contentValues.put(col_5, date);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else {
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();

    }
}



