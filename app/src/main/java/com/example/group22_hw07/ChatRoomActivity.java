package com.example.group22_hw07;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatRoomActivity extends AppCompatActivity {
    RecyclerView ChatRecyclerView;
    FloatingActionButton btn_Send_message;
    EditText et_Message;

    TripData CurrentTripData;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    Query query;
    String userId;
    String userName;
    FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent getTrip = getIntent();
        CurrentTripData = (TripData) getTrip.getSerializableExtra("TripData");
        Log.d("Chat", "onCreate: " + CurrentTripData.toString());

        ChatRecyclerView = findViewById(R.id.ChatRecyclerView);
        btn_Send_message = findViewById(R.id.btn_Send_message);
        et_Message = findViewById(R.id.et_Message);
        ChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btn_Send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_Message.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(ChatRoomActivity.this, "Enter Something", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    database.collection("Messages").add(new Message(userName, message, userId, CurrentTripData.TripID));
                    et_Message.setText("");
                }
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        userName = user.getDisplayName();
        database = FirebaseFirestore.getInstance();
        query = database.collection("Messages").whereEqualTo("tripID",CurrentTripData.TripID);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                }
            }
        });
        adapter = new MessageAdapter(ChatRoomActivity.this,query, userId);
        ChatRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ViewTripsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}
