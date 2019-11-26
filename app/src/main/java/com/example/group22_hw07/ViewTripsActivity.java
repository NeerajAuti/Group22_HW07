package com.example.group22_hw07;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewTripsActivity extends AppCompatActivity {
    FloatingActionButton button_add_trip, button_signout, button_edit_profile;
    TextView tv_userName;

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        tv_userName = findViewById(R.id.tv_userName);
        recyclerView = findViewById(R.id.rv_TripsView);

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(UID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = new User(documentSnapshot.getData());
                Log.d("demo", user.toString());
                tv_userName.setText(user.getFirst_name() + " " + user.getLast_name());
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewTripsActivity.this));
                recyclerView.setHasFixedSize(true);
                fetch();

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
                FirebaseAuth.getInstance().signOut();
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
//                Snackbar.make(view, "Go to Edit Page", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent gotoEditProfile = new Intent(ViewTripsActivity.this, EditProfileActivity.class);
                startActivity(gotoEditProfile);
                finish();
            }
        });
    }

    private void fetch() {
        Log.d("test", "fetch: "+FirebaseDatabase.getInstance());
        Log.d("test", "fetch: "+FirebaseDatabase.getInstance().getReference("Trips").toString());
        Query query = FirebaseDatabase.getInstance().getReference("Trips");

        FirebaseRecyclerOptions<TripData> options =
                new FirebaseRecyclerOptions.Builder<TripData>()
                        .setQuery(query, TripData.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<TripData, TripHolder>(options) {
            @Override

            public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trip_recycler_view, parent, false);

                return new TripHolder(view);
            }


            @Override
            protected void onBindViewHolder(TripHolder holder, final int position, TripData tripData) {
                TextView TripName = holder.tv_TripName;
                TextView TripDescription = holder.tv_TripDescription;
                TextView TripCreatedBy = holder.tv_TripCreatedBy;
                ImageView TripPhoto = holder.iv_TripPhoto;

                TripName.setText(tripData.TripName);
                TripDescription.setText(tripData.TripDescription);
                TripCreatedBy.setText(tripData.CreatedBy);
                TripPhoto.setTag(tripData.PhotoURL);
                //TripPhoto.set

            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public class TripHolder extends RecyclerView.ViewHolder {
        TextView tv_TripName;
        TextView tv_TripDescription;
        TextView tv_TripCreatedBy;
        ImageView iv_TripPhoto;

        public TripHolder(View itemView) {
            super(itemView);
            tv_TripName = itemView.findViewById(R.id.tv_TripName);
            tv_TripDescription = itemView.findViewById(R.id.tv_TripDescription);
            tv_TripCreatedBy = itemView.findViewById(R.id.tv_TripCreatedBy);
            iv_TripPhoto = itemView.findViewById(R.id.iv_TripPhoto);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
