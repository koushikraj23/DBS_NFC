package com.example.dbs_nfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private  static  UserDetails user;
    private static final String TAG = dbHelper.class.getName();



    public void insert(final String cardID, String dbsID, String pswd, final String uuid){




System.out.println("Insert opertaion"+uuid+"------------"+cardID);
        user.setDbsID(dbsID);
        user.setCardId(cardID);
        user.setPswd(pswd);
        user.setUuid(uuid);
final  String card=cardID;

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




    public UserDetails readTagDataAsyn(final MainActivity.readCallback rCallback,final String cardID,final String uuid){

//   Map<String, Object> user = new HashMap<>();

        user=new UserDetails();

        //  user.put("cardId", cardID);
        db.collection("users")
                .whereEqualTo("cardId", cardID)
                .whereEqualTo("uuid", uuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserDetails uTemp=document.toObject(UserDetails.class);
                                user.setDbsID(uTemp.getDbsID());
                                user.setCardId(uTemp.getCardId());
                                user.setPswd(uTemp.getPswd());

                            }
                                if(user.getDbsID()==null){

                                    db.collection("users")
                                            .whereEqualTo("cardId", cardID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        UserDetails uTemp=new UserDetails();
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            uTemp=document.toObject(UserDetails.class);
                                                        }
                                                        if(uTemp.getDbsID()==null){


                                                            db.collection("users")
                                                                    .whereEqualTo("uuid", uuid)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {

                                                                                UserDetails uTemp=new UserDetails();
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    uTemp = document.toObject(UserDetails.class);
                                                                                }
                                                                                if(uTemp.getDbsID()!=null){

                                                                                    Log.d(TAG, " => Already present for current device");
                                                                                    user.setFlag(2);
                                                                                    rCallback.onCallback(user);
                                                                                }

else {
                                                                                    user.setFlag(0);
                                                                                    rCallback.onCallback(user);

                                                                                }

                                                                            }
                                                                            else {
                                                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });


                                                        }
                                                        else {

                                                            Log.d(TAG, " => Already present for current card");
                                                            user.setFlag(1);
                                                            rCallback.onCallback(user);
                                                        }

                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());

                                                    }
                                                }
                                            });
                                }
                                else {
                                    user.setFlag(0);
                                    rCallback.onCallback(user);
                                }

//

                        } else {

                            Log.d(TAG, "Error getting documents: ", task.getException());


                        }
                    }
                });

        Log.d(TAG,user.getCardId()+"--'"+user.getDbsID());

        return user;
    }



}
