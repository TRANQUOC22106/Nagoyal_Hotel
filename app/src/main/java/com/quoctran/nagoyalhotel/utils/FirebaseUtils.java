package com.quoctran.nagoyalhotel.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import static android.content.ContentValues.TAG;

public class FirebaseUtils {
    public static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    public static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    public static final String PROFILES_REF = "profiles";

    public static FirebaseUser getCurrentUser() {
        return FIREBASE_AUTH.getCurrentUser();
    }

    public static void signOut() {
        FIREBASE_AUTH.signOut();
    }

    /*
    * Add new Object data with auto-generate ID
    * */
    public static void addData(String ref, Object data) {
        DB.collection(ref)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " +
                                documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /*
    * Update object in a collection
    * */
    public static void updateAObject(String ref, String field, Object data) {
        DB.collection(ref).document(field).set(data, SetOptions.merge());
    }

    /*
    * Update a property in a object
    * */
    public static void updateAOjectProperty(String ref, String field, String propertyName, Object data) {
        DocumentReference washingtonRef = DB.collection(ref).document(field);
        washingtonRef.update(propertyName, data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public static DocumentReference getDocument(String ref, String document) {
        return getRef(ref).document(document);
    }

    public static CollectionReference getRef(String ref) {
        return DB.collection(ref);
    }
}
