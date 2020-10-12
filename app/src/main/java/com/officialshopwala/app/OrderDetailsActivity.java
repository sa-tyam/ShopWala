package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    TextView orderDetailCustomerCity;
    Button orderDetailNegativeButton;
    Button orderDetailPositiveButton;

    ImageView orderDetailProductImage;
    TextView orderDetailProductName;

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
        orderDetailCustomerCity = findViewById(R.id.orderDetailCustomerCity);
        orderDetailNegativeButton = findViewById(R.id.orderDetailNegativeButton);
        orderDetailPositiveButton = findViewById(R.id.orderDetailPositiveButton);

        orderDetailProductImage = findViewById(R.id.orderDetailProductImage);
        orderDetailProductName = findViewById(R.id.orderDetailProductName);
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
                    if (keyNode.getKey().equals("productId")){
                        if(keyNode.getValue(String.class)!=null) {
                            String productId = keyNode.getValue(String.class);
                            Log.i("product id for order", keyNode.getValue(String.class));
                            databaseReference.child("Sellers").child(phoneNumber).child("Products").child(String.valueOf(productId)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                        if (keyNode.getKey().equals("name")) {
                                            if (keyNode.getValue(String.class) != null) {
                                                orderDetailProductName.setText(keyNode.getValue(String.class));
                                                Log.i("product name for order" , keyNode.getValue(String.class));
                                            }
                                        }

                                        if (keyNode.getKey().equals("productImageUrl")){
                                            String productImageUrl = "";
                                            productImageUrl = keyNode.getValue(String.class);
                                            if (!productImageUrl.equals("")) {
                                                Picasso.get()
                                                        .load(productImageUrl)
                                                        .fit()
                                                        .centerCrop()
                                                        .into(orderDetailProductImage);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    if (keyNode.getKey().equals("orderTime")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailOrderTime.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("itemCount")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailOrderItemTotalNumber.setText(keyNode.getValue(String.class) + " ITEMS");
                        }
                    }
                    if (keyNode.getKey().equals("price")){
                        if(keyNode.getValue(Integer.class)!=null) {
                            orderDetailOrderItemTotal.setText(getResources().getString(R.string.Rs) +keyNode.getValue(Integer.class));
                            setPrice(keyNode.getValue(Integer.class));
                        }
                    }
                    if (keyNode.getKey().equals("payment")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerPayment.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("buyerName")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerName.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("address")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerAddress.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("pinCode")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerPinCode.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("buyerCity")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerCity.setText(keyNode.getValue(String.class));
                        }
                    }
                    if (keyNode.getKey().equals("buyerMobile")){
                        if(keyNode.getValue(String.class)!=null) {
                            orderDetailCustomerMobile.setText(keyNode.getValue(String.class));
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
                    orderDetailDeliveryTotal.setText(getResources().getString(R.string.Rs)+"0");
                    orderDetailOrderGrandTotal.setText(getResources().getString(R.string.Rs)+String.valueOf(acceptingOrderPrice));
                } else {
                    acceptingOrderPrice = price + charge;
                    orderDetailDeliveryTotal.setText(getResources().getString(R.string.Rs)+String.valueOf(charge));
                    orderDetailOrderGrandTotal.setText(String.valueOf(getResources().getString(R.string.Rs)+acceptingOrderPrice));
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
                databaseReference.child("Sellers").child(phoneNumber).child("orders").child("accepted").child(String.valueOf(orderId)).removeValue();
                databaseReference.child("Sellers").child(phoneNumber).child("orders").child("shipped").child(String.valueOf(orderId)).removeValue();
                databaseReference.child("Sellers").child(phoneNumber).child("Revenue").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot rootNode : dataSnapshot.getChildren()) {
                            if ( rootNode.getKey().equals("revenue") && rootNode.getValue(Long.class) != null ) {
                                Long revenue = rootNode.getValue(Long.class);
                                acceptingOrderPrice += revenue;
                                databaseReference.child("Sellers").child(phoneNumber).child("Revenue").child("revenue").setValue(acceptingOrderPrice);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });
    }

    public void notDeliveredOrder(View v) {
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("notDelivered").child(String.valueOf(orderId)).child("orderId").setValue(orderId);
        databaseReference.child("Sellers").child(phoneNumber).child("orders").child("all").child(String.valueOf(orderId)).child("orderStatus").setValue("notDelivered").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("Sellers").child(phoneNumber).child("orders").child("accepted").child(String.valueOf(orderId)).removeValue();
                databaseReference.child("Sellers").child(phoneNumber).child("orders").child("shipped").child(String.valueOf(orderId)).removeValue();
                finish();
            }
        });
    }

}