package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetDeliveryChargeActivity extends AppCompatActivity {

    EditText deliveryChargeEditText;
    EditText freeDeliveryOverEditText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public void backFromSetDeliveryCharge(View view) {
        finish();
    }

    public void saveDeliveryCharge (View view) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");

        String deliveryChargeString = deliveryChargeEditText.getText().toString();
        String deliveryChargeFreeOrderString = freeDeliveryOverEditText.getText().toString();

        int deliveryCharge;
        int deliveryChargeFreeOrder;

        if (deliveryChargeString.equals("")) {
            deliveryCharge = 0;
        } else {
            deliveryCharge = Integer.parseInt(deliveryChargeString);
        }

        if (deliveryChargeFreeOrderString.equals("")) {
            deliveryChargeFreeOrder = 0;
        } else {
            deliveryChargeFreeOrder = Integer.parseInt(deliveryChargeFreeOrderString);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("deliveryCharges").child("deliveryCharge").setValue(deliveryCharge);
        databaseReference.child(phoneNumber).child("deliveryCharges").child("deliveryChargeFreeOrder").setValue(deliveryChargeFreeOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SetDeliveryChargeActivity.this, "delivery charge Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_delivery_charge);
        deliveryChargeEditText = findViewById(R.id.deliveryChargeperOrderEditText);
        freeDeliveryOverEditText = findViewById(R.id.deliveryChargeFreeDeliveryEditText);
    }
}