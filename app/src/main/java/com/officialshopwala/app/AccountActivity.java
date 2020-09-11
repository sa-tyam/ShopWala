package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    public void editShopDetail (View view) {
        Toast.makeText(this, "edit detail", Toast.LENGTH_SHORT).show();
    }

    public void setDeliveryCharges (View view) {
        Toast.makeText(this, "set delivery charge", Toast.LENGTH_SHORT).show();
    }

    public void showVideoTutorial (View view) {
        Toast.makeText(this, "show video tutorial", Toast.LENGTH_SHORT).show();
    }

    public void shareWithCustomer (View view) {
        Toast.makeText(this, "share with customer", Toast.LENGTH_SHORT).show();
    }

    public void showPrivacyPolicy (View view) {
        Toast.makeText(this, "show privacy policy", Toast.LENGTH_SHORT).show();
    }

    public void rateUsOnPlaystore (View view) {
        Toast.makeText(this, "rate us on playstore", Toast.LENGTH_SHORT).show();
    }

    public void signOutOfShopWala (View view) {
        Toast.makeText(this, "sign out of shopwala", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set account as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_account);

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
                        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottombar_account:
                        return true;
                }

                return false;
            }
        });

    }
}