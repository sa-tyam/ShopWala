package com.officialshopwala.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;
import static com.officialshopwala.app.AddCategoryActivity.DB_NAME;

public class SetCategoryTable {

    Context mContext;

    public SetCategoryTable(Context mContext) {
        this.mContext = mContext;
    }

    public void setCategoriesTable() {
        final SQLiteDatabase myDatabase = mContext.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS categoryNamesTable (name VARCHAR)");
        myDatabase.execSQL("DELETE FROM categoryNamesTable");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Sellers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber = "+919000990098";
        if (user!= null) {
            phoneNumber = user.getPhoneNumber();
        }

        databaseReference.child(phoneNumber).child("productCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if(keyNode.hasChild("categoryName")) {
                        String categoryName = keyNode.child("categoryName").getValue(String.class);
                        String sql = "INSERT INTO categoryNamesTable (name) VALUES(?)";
                        SQLiteStatement statement = myDatabase.compileStatement(sql);
                        statement.bindString(1, categoryName);
                        statement.execute();
                    } else {
                        Log.i("child", "not found");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
