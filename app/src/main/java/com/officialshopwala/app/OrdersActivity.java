package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrdersActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set orders as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_orders);

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
                        return true;
                    case R.id.bottombar_products:
                        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_categories:
                        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });
    }
}