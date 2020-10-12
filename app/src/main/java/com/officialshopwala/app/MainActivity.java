package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.officialshopwala.app.GetOrdersList.orderItemArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String phoneNumber;
    String shopLink = "";

    TextView homeShareHeaderLink;
    TextView OverviewOrdersCountTextView;
    TextView OverviewRevenueCountTextView;
    TextView OverviewStoreViewsCountTextView;
    TextView OverviewProductViewsCountTextView;

    ArrayList<OrderItem> ActiveOrderItemArrayList = new ArrayList<>();
    LinearLayout homeActiveOrdersLayout;

    public void shareLinkOnWhatsapp(View view) {
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?text=" + shopLink));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.whatsappNotInstalled), Toast.LENGTH_SHORT).show();
        }
    }


    public void showAllOrders(View view) {
        Intent myIntent = new Intent(getApplicationContext(), OrdersActivity.class);
        myIntent.putExtra("filter", "all");
        startActivity(myIntent);
        finish();
    }

    public void showPendingOrders(View view) {
        Intent myIntent = new Intent(getApplicationContext(), OrdersActivity.class);
        myIntent.putExtra("filter", "pending");
        startActivity(myIntent);
        finish();
    }

    public void showAcceptedOrders(View view) {
        Intent myIntent = new Intent(getApplicationContext(), OrdersActivity.class);
        myIntent.putExtra("filter", "accepted");
        startActivity(myIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_home);

        // initialising views
        OverviewOrdersCountTextView = findViewById(R.id.OverviewOrdersCountTextView);
        OverviewRevenueCountTextView = findViewById(R.id.OverviewRevenueCountTextView);
        OverviewStoreViewsCountTextView = findViewById(R.id.OverviewStoreViewsCountTextView);
        OverviewProductViewsCountTextView = findViewById(R.id.OverviewProductViewsCountTextView);
        homeShareHeaderLink = findViewById(R.id.homeShareHeaderLink);
        homeActiveOrdersLayout = findViewById(R.id.homeActiveOrdersLayout);

        //setting database;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        user = FirebaseAuth.getInstance().getCurrentUser();
        phoneNumber = "+919000990098";
        if (user != null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("businessName")) {
                    startActivity(new Intent(getApplicationContext(), ShopSaveActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set on item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottombar_home:
                        return true;
                    case R.id.bottombar_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
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

        setDataInCards();

        SetCategoryTable setCategoryTable = new SetCategoryTable(getApplicationContext());
        setCategoryTable.setCategoriesTable();

        setActiveOrdersInView();

        if (shopLink.equals("")) {
            shopLink = "http://www.shopwala.link/?seller_phone=";
            String substr = phoneNumber.substring(1);
            shopLink = shopLink + substr;
            databaseReference.child(phoneNumber).child("businessLink").setValue(shopLink);
            homeShareHeaderLink.setText(shopLink);
        }
    }

    private void setDataInCards() {
        databaseReference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("Revenue")) {
                        if (keyNode.child("revenue").getValue(Long.class) != null) {
                            OverviewRevenueCountTextView.setText(getResources().getString(R.string.Rs) + String.valueOf(keyNode.child("revenue").getValue(Long.class)));
                        }
                    }
                    if (keyNode.getKey().equals("StoreViews")) {
                        if (keyNode.child("storeViews").getValue(Integer.class) != null) {
                            OverviewStoreViewsCountTextView.setText(String.valueOf(keyNode.child("storeViews").getValue(Integer.class)));
                        }
                    }
                    if (keyNode.getKey().equals("ProductViews")) {
                        if (keyNode.child("productViews").getValue(Integer.class) != null) {
                            OverviewProductViewsCountTextView.setText(String.valueOf(keyNode.child("productViews").getValue(Integer.class)));
                        }
                    }
                    if (keyNode.getKey().equals("orders")) {
                        if (keyNode.child("all").getChildrenCount() >= 0) {
                            OverviewOrdersCountTextView.setText(String.valueOf(keyNode.child("all").getChildrenCount()));
                        }
                    }
                    if (keyNode.getKey().equals("businessLink")) {
                        if (keyNode.getValue(String.class) != null) {
                            shopLink = keyNode.getValue(String.class);
                            if (shopLink != null && !shopLink.equals("")) {
                                homeShareHeaderLink.setText(shopLink);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setActiveOrdersInView() {

        final ArrayList<Long> pendingOrderIds = new ArrayList<>();
        databaseReference.child(phoneNumber).child("orders").child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.child("orderId").getValue(Integer.class) != null) {
                        long orderNumber = keyNode.child("orderId").getValue(Long.class);
                        databaseReference.child(phoneNumber).child("orders").child("all").child(String.valueOf(orderNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                addOrderToList(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public ArrayList<OrderItem> addOrderToList(DataSnapshot dataSnapshot) {

        long orderNumber = -1;
        int price = -1;
        int itemCount = -1;
        String orderTime = "";
        String orderStatus = "";
        String address = "";
        String buyerName = "";
        int pinCode = -1;
        String buyerMobile = "";

        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {

            if (keyNode.getKey().equals("orderId")) {
                orderNumber = keyNode.getValue(Long.class);
                Log.i("ordernumber from last", String.valueOf(orderNumber));
            }

            if (keyNode.getKey().equals("price")) {
                price = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("itemCount")) {
                itemCount = Integer.parseInt(keyNode.getValue(String.class));
            }
            if (keyNode.getKey().equals("orderTime")) {
                orderTime = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("orderStatus")) {
                orderStatus = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("address")) {
                address = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("buyerName")) {
                buyerName = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("pinCode")) {
                pinCode = Integer.parseInt(keyNode.getValue(String.class));
            }
            if (keyNode.getKey().equals("buyerMobile")) {
                buyerMobile = keyNode.getValue(String.class);
            }
        }

        if (orderNumber != -1) {
            OrderItem ordr = new OrderItem(orderNumber, price, itemCount, orderTime, orderStatus, address, buyerName, pinCode, buyerMobile);
            addView(ordr);
            ActiveOrderItemArrayList.add(ordr);
        }
        return ActiveOrderItemArrayList;
    }

    public void addView (OrderItem item) {
        View view = getLayoutInflater().inflate(R.layout.orders_item, null, false);

        TextView orderNumberTitleTextView = view.findViewById(R.id.productItemName);
        TextView orderPriceTextView = view.findViewById(R.id.productItemPriceTextView);
        TextView orderItemCountTextView = view.findViewById(R.id.productItemSwitchTextView);
        TextView orderItemTimeTextView = view.findViewById(R.id.orderItemTimeTextView);
        TextView orderStatusTextView = view.findViewById(R.id.orderStatusTextView);
        TextView orderDetailButton = view.findViewById(R.id.orderDetailButton);


        final long orderNumber = item.getOrderNumber();

        if (orderNumber != 0) {

            orderNumberTitleTextView.setText("Order #" + Long.toString(orderNumber));
            orderPriceTextView.setText("\u20B9" + Integer.toString(item.getPrice()));
            orderItemCountTextView.setText(Integer.toString(item.getItemCount()) + " items");
            orderItemTimeTextView.setText(item.getOrderTime());
            orderStatusTextView.setText(item.getOrderStatus());
            if (orderNumber != -1) {
                orderDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        myIntent.putExtra("orderNumber", orderNumber);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        myIntent.putExtra("orderNumber", orderNumber);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                    }
                });
            }
        } else {
            orderNumberTitleTextView.setText("No orders");
            orderPriceTextView.setText("");
            orderItemCountTextView.setText("");
            orderItemTimeTextView.setText("");
            orderStatusTextView.setText("");
            orderDetailButton.setText("");
        }

        homeActiveOrdersLayout.addView(view);
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
            e.printStackTrace();
        }
        return app_installed;
    }

}