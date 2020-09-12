package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class AccountActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;

    TextView accountLayoutShopName;

    BottomNavigationView bottomNavigationView;

    public void editShopDetail (View view) {
        startActivity(new Intent(getApplicationContext(), EditShopDetailActivity.class));
    }

    public void setDeliveryCharges (View view) {
        startActivity(new Intent(getApplicationContext(), SetDeliveryChargeActivity.class));
    }

    public void showVideoTutorial (View view) {
        // link to youtube
    }

    public void shareWithCustomer (View view) {
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?text="+getString(R.string.homeShareHeaderLink)));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.whatsappNotInstalled), Toast.LENGTH_SHORT).show();
        }
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

    public void showPrivacyPolicy (View view) {
        startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
    }

    public void rateUsOnPlaystore (View view) {
        //link to playstore
    }

    public void signOutOfShopWala (View view) {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseAuth.getInstance().signOut();
        }
        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountLayoutShopName = findViewById(R.id.accountLayoutShopName);

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
                        finish();
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
                        return true;
                }

                return false;
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber = "+919000990098";
        if (user != null) {
            phoneNumber = user.getPhoneNumber();
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Sellers");


        reference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("businessName")) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.getKey().equals("businessName")){
                            accountLayoutShopName.setText(keyNode.getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}