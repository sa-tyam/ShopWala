package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        phoneNumber = findViewById(R.id.phoneNumberEditText);
    }

    public void sendCodeForVerification (View view) {
        Intent myIntent = new Intent (SignupActivity.this, OTPVerificationActivity.class);
        myIntent.putExtra("phoneNumber", phoneNumber.getText().toString());
        startActivity(myIntent);
        finish();
    }
}