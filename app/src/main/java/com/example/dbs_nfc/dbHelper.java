package com.example.dbs_nfc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    public final static String db_name="DBS";
    public final static String tb_name="User_Details";
    public final static String Col1="ID";
    public final static String Col2="Name";
    public dbHelper(@Nullable Context context) {
        super(context, db_name, null, 1);
        SQLiteDatabase sq=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("create table "+tb_name+" (ID Integer Primary Key AutoIncrement,Name text )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("drop table if exists "+tb_name);
onCreate(db);
    }
}
