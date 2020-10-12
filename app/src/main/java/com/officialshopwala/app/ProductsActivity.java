package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.officialshopwala.app.GetOrdersList.orderItemArrayList;
import static com.officialshopwala.app.GetProductList.productItemArrayList;
import static com.officialshopwala.app.OrdersActivity.orderAdapter;
import static com.officialshopwala.app.ProductAdapter.productItemList;

public class ProductsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    RecyclerView productsRecyclerView;
    static ProductAdapter productAdapter;

    SearchView productActivitySearchView;

    public void addProduct(View view) {
        startActivity(new Intent(this, AddProductActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        productActivitySearchView = findViewById(R.id.productActivitySearchView);



        //set product as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_products);

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
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.bottombar_products:
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

        productActivitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResult(newText);
                return true;
            }
        });

        callRetrieveData();
    }

    public void searchResult (String query ) {
        String userInput = query.toLowerCase();
        ArrayList<ProductItem> searchFiles = new ArrayList<>();
        for ( ProductItem item : productItemArrayList) {
            if (item.getName().toLowerCase().contains(userInput)) {
                searchFiles.add(item);
            }
        }
        productAdapter.searchData(searchFiles);
    }

    private void callRetrieveData() {
        GetProductList.retrieveDataFromFirebase(new GetProductList.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<ProductItem> productItemList, ArrayList<String> dataKeys) {
                setProductRecyclerView();
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
        });
    }

    private void setProductRecyclerView() {
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        productAdapter = new ProductAdapter(getApplicationContext(), productItemArrayList);
        productsRecyclerView.setAdapter(productAdapter);
    }

}