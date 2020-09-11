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

public class GetProductList {

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;

    static ArrayList<ProductItem> productItemArrayList = new ArrayList<>();
    static ArrayList<String> dataKeys = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(ArrayList<ProductItem> productItemArrayList, ArrayList<String> dataKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static void retrieveDataFromFirebase(final DataStatus dataStatus) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        productItemArrayList.clear();

        String phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }
        databaseReference.child(phoneNumber).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addOrderToList(dataSnapshot);
                dataStatus.DataIsLoaded(productItemArrayList, dataKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<ProductItem> addOrderToList(DataSnapshot dataSnapshot) {
        productItemArrayList.clear();
        String phoneNumber = "+919000990098";
        // phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());
            long productId = -1;
            String user = phoneNumber;
            int price = -1;
            String name = "";
            String productCategory = "";
            String quantityType = "";
            String description = "";

            if (keyNode.child("productId").getValue(Long.class)!=null) {
                productId = keyNode.child("productId").getValue(Long.class);
            }
            if (keyNode.child("price").getValue(Integer.class)!=null) {
                price = keyNode.child("price").getValue(Integer.class);
            }
            if (keyNode.child("name").getValue(String.class)!=null) {
                name = keyNode.child("name").getValue(String.class);
            }
            if (keyNode.child("productCategory").getValue(String.class)!=null) {
                productCategory = keyNode.child("productCategory").getValue(String.class);
            }
            if (keyNode.child("quantityType").getValue(String.class)!=null) {
                quantityType = keyNode.child("quantityType").getValue(String.class);
            }
            if (keyNode.child("description").getValue(String.class)!=null) {
                description = keyNode.child("description").getValue(String.class);
            }
            ProductItem prdct = new ProductItem(productId, user, price, name, productCategory, quantityType, description);
            productItemArrayList.add(prdct);
        }
        return productItemArrayList;
    }

}
