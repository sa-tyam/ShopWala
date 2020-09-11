package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    public void shareLinkOnWhatsapp (View view) {
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?text="+getString(R.string.homeShareHeaderLink)));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.whatsappNotInstalled), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_home);

        //setting database rootnode;
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber = "+919000990098";
        if (user!=null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("businessName")){
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

                switch(item.getItemId()){
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