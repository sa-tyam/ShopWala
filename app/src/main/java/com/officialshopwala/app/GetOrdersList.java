package com.officialshopwala.app;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.officialshopwala.app.OrdersActivity.orderAdapter;

public class GetOrdersList {

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;
    static String phoneNumber;

    static ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    static ArrayList<String> dataKeys = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(ArrayList<OrderItem> orderItemArrayList, ArrayList<String> dataKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static void retrieveDataFromFirebase(final DataStatus dataStatus, final String filter) {
        orderItemArrayList.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final ArrayList<Long> filterIds = new ArrayList<>();

        phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("orders").child(filter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.child("orderId").getValue(Integer.class) != null) {
                            long orderNumber = keyNode.child("orderId").getValue(Long.class);
                            filterIds.add(orderNumber);
                            Log.i("ordernumber", String.valueOf(orderNumber));
                            getFromAll(dataStatus, orderNumber);
                        }
                    }
                } else {
                    if (orderItemArrayList.isEmpty()) {
                        dataStatus.DataIsLoaded(orderItemArrayList, dataKeys);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static public void getFromAll (final DataStatus dataStatus, final Long filter) {
        databaseReference.child(phoneNumber).child("orders").child("all").child(String.valueOf(filter)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addOrderToList(dataSnapshot);
                dataStatus.DataIsLoaded(orderItemArrayList, dataKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<OrderItem> addOrderToList(DataSnapshot dataSnapshot) {

        long orderNumber = -1;
        int price = -1;
        int itemCount = -1;
        String orderTime = "";
        String orderStatus = "";
        String address = "";
        String buyerName = "";
        int pinCode = -1;
        String buyerMobile = "";

        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());

            if (keyNode.getKey().equals("orderId")) {
                orderNumber = keyNode.getValue(Long.class);
                Log.i("ordernumber from last", String.valueOf(orderNumber));
            }

            if (keyNode.getKey().equals("price")) {
                price = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("itemCount")) {
                itemCount = Integer.parseInt(keyNode.getValue(String.class));
            }
            if (keyNode.getKey().equals("orderTime")) {
                orderTime = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("orderStatus")) {
                orderStatus = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("address")) {
                address = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("buyerName")) {
                buyerName = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("pinCode")) {
                pinCode = Integer.parseInt(keyNode.getValue(String.class));
            }
            if (keyNode.getKey().equals("buyerMobile")) {
                buyerMobile = keyNode.getValue(String.class);
            }
        }

        if (orderNumber != -1) {
            OrderItem ordr = new OrderItem(orderNumber, price, itemCount, orderTime, orderStatus, address, buyerName, pinCode, buyerMobile);
            orderItemArrayList.add(ordr);
        }
        return orderItemArrayList;
    }

}
