package com.example.group22_hw07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripDetailsActivity extends AppCompatActivity {

    TextView tv_trip_name, tv_created_by, tv_trip_desc, tv_tripUsers;
    Button button_join_trip, button_chatroom, button_unfollow, button_cancel;
    ImageView iv_viewTripPhoto;

    ArrayList<User> tripUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        setTitle("Trip Details");

        tv_trip_name = findViewById(R.id.tv_trip_name);
        tv_tripUsers = findViewById(R.id.tv_tripUsers);
        tv_created_by = findViewById(R.id.tv_created_by);
        tv_trip_desc = findViewById(R.id.tv_trip_desc);
        button_join_trip = findViewById(R.id.button_join_trip);
        button_chatroom = findViewById(R.id.button_chatroom);
        button_unfollow = findViewById(R.id.button_unfollow);
        iv_viewTripPhoto = findViewById(R.id.iv_viewTripPhoto);
        button_cancel = findViewById(R.id.button_cancel);

        button_chatroom.setEnabled(false);
        button_unfollow.setEnabled(false);
        tv_tripUsers.setText("");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final User[] newUser = new User[1];

        Intent getTrip = getIntent();
        final TripData newTripData = (TripData) getTrip.getSerializableExtra("TripData");
        Log.d("TripData", "onCreate: "+newTripData.toString());
        String CurrentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference currentUserRef = db.collection("Users").document(CurrentUID);
        currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (newTripData.UIDs.contains(documentSnapshot.getId())) {
                    button_chatroom.setEnabled(true);
                    button_unfollow.setEnabled(true);
                    button_join_trip.setEnabled(false);
                }
                else {
                    button_chatroom.setEnabled(false);
                    button_unfollow.setEnabled(false);
                    button_join_trip.setEnabled(true);
                }
            }
        });

        for (String UID : newTripData.UIDs) {
            final DocumentReference documentReference = db.collection("Users").document(UID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User tmp = new User(documentSnapshot.getData());
                    tv_tripUsers.setText(tv_tripUsers.getText() + tmp.first_name + " " + tmp.last_name + "\n");
                }
            });
        }

        final String userID = newTripData.CreatedBy;
        final DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                newUser[0] = new User(documentSnapshot.getData());
                tv_created_by.setText(newUser[0].first_name + " " + newUser[0].last_name);
            }
        });

        tv_trip_name.setText(newTripData.TripName);
        tv_trip_desc.setText(newTripData.TripDescription);
        Picasso.get().load(newTripData.PhotoURL).into(iv_viewTripPhoto);

        button_join_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_chatroom.setEnabled(true);
                button_unfollow.setEnabled(true);

                tripUsers.add(newUser[0]);
                Toast.makeText(TripDetailsActivity.this, newUser[0].first_name + " added in list", Toast.LENGTH_SHORT).show();
                button_join_trip.setEnabled(false);
            }
        });

        button_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripUsers.remove(newUser[0]);
                Toast.makeText(TripDetailsActivity.this, newUser[0].first_name + " removed from list", Toast.LENGTH_SHORT).show();
                button_join_trip.setEnabled(true);
                button_chatroom.setEnabled(false);
                button_unfollow.setEnabled(false);
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent(TripDetailsActivity.this, ViewTripsActivity.class);
                startActivity(cancelIntent);
                finish();
            }
        });

    }
}
