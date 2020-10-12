package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Date;

public class EditShopDetailActivity extends AppCompatActivity {

    ImageView shopImageView;
    TextView shopNameTextView;
    EditText shopLinkEditText;
    EditText shopPhoneNumberEditText;
    EditText shopAddressEditText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser user;
    String phoneNumber;

    String shopName = "";
    String shopLink = "";
    String shopAddress = "";
    String ShopImageDownloadUrl = "";

    Uri selectedImage = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_IMAGE_PICK = 2;

    String currentPhotoPath;

    public void backFromEditBusinessDetail (View view) {
        finish();
    }

    public void saveBusinessDetailChange (View view) {
        shopAddress = shopAddressEditText.getText().toString();
        databaseReference.child(phoneNumber).child("businessAddress").setValue(shopAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditShopDetailActivity.this, "Shop Detail Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sellers");
        storageReference = FirebaseStorage.getInstance().getReference("ShopImages");

        user = FirebaseAuth.getInstance().getCurrentUser();
        phoneNumber = "+919000990098";
        if ( user != null) {
            phoneNumber = user.getPhoneNumber();
        }
        initViews();
        initNonEditable();

        shopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopImageViewClicked();
            }
        });
    }

    private void shopImageViewClicked() {
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
            selectedImage = data.getData();
            Log.i("selectedImage", selectedImage.toString());
            uploadFile(selectedImage);


            if (!selectedImage.equals("")) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                shopImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }


        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("image capture result", "called");
            File f = new File(currentPhotoPath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            if(data!=null && data.getExtras()!=null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                shopImageView.setImageBitmap(imageBitmap);
            }

            uploadFile(contentUri);
        }

        if (data==null) {
            Log.i("data", "null");
        } else {
            Log.i("data", " not null");
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadFile (Uri imageUri) {
        Log.i("upload file", "called");
        if (imageUri!=null) {
            Log.i("imageUri", imageUri.toString());
            final StorageReference fileRef = storageReference.child(phoneNumber).child("ShopImage").child("image"+
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
                        databaseReference.child(phoneNumber).child("ShopImageDownloadUrl").setValue(downloadUrl);
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
//                            Toast.makeText(EditShopDetailActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                            String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
//                            databaseReference.child(phoneNumber).child("ShopImageDownloadUrl").setValue(downloadUrl);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.i("onFailure", "called");
//                            Toast.makeText(EditShopDetailActivity.this, "Image upload failed...", Toast.LENGTH_SHORT).show();
//                        }
//                    });
        } else {
            Log.i("image uri", "null");
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initNonEditable() {
        databaseReference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals("businessName")) {
                        shopName = keyNode.getValue(String.class);
                        shopNameTextView.setText(shopName);
                        Log.i("shopName", keyNode.getValue(String.class));
                    }
                    if (keyNode.getKey().equals("businessAddress")) {
                        shopAddress = keyNode.getValue(String.class);
                        shopAddressEditText.setText(shopAddress);
                        Log.i("shopadress", keyNode.getValue(String.class));
                    }
                    if (keyNode.getKey().equals("businessLink")) {
                        shopLink = keyNode.getValue(String.class);
                        shopLinkEditText.setText(shopLink);
                        Log.i("shopLink", keyNode.getValue(String.class));
                    }
                    if (keyNode.getKey().equals("ShopImageDownloadUrl")) {
                        ShopImageDownloadUrl = keyNode.getValue(String.class);
                        if(!ShopImageDownloadUrl.equals("")){
                            Picasso.get()
                                    .load(ShopImageDownloadUrl)
                                    .fit()
                                    .centerCrop()
                                    .into(shopImageView);
                        }
                        Log.i("ShopImageDownloadUrl", keyNode.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        shopPhoneNumberEditText.setText(phoneNumber);
    }

    private void initViews() {
        shopImageView = findViewById(R.id.businessShopImageView);
        shopNameTextView = findViewById(R.id.editBusinessDetailshopNameTextView);
        shopLinkEditText = findViewById(R.id.editBusinessDetailShopLinkEditText);
        shopPhoneNumberEditText = findViewById(R.id.editBusinessDetailShopPhoneNumberEditText);
        shopAddressEditText = findViewById(R.id.editBusinessDetailShopAddressEditText);
    }
}