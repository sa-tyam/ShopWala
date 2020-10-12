package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductDetailActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
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
    EditText editProductSpecialVariety;
    Button AddProductSaveButton;

    long productId = 1000;

    String PhoneNumber;

    String name;
    int price;
    String quantityType;
    String productCategory;
    String description;

    Uri selectedImage = null;
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_IMAGE_PICK = 2;

    String currentPhotoPath;

    Uri imageUri = null;


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
        String variety = editProductSpecialVariety.getText().toString();

        if (name != null && price > 0 && quantityType!=null) {

            if (imageUri != null) {
                uploadFile(imageUri);
            }

            if (productId >= 1000) {

                saveProduct(databaseReference, productId, PhoneNumber, name, price, quantityType, productCategory, description, variety);
            }
        }
    }

    public void saveProduct (DatabaseReference databaseReference, long productId, String PhoneNumber,String name, int price,String quantityType, String productCategory, String description, String variety) {
        databaseReference.child("ProductsActive").child(String.valueOf(productId)).child("productId").setValue(productId);
        databaseReference.child("ProductsActive").child(String.valueOf(productId)).child("seller").setValue(PhoneNumber);

        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productId").setValue(productId);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("name").setValue(name);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("price").setValue(price);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("quantityType").setValue(quantityType);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productCategory").setValue(productCategory);
        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("variety").setValue(variety);
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

    private void addProductImageViewClicked() {
        String[] options = {"Click Image", "Chose Image",};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        pickImage();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.officialshopwala.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void pickImage () {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("on Activity result", "called");
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            selectedImage = data.getData();
            if (!selectedImage.equals("")) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                AddProductImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("image capture result", "called");
            File f = new File(currentPhotoPath);
            imageUri = Uri.fromFile(f);

            if (data!=null && data.getExtras()!=null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                AddProductImageView.setImageBitmap(imageBitmap);
            }
        }

        if (data==null) {
            Log.i("data", "null");
        } else {
            Log.i("data", " not null");
        }
    }

    public void uploadFile (Uri imageUri) {
        Log.i("upload file", "called");
        if (imageUri!=null) {
            Log.i("imageUri", imageUri.toString());
            final StorageReference fileRef = storageReference.child(PhoneNumber).child("ProductImages").child(String.valueOf(productId)).child("image"+
                    ".jpg");

            UploadTask uploadTask = fileRef.putFile(imageUri);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadUrl = downloadUri.toString();
                        databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productImageUrl").setValue(downloadUrl);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });





//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Log.i("onSucces", "called");
//                            Toast.makeText(AddProductActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                            String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            databaseReference.child("Sellers").child(PhoneNumber).child("Products").child(String.valueOf(productId)).child("productImageUrl").setValue(downloadUrl);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.i("onFailure", "called");
//                            Toast.makeText(AddProductActivity.this, "Image upload failed...", Toast.LENGTH_SHORT).show();
//                        }
//                    });
        } else {
            Log.i("image uri", "null");
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference("ProductImages");

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
                    if (keyNode.getKey().equals("productImageUrl")){
                        String productImageUrl = "";
                        productImageUrl = keyNode.getValue(String.class);
                        if (!productImageUrl.equals("")) {
                            Picasso.get()
                                    .load(productImageUrl)
                                    .fit()
                                    .centerCrop()
                                    .into(AddProductImageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AddProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductImageViewClicked();
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
        editProductSpecialVariety = findViewById(R.id.editProductSpecialVariety);
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