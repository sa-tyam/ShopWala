package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;

    ImageView shopwalaLogoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        shopwalaLogoImage = findViewById(R.id.logoImageView);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        shopwalaLogoImage.animate().rotationBy(360).setDuration(200);

        final String businessName;

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Intent signupIntent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                        startActivity(signupIntent);
                    }
                    finish();
                }
            }, 4000);
        } catch(Exception e){
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Intent signupIntent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                startActivity(signupIntent);
            }
            finish();
        }
    }
}