package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity {

    public static final String DB_NAME = "CATEGORY_NAME_DATABASE";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText categoryNameEditText;
    Button addCategoryFinishButton;

    public void addCategory(View view) {

        addCategoryFinishButton.setClickable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");

        String categoryName = categoryNameEditText.getText().toString();

        String phoneNumber = "+919000990098";

        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).setValue(categoryName);
        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).child("numberOfProducts").setValue(0);
        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).child("categoryName").setValue(categoryName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SetCategoryTable setCategoryTable = new SetCategoryTable(getApplicationContext());
                setCategoryTable.setCategoriesTable();
                finish();
            }
        });

    }

    public void addCategoryBackButtonClicked(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEditText = findViewById(R.id.updateCategoryNameTextView);
        addCategoryFinishButton = findViewById(R.id.updateCategorySaveButton);

    }
}