package com.officialshopwala.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        orderItemArrayList.clear();

        final ArrayList<Long> filterIds = new ArrayList<>();

        phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("orders").child(filter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.child("orderNumber").getValue(Integer.class)!=null) {
                        long orderNumber = keyNode.child("orderNumber").getValue(Long.class);
                        filterIds.add(orderNumber);
                        Log.i("ordernumber", String.valueOf(orderNumber));
                        getFromAll(dataStatus, orderNumber);
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
        orderItemArrayList.clear();

        long orderNumber = -1;
        int price = -1;
        int itemCount = -1;
        String orderTime = "";
        String orderStatus = "";

        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());

            if (keyNode.getKey().equals("orderNumber")) {
                orderNumber = keyNode.getValue(Long.class);
                Log.i("ordernumber from last", String.valueOf(orderNumber));
            }

            if (keyNode.getKey().equals("price")) {
                price = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("itemCount")) {
                itemCount = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("orderTime")) {
                orderTime = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("orderStatus")) {
                orderStatus = keyNode.getValue(String.class);
            }
        }

        OrderItem ordr = new OrderItem(itemCount, orderNumber, orderStatus, orderTime, price);
        orderItemArrayList.add(ordr);

        return orderItemArrayList;
    }

}
