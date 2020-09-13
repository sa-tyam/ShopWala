package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String phoneNumber = "+919000990098";
    Long orderId;
    String buyerPhoneNumber = "";

    int acceptingOrderPrice = 0;

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
    Button orderDetailNegativeButton;
    Button orderDetailPositiveButton;

    public void orderDetailBackButtonCicked (View view) {
        finish();
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
        orderId = getIntent().getLongExtra("orderNumber", -1);
        getOrderFromDatabase();
        Log.i("buyer phone Number", buyerPhoneNumber);
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
        orderDetailNegativeButton = findViewById(R.id.orderDetailNegativeButton);
        orderDetailPositiveButton = findViewById(R.id.orderDetailPositiveButton);

    }

    public void getOrderFromDatabase() {
        orderDetailOrderId.setText("#"+String.valueOf(orderId));
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("orderStatus")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailOrderStatus.setText(keyNode.getValue(String.class));
                            setButtonActions(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("orderTime")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailOrderTime.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("itemCount")){
                        if(keyNode.getValue(Integer.class)!=null) {
                            orderDetailOrderItemTotalNumber.setText(keyNode.getValue(Integer.class) + " ITEMS");
                        }
                    }
                    if (keyNode.getKey().equals("price")){
                        if(keyNode.getValue(Integer.class)!=null) {
                            orderDetailOrderItemTotal.setText("$"+keyNode.getValue(Integer.class));
                            setPrice(keyNode.getValue(Integer.class));
                        }
                    }
                    if (keyNode.getKey().equals("payment")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerPayment.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("buyerMobile")){
                        if(keyNode.getValue(String.class)!=null) {
                            buyerPhoneNumber = keyNode.getValue(String.class);
                            if (buyerPhoneNumber.length()>=10) {
                                setCustomerDetailFromDatabase(buyerPhoneNumber);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setCustomerDetailFromDatabase (String buyerPhoneNumber) {
        orderDetailCustomerMobile.setText(buyerPhoneNumber);
        databaseReference.child("Buyers").child(buyerPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("name")) {
                        if (keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerName.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("address")) {
                        if (keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerAddress.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("pinCode")) {
                        if (keyNode.getValue(Integer.class)!=null) {
                            orderDetailCustomerPinCode.setText(String.valueOf(keyNode.getValue(Integer.class)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setPrice (final int price) {
        databaseReference.child("Sellers").child(phoneNumber).child("deliveryCharges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int charge = 0;
                int freeOver = 0;
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("deliveryCharge")) {
                        if (keyNode.getValue(Integer.class)!=null) {
                            charge = keyNode.getValue(Integer.class);
                        }
                    }
                    if (keyNode.getKey().equals("deliveryChargeFreeOrder")) {
                        if (keyNode.getValue(Integer.class)!=null) {
                            freeOver = keyNode.getValue(Integer.class);
                        }
                    }
                }
                if (price >= freeOver ) {
                    acceptingOrderPrice = price;
                    orderDetailDeliveryTotal.setText("$0");
                    orderDetailOrderGrandTotal.setText("$"+String.valueOf(acceptingOrderPrice));
                } else {
                    acceptingOrderPrice = price + charge;
                    orderDetailDeliveryTotal.setText("$"+String.valueOf(charge));
                    orderDetailOrderGrandTotal.setText(String.valueOf("$"+acceptingOrderPrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setButtonActions (String orderStatus ) {
        switch (orderStatus) {
            case "pending" :
                orderDetailPositiveButton.setText(getString(R.string.OrderDetailAcceptButton));
                orderDetailNegativeButton.setText(getString(R.string.OrderDetailDeclineButton));
                orderDetailPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderDetailPositiveButton.setClickable(false);
                        orderDetailNegativeButton.setClickable(false);
                        acceptOrder(v);
                    }
                });
                orderDetailNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        declineOrder(v);
                    }
                });
                break;
            case "declined" :
                orderDetailNegativeButton.setVisibility(View.INVISIBLE);
                orderDetailPositiveButton.setVisibility(View.INVISIBLE);
                break;
            case "accepted" :
                orderDetailPositiveButton.setText(getString(R.string.OrderDetailShipButton));
                orderDetailNegativeButton.setText(getString(R.string.OrderDetailCancelButtton));
                orderDetailPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderDetailPositiveButton.setClickable(false);
                        orderDetailNegativeButton.setClickable(false);
                        shipOrder(v);
                    }
                });
                orderDetailNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(v);
                    }
                });
                break;
            case "shipped" :
                orderDetailPositiveButton.setText(getString(R.string.OrderDetailDeliveredButton));
                orderDetailNegativeButton.setText(getString(R.string.OrderDetailNotDeliveredButton));
                orderDetailPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderDetailPositiveButton.setClickable(false);
                        orderDetailNegativeButton.setClickable(false);
                        deliveredOrder(v);
                    }
                });
                orderDetailNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notDeliveredOrder(v);
                    }
                });
                break;
            case "cancelled" :
                orderDetailNegativeButton.setVisibility(View.INVISIBLE);
                orderDetailPositiveButton.setVisibility(View.INVISIBLE);
                break;
            case "notDelivered" :
                orderDetailNegativeButton.setVisibility(View.INVISIBLE);
                orderDetailPositiveButton.setVisibility(View.INVISIBLE);
                break;
            case "delivered" :
                orderDetailNegativeButton.setVisibility(View.INVISIBLE);
                orderDetailPositiveButton.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void declineOrder (View view) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("declined").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("declined").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void acceptOrder (View view) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("accepted").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void cancelOrder (View view) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("cancelled").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("cancelled").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void shipOrder (View view) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("shipped").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("shipped").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void deliveredOrder(View v) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("delivered").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("delivered").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void notDeliveredOrder(View v) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("notDelivered").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("notDelivered").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

}