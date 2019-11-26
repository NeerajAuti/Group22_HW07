package com.example.group22_hw07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.UUID;

public class CreateTripActivity extends AppCompatActivity {

    EditText et_tripName, et_tripDesc;
    ImageButton ib_tripPhoto;
    Button button_create_trip, button_cancel_trip;

    String tripName, tripDesc;
    Bitmap coverPhoto = null;
    static final int REQUEST_COVER_IMAGE_CAPTURE = 1;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        setTitle("Create a Trip");

        final FirebaseFirestore db =FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        et_tripName = findViewById(R.id.et_tripName);
        et_tripDesc = findViewById(R.id.et_tripDesc);
        ib_tripPhoto = findViewById(R.id.ib_tripPhoto);
        button_create_trip = findViewById(R.id.button_create_trip);
        button_cancel_trip = findViewById(R.id.button_cancel_trip);

        ib_tripPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        button_create_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripName = et_tripName.getText().toString();
                tripDesc = et_tripDesc.getText().toString();

                if (TextUtils.isEmpty(et_tripName.getText()) || TextUtils.isEmpty(et_tripDesc.getText())) {
                    if (TextUtils.isEmpty(et_tripName.getText())) {
                        Toast.makeText(CreateTripActivity.this, "Enter Trip Name", Toast.LENGTH_SHORT).show();
                        et_tripName.requestFocus();
                    } else if (TextUtils.isEmpty(et_tripDesc.getText())) {
                        Toast.makeText(CreateTripActivity.this, "Enter Trip Description", Toast.LENGTH_SHORT).show();
                        et_tripDesc.requestFocus();
                    }
                } else {
                    String UID = firebaseAuth.getCurrentUser().getUid();
                    TripData tripData = new TripData();
                    tripData.setTripName(tripName);
                    tripData.setTripDescription(tripDesc);
                    tripData.setCreatedBy(firebaseAuth.getCurrentUser().getUid());
                    //tripData.setPhotoURL();

                    Map<String, Object> tripMap = tripData.toHashMap();
                    db.collection("Trips").document().set(tripMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CreateTripActivity.this, "Trip created successfully!", Toast.LENGTH_SHORT).show();
                                        Intent tripIntent = new Intent(CreateTripActivity.this, ViewTripsActivity.class);
                                        startActivity(tripIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(CreateTripActivity.this, "Error while creating trip", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("CreateTripActivity", e.toString());
                            Toast.makeText(CreateTripActivity.this, "onFailure: Exception", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        button_cancel_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent(CreateTripActivity.this, ViewTripsActivity.class);
                startActivity(cancelIntent);
                finish();
            }
        });
    }

    //    Upload Camera Photo to Cloud Storage....
    private void uploadImage(Bitmap photoBitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        final StorageReference imageRepo = storageReference.child("images/trip/" + UUID.randomUUID().toString() + ".jpeg");

//        Converting the Bitmap into a bytearrayOutputstream....
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "onFailure: "+e.getMessage());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d(TAG, "onSuccess: "+"Image Uploaded!!!");
//            }
//        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("CreateTripActivity", "Image Download URL" + task.getResult());
                    String imageURL = task.getResult().toString();
                    Log.d("CreateTripActivity", "onSuccess: " + task.getResult());
                    Picasso.get().load(imageURL).into(ib_tripPhoto);
                } else {
                    Log.d("CreateTripActivity", "Image not uploaded!" + task.getException());
                }
            }
        });
    }

    //    TAKE PHOTO USING CAMERA...
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_COVER_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_COVER_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ib_tripPhoto.setImageBitmap(bitmap);

            coverPhoto = bitmap;

            uploadImage(coverPhoto);
        }
    }
}
