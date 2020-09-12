package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    SpinnerItemAdapter quantityTypeSpinnerAdapter;
    static SpinnerItemAdapter categoryTypeSpinnerAdapter;

    ArrayList<SpinnerItems> quantityTypeSpinnerItemList = new ArrayList<>();
    ArrayList<SpinnerItems> categorySpinnerItemList = new ArrayList<>();

    Spinner quantityTypeSpinner;
    Spinner categorySpinner;

    SpinnerItems productQuantityType;
    SpinnerItems productCategoryType;

    ImageView AddProductImageView;
    EditText AddProductName;
    EditText AddProductPrice;
    EditText AddProductDescription;
    Button AddProductSaveButton;

    long productId = 1000;

    String PhoneNumber;

    String name;
    int price;
    String quantityType;
    String productCategory;
    String description;


    public void addProductBackButtonCicked (View view) {
        finish();
    }

    public void updateProduct(View view) {
        name = AddProductName.getText().toString();
        price = Integer.parseInt(AddProductPrice.getText().toString());
        quantityType = "";
        if(productQuantityType!=null) {
            quantityType = productQuantityType.getSpinnerItemName();
        }
        productCategory="";
        if (productCategoryType !=null){
            productCategory = productCategoryType.getSpinnerItemName();
        }


        description = AddProductDescription.getText().toString();

        if (name != null && price > 0 && quantityType!=null) {

            if (productId >= 1000) {

                saveProduct(databaseReference, productId, PhoneNumber, name, price, quantityType, productCategory, description);
            }
        }
    }

    public void saveProduct (DatabaseReference databaseReference, long productId, String PhoneNumber,String name, int price,String quantityType, String productCategory, String description) {
        databaseReference.child("ProductsActive").child(String.valueOf(productId)).child("productId").setValue(productId);
        databaseReference.child("ProductsActive").child(String.valueOf(productId)).child("seller").setValue(PhoneNumber);

        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productId").setValue(productId);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("name").setValue(name);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("price").setValue(price);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("quantityType").setValue(quantityType);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productCategory").setValue(productCategory);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("description").setValue(description).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    public void deleteProduct (View view) {
        AlertDialog.Builder subbuilder = new AlertDialog.Builder(this);
        subbuilder.setTitle("Delete Product")
                .setMessage("Are you sure to delete the product?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String phoneNumber = "+919000990098";
                        if (user!=null) {
                            phoneNumber = user.getPhoneNumber();
                        }
                        databaseReference.child("ProductsActive").child(String.valueOf(productId)).child("deleted").setValue("deleted product");
                        databaseReference.child("Sellers").child(phoneNumber).child("Products").child(String.valueOf(productId)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        PhoneNumber = "+919000990098";
        if (user != null) {
            PhoneNumber = user.getPhoneNumber();
        }

        productId = getIntent().getLongExtra("productId", -1);

        initialiseViews();
        setInitialValues();
        setSpinners();
    }

    private void setInitialValues() {

        Log.i("setInitialValues", "called");
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("description")){
                        String description = keyNode.getValue(String.class);
                        AddProductDescription.setText(description);
                    }
                    if (keyNode.getKey().equals("name")){
                        String name = keyNode.getValue(String.class);
                        AddProductName.setText(name);
                    }
                    if (keyNode.getKey().equals("price")){
                        int price = keyNode.getValue(Integer.class);
                        AddProductPrice.setText(String.valueOf(price));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void initialiseViews() {
        quantityTypeSpinner = findViewById(R.id.editProductQuantityTypeSpinner);
        categorySpinner = findViewById(R.id.editChoseCtgSpinner);
        AddProductImageView = findViewById(R.id.editProductimageView);
        AddProductName = findViewById(R.id.editProductProductName);
        AddProductPrice = findViewById(R.id.editProductPrice);
        AddProductDescription = findViewById(R.id.editProductDescription);
        AddProductSaveButton = findViewById(R.id.updateCategorySaveButton);
    }

    private void setSpinners() {
        initCategoryList();
        initQuantityTypeList();

        quantityTypeSpinnerAdapter = new SpinnerItemAdapter(this, quantityTypeSpinnerItemList);
        categoryTypeSpinnerAdapter = new SpinnerItemAdapter(this, categorySpinnerItemList);

        quantityTypeSpinner.setAdapter(quantityTypeSpinnerAdapter);
        categorySpinner.setAdapter(categoryTypeSpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productCategoryType = (SpinnerItems) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        quantityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productQuantityType = (SpinnerItems)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void initCategoryList() {
        categorySpinnerItemList.clear();
        /*PhoneNumber = "+919000990098";
        if (user != null) {
            PhoneNumber = user.getPhoneNumber();
        }

        databaseReference.child("Sellers").child(PhoneNumber).child("productCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if(keyNode.getKey()!=null) {
                        categorySpinnerItemList.add(new SpinnerItems(keyNode.getKey()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

        SQLiteDatabase myDatabase = this.openOrCreateDatabase("CATEGORY_NAME_DATABASE", MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS categoryNamesTable (name VARCHAR)");

        Cursor c = myDatabase.rawQuery("SELECT * FROM categoryNamesTable", null);
        int nameIndex = c.getColumnIndex("name");

        if(c!=null && c.moveToFirst()) {
            Log.i("first", c.getString(nameIndex));
            for ( int i = 0 ; i < 100 ; i++ ) {
                if (c.moveToNext()) {
                    categorySpinnerItemList.add(new SpinnerItems(c.getString(nameIndex)));
                } else {
                    break;
                }
            }
        }
        c.close();
    }

    private void initQuantityTypeList() {
        quantityTypeSpinnerItemList.add(new SpinnerItems(getString(R.string.AddProductPerUnit)));
        quantityTypeSpinnerItemList.add(new SpinnerItems(getString(R.string.AddProductPerKg)));
        quantityTypeSpinnerItemList.add(new SpinnerItems(getString(R.string.AddProductPerGm)));
        quantityTypeSpinnerItemList.add(new SpinnerItems(getString(R.string.AddProductPerLtr)));
        quantityTypeSpinnerItemList.add(new SpinnerItems(getString(R.string.AddProductPerMl)));
    }

}