package com.example.group22_hw07;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import static com.example.group22_hw07.MainActivity.firebaseAuth;
import static com.example.group22_hw07.MainActivity.userRef;

public class ViewTripsActivity extends AppCompatActivity {
    FloatingActionButton button_add_trip, button_signout, button_edit_profile;
    TextView tv_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        tv_userName = findViewById(R.id.tv_userName);

        String UID = firebaseAuth.getCurrentUser().getUid();
        Log.d("UID", UID);
        final DocumentReference documentReference = userRef.document(UID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = new User(documentSnapshot.getData());
                Log.d("demo", user.toString());
                tv_userName.setText(user.getFirst_name() + " " + user.getLast_name());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error", e.toString());
            }
        });

        button_add_trip = findViewById(R.id.button_add_trip);
        button_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Trip", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        button_signout = findViewById(R.id.button_signout);
        button_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getInstance().signOut();
                MainActivity.mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent gotoMainIntent = new Intent(ViewTripsActivity.this, MainActivity.class);
                        startActivity(gotoMainIntent);
                    }
                });
            }
        });

        button_edit_profile = findViewById(R.id.button_edit_profile);
        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to Edit Page", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Snackbar.make(view, "Go to Edit Page", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent gotoEditProfile = new Intent(ViewTripsActivity.this, EditProfileActivity.class);
                //gotoEditProfile.putExtra("Profile", MainActivity.firebaseAuth.getCurrentUser());
                startActivity(gotoEditProfile);
                finish();
            }
        });
    }

}
