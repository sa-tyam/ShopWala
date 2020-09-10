package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    RecyclerView categoriesRecyclerView;
    static CategoriesAdapter categoriesAdapter;
    static ArrayList<Category> categoryArrayList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public void addCategory (View view) {
        Intent myIntent = new Intent(getApplicationContext(), AddCategoryActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);

        //set categories as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_categories);

        //set on item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.bottombar_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_products:
                        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_categories:
                        return true;
                    case R.id.bottombar_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        retrieveDataFromFirebase();

        setCategoryRecyclerView();
    }

    private void setCategoryRecyclerView() {
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        categoriesAdapter = new CategoriesAdapter(getApplicationContext(), categoryArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
    }

    private void retrieveDataFromFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        categoryArrayList.clear();

        String phoneNumber = "+919000990098";

        databaseReference = firebaseDatabase.getReference("Sellers");
        databaseReference.child(phoneNumber).child("productCategories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("retrieve data", "called");
                addCategoriesToList( dataSnapshot );
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*if (user!= null) {
            String phoneNumber = user.getPhoneNumber();

            databaseReference = firebaseDatabase.getReference("Sellers");
            databaseReference.child(phoneNumber).child("productCategories").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i("retrieve data", "called");
                    addCategoriesToList( dataSnapshot );
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } */

    }

    public void addCategoriesToList (DataSnapshot dataSnapshot ) {
        Log.i("add categories to list", "called");
        String name="";
        Category ct = new Category();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            name = ds.getValue(String.class);
            if (name != null) {
                Log.i("name", name);
            }
            ct.setCategoryName(name);
            ct.setNumberOfProducts("0");
            categoryArrayList.add(ct);
            categoriesAdapter.notifyDataSetChanged();
        }
    }
}