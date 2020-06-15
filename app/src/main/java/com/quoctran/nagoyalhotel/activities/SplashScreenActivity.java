package com.quoctran.nagoyalhotel.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quoctran.nagoyalhotel.MainActivity;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
        Intent intent = new Intent(this, currentUser == null
                ? LoginActivity.class : MainActivity.class);
        startActivity(intent);
        finish();
    }
}
