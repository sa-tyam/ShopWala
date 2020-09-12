package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryDetailActivity extends AppCompatActivity {

    EditText updateCategoryNameTextView;
    String categoryName;
    Button updateCategorySaveButton;
    Button updateCategoryDeleteButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    String phoneNumber = "+919000990098";

    public void addCategoryBackButtonClicked (View view) {
        finish();
    }

    public void deleteProduct(View view) {
        updateCategorySaveButton.setClickable(false);
        updateCategoryDeleteButton.setClickable(false);
        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SetCategoryTable setCategoryTable = new SetCategoryTable(getApplicationContext());
                setCategoryTable.setCategoriesTable();
                finish();
            }
        });
    }

    public void updateCategory (View view) {
        updateCategorySaveButton.setClickable(false);
        updateCategoryDeleteButton.setClickable(false);

        categoryName = updateCategoryNameTextView.getText().toString();
        if (categoryName!=null) {
            databaseReference.child(phoneNumber).child("productCategories").child(categoryName).setValue(categoryName);
            databaseReference.child(phoneNumber).child("productCategories").child(categoryName).child("categoryName").setValue(categoryName).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    SetCategoryTable setCategoryTable = new SetCategoryTable(getApplicationContext());
                    setCategoryTable.setCategoriesTable();
                    finish();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        updateCategoryNameTextView = findViewById(R.id.updateCategoryNameTextView);
        updateCategorySaveButton = findViewById(R.id.updateCategorySaveButton);
        updateCategoryDeleteButton = findViewById(R.id.updateCategoryDeleteButton);

        categoryName = getIntent().getStringExtra("categoryName");
        updateCategoryNameTextView.setText(categoryName);


        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");

        if (user != null) {
            phoneNumber = user.getPhoneNumber();
        }
    }
}