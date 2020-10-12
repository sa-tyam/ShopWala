package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.officialshopwala.app.GetOrdersList.orderItemArrayList;

public class OrdersActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    static RecyclerView ordersRecyclerView;
    static OrderAdapter orderAdapter;

    String filter = "all";

    public void showOrderFilter (View view) {
        String[] options = {"All", "Pending", "Accepted", "Declined", "Shipped", "Cancelled", "Delivered"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        callRetrieveData("all");
                        break;
                    case 1:
                        callRetrieveData("pending");
                        break;
                    case 2:
                        callRetrieveData("accepted");
                        break;
                    case 3:
                        callRetrieveData("declined");
                        break;
                    case 4:
                        callRetrieveData("shipped");
                        break;
                    case 5:
                        callRetrieveData("cancelled");
                        break;
                    case 6:
                        callRetrieveData("delivered");
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ordersRecyclerView = findViewById(R.id.productsRecyclerView);

        // checking for filters from main activity
        if (getIntent().getStringExtra("filter")!= null) {
            filter = getIntent().getStringExtra("filter");
        }

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
                        finish();
                        return true;
                    case R.id.bottombar_orders:
                        return true;
                    case R.id.bottombar_products:
                        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.bottombar_categories:
                        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.bottombar_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        callRetrieveData(filter);
    }

    private void setOrderRecyclerView() {
        ordersRecyclerView.setHasFixedSize(true);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        orderAdapter = new OrderAdapter(getApplicationContext(), orderItemArrayList);
        ordersRecyclerView.setAdapter(orderAdapter);
    }

    private void callRetrieveData( String filter) {
        GetOrdersList.retrieveDataFromFirebase(new GetOrdersList.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<OrderItem> orderItemArrayList, ArrayList<String> dataKeys) {
                setOrderRecyclerView();
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {
            }
        }, filter);
    }


}