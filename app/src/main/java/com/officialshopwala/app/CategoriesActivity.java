package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.officialshopwala.app.GetCategoriesList.categoryArrayList;

public class CategoriesActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    RecyclerView categoriesRecyclerView;
    static CategoriesAdapter categoriesAdapter;

    public void addCategory (View view) {
        Intent myIntent = new Intent(getApplicationContext(), AddCategoryActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        categoriesRecyclerView = findViewById(R.id.productsRecyclerView);

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

        callRetrieveData();
    }

    private void callRetrieveData() {
        GetCategoriesList.retrieveDataFromFirebase(new GetCategoriesList.DataStatus(){
            @Override
            public void DataIsLoaded(ArrayList<Category> categoryArrayList, ArrayList<String> dataKeys) {
                setCategoryRecyclerView();
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

    private void setCategoryRecyclerView() {
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        categoriesAdapter = new CategoriesAdapter(getApplicationContext(), categoryArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
    }

}