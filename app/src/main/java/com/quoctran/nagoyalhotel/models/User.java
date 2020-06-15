package com.quoctran.nagoyalhotel.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;

import static android.content.ContentValues.TAG;

public class User extends Profiles{
    private String uid, email, password;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void getCurrentUser() {
        FirebaseUser firebaseUser = FirebaseUtils.getCurrentUser();
            Thread getUserProfiles = new Thread(new Runnable() {
                @Override
                public void run() {
                    setUid(firebaseUser.getUid());
                    setEmail(getEmail());
                    DocumentReference ref =
                            FirebaseUtils.getDocument(FirebaseUtils.PROFILES_REF, firebaseUser.getUid());
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Profiles profiles = (Profiles) document.getData();
                                    setName(profiles.getName());
                                    setPhone(profiles.getPhone());
                                    setPhone(profiles.getAvatar());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            }
                        }
                    });
                }
            });
            getUserProfiles.start();
    }
}
