package com.example.group22_hw07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ViewTripsActivity extends AppCompatActivity {

    Button button_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);
        setTitle("Trips");

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
    }
}
