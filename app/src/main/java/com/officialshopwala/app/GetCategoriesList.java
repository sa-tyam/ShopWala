package com.officialshopwala.app;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetCategoriesList {

    public interface DataStatus {
        void DataIsLoaded(ArrayList<Category> categoryArrayList, ArrayList<String> dataKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;

    static ArrayList<Category> categoryArrayList = new ArrayList<>();
    static ArrayList<String> dataKeys = new ArrayList<>();

    public static void retrieveDataFromFirebase(final DataStatus dataStatus) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        categoryArrayList.clear();

        String phoneNumber = "+919000990098";
        databaseReference.child(phoneNumber).child("productCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addCategoriesToList(dataSnapshot);
                dataStatus.DataIsLoaded(categoryArrayList, dataKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*String phoneNumber = user.getPhoneNumber();

        if (user!=null) {
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
        }*/
    }

    public static void addCategoriesToList(DataSnapshot dataSnapshot) {
        categoryArrayList.clear();
        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());

            String name = "";

            if (keyNode.child("categoryName").getValue(String.class)!=null) {
                name = keyNode.child("categoryName").getValue(String.class);
            }
            int numberOfProducts = -1;

            if (keyNode.child("numberOfProducts").getValue(Integer.class)!=null) {
                numberOfProducts = keyNode.child("numberOfProducts").getValue(Integer.class);
            }

            Category ctg = new Category(name, numberOfProducts);
            categoryArrayList.add(ctg);
        }
    }

}
