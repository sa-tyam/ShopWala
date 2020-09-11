package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import static com.officialshopwala.app.CategoriesActivity.categoriesAdapter;

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

        SQLiteDatabase myDatabase = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS categoryNamesTable (name VARCHAR)");

        String sql = "INSERT INTO categoryNamesTable (name) VALUES(?)";
        SQLiteStatement statement = myDatabase.compileStatement(sql);
        statement.bindString(1, categoryName);
        statement.execute();

        String phoneNumber = "+919000990098";

        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).setValue(categoryName);
        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).child("numberOfProducts").setValue(0);
        databaseReference.child(phoneNumber).child("productCategories").child(categoryName).child("categoryName").setValue(categoryName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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

        categoryNameEditText = findViewById(R.id.addCategoryNameTextView);
        addCategoryFinishButton = findViewById(R.id.AddProductSaveButton);

    }
}