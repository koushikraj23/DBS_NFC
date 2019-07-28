package com.example.dbs_nfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
public class dbHelper{
//public class dbHelper extends SQLiteOpenHelper {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final static String db_name="DBS";
    public final static String tb_name="User_Details";
    public final static String col1="ID";
    public final static String col2="Name";
    public final static String col3="Card";
    private static final String TAG = dbHelper.class.getName();
//    public dbHelper(@Nullable Context context) {
//        super(context, db_name, null, 1);
//
//    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
////db.execSQL("create table "+tb_name+" (ID Integer Primary Key AutoIncrement,Name text,Card text )");
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////db.execSQL("drop table if exists "+tb_name);
////onCreate(db);
//    }


    public void insert( String cardID, String name,String pswd){




        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("cardId", cardID);
        user.put("Pswd", pswd);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }


    public void readData( String cardID){


        Map<String, Object> user = new HashMap<>();

        user.put("cardId", cardID);
        db.collection("users")
                .whereEqualTo("cardId", cardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }
}
