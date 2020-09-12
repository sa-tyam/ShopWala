package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDetailsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String phoneNumber = "+919000990098";

    TextView orderDetailOrderId;
    TextView orderDetailOrderStatus;
    TextView orderDetailOrderTime;
    TextView orderDetailOrderItemTotalNumber;
    TextView orderDetailOrderItemTotal;
    TextView orderDetailDeliveryTotal;
    TextView orderDetailOrderGrandTotal;
    TextView orderDetailCustomerName;
    TextView orderDetailCustomerMobile;
    TextView orderDetailCustomerAddress;
    TextView orderDetailCustomerPinCode;
    TextView orderDetailCustomerPayment;
    Button orderDetailDeclineButton;
    Button orderDetailAcceptButton;

    public void orderDetailBackButtonCicked (View view) {
        finish();
    }

    public void declineOrder (View view) {

    }

    public void acceptOrder (View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            phoneNumber = user.getPhoneNumber();
        }
    }

    private void initViews() {
        orderDetailOrderId = findViewById(R.id.orderDetailOrderId);
        orderDetailOrderStatus = findViewById(R.id.orderDetailOrderStatus);
        orderDetailOrderTime = findViewById(R.id.orderDetailOrderTime);
        orderDetailOrderItemTotalNumber = findViewById(R.id.orderDetailOrderItemTotalNumber);
        orderDetailOrderItemTotal = findViewById(R.id.orderDetailOrderItemTotal);
        orderDetailDeliveryTotal = findViewById(R.id.orderDetailDeliveryTotal);
        orderDetailOrderGrandTotal = findViewById(R.id.orderDetailOrderGrandTotal);
        orderDetailCustomerName = findViewById(R.id.orderDetailCustomerName);
        orderDetailCustomerMobile = findViewById(R.id.orderDetailCustomerMobile);
        orderDetailCustomerAddress = findViewById(R.id.orderDetailCustomerAddress);
        orderDetailCustomerPinCode = findViewById(R.id.orderDetailCustomerPinCode);
        orderDetailCustomerPayment = findViewById(R.id.orderDetailCustomerPayment);
        orderDetailDeclineButton = findViewById(R.id.orderDetailDeclineButton);
        orderDetailAcceptButton = findViewById(R.id.orderDetailAcceptButton);

    }
}