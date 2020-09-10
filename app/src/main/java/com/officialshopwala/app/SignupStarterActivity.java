package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignupStarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_starter);
    }

    public void startSelling (View view) {
        startActivity(new Intent(SignupStarterActivity.this, MainActivity.class));
        finish();
    }
}