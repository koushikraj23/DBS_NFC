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
import com.google.firebase.firestore.auth.User;

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
    public   static UserDetails user=new UserDetails();
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





        user.setName(name);
        user.setCardId(cardID);
        user.setPswd(pswd);
        db.collection("users")
                .whereEqualTo("cardId", cardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserDetails u=document.toObject(UserDetails.class);
                                Log.d(TAG, document.getId() + " => Already present");

                            }
                        } else {
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
                });
// Add a new document with a generated ID



    }


    public UserDetails readTagData( String cardID){


//   Map<String, Object> user = new HashMap<>();


      //  user.put("cardId", cardID);
        db.collection("users")
                .whereEqualTo("cardId", cardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 UserDetails uTemp=document.toObject(UserDetails.class);
                                Log.d(TAG, document.getId() + " => " +uTemp.getCardId()+uTemp.getName());
                                user.setName(uTemp.getName());
                                Log.d(TAG, user.getName());
                                user.setCardId(uTemp.getCardId());
                                user.setPswd(uTemp.getPswd());


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());


                        }
                    }
                });
        Log.d(TAG,user.getCardId()+"--'"+user.getName());

        return user;
    }
}
