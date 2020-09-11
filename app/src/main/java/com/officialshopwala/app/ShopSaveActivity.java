package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopSaveActivity extends AppCompatActivity {

    EditText businessNameEditText;
    EditText addressEditText;
    Button  shopActivityFinishButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public void saveShop(View view) {

        shopActivityFinishButton.setClickable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber="+919000990098";

        if (user != null) {
            phoneNumber = user.getPhoneNumber();
        }
        String businessName = businessNameEditText.getText().toString();
        String businessAddress = addressEditText.getText().toString();

        if ( phoneNumber.length()>=10 && businessName.length()>0 && businessAddress.length()>0 ) {

            databaseReference.child(phoneNumber).push().setValue("user");
            databaseReference.child(phoneNumber).child("businessName").setValue(businessName);
            databaseReference.child(phoneNumber).child("businessAddress").setValue(businessAddress);
            databaseReference.child(phoneNumber).child("businessLink").setValue("shopWala.com/businessName/1234");

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_save);

        businessNameEditText = findViewById(R.id.shopSaveActivityName);
        addressEditText = findViewById(R.id.shopSaveActivityAddress);
        shopActivityFinishButton = findViewById(R.id.shopSaveActivityFinishButton);

    }

}