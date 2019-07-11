package com.example.dbs_nfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    public final static String db_name="DBS";
    public final static String tb_name="User_Details";
    public final static String col1="ID";
    public final static String col2="Name";
    public final static String col3="Card";
    private static final String TAG = dbHelper.class.getName();
    public dbHelper(@Nullable Context context) {
        super(context, db_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("create table "+tb_name+" (ID Integer Primary Key AutoIncrement,Name text,Card text )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("drop table if exists "+tb_name);
onCreate(db);
    }


    public void insert(String db, String cardID, String name){
        SQLiteDatabase sq=this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put(col2,name);
        content.put(col3,cardID);
        long insert = db.insert(tb_name,null, content);
        if(insert>0){
            Log.e(TAG, "insert: " );
        }
        else{

        }


    }
}
