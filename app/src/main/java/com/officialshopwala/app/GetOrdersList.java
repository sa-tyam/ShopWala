package com.officialshopwala.app;

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

    static ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    static ArrayList<String> dataKeys = new ArrayList<>();


    public interface DataStatus {
        void DataIsLoaded(ArrayList<OrderItem> orderItemArrayList, ArrayList<String> dataKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static void retrieveDataFromFirebase(final DataStatus dataStatus) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        orderItemArrayList.clear();

        String phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }
        databaseReference.child(phoneNumber).child("orders").child("all").addValueEventListener(new ValueEventListener() {
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
        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());
            int orderNumber = -1;
            int price = -1;
            int itemCount = -1;
            String orderTime = "";
            String orderStatus = "";

            if (keyNode.child("orderNumber").getValue(Integer.class)!=null) {
                orderNumber = keyNode.child("orderNumber").getValue(Integer.class);
            }
            if (keyNode.child("price").getValue(Integer.class)!=null) {
                price = keyNode.child("price").getValue(Integer.class);
            }
            if (keyNode.child("itemCount").getValue(Integer.class)!=null) {
                itemCount = keyNode.child("itemCount").getValue(Integer.class);
            }
            if (keyNode.child("orderTime").getValue(String.class)!=null) {
                orderTime = keyNode.child("orderTime").getValue(String.class);
            }
            if (keyNode.child("orderStatus").getValue(String.class)!=null) {
                orderStatus = keyNode.child("orderStatus").getValue(String.class);
            }
            OrderItem ordr = new OrderItem(itemCount, orderNumber, orderStatus, orderTime, price);
            OrderItem orderItem = keyNode.getValue(OrderItem.class);
            orderItemArrayList.add(ordr);
        }
        return orderItemArrayList;
    }

}
