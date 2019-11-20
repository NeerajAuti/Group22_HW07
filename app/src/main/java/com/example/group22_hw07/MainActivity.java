package com.example.group22_hw07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button button_login, button_signUp;
    User user = new User();
    String email, password;

    final int REQ_CODE_ADD_PROFILE = 100;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("Users");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request", String.valueOf(requestCode));
        if (requestCode == REQ_CODE_ADD_PROFILE) {
            if (data != null) {
                final User newUser = (User) data.getSerializableExtra("User");

                Map<String, Object> userMap = newUser.toHashMap();
                userRef.document(firebaseAuth.getCurrentUser().getUid())
                        .set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("MainActivity: NewUser", newUser.toString());
                        } else {
                            Log.e("MainActivity", task.getException().toString());
                            Toast.makeText(MainActivity.this, "Error while adding user..." + firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_emailId);
        et_password = findViewById(R.id.et_password);
        button_login = findViewById(R.id.button_login);
        button_signUp = findViewById(R.id.button_signUp);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();

                    Intent tripIntent = new Intent(MainActivity.this, ViewTripsActivity.class);
                    startActivity(tripIntent);
                    finish();
                }
            }
        };

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if (email.isEmpty()) {
                    et_email.setError("Please enter Email ID");
                    et_email.requestFocus();
                } else if (password.isEmpty()) {
                    et_password.setError("Please enter a password");
                    et_password.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                Uri photoURL = user.getPhotoUrl();
                                String UID = user.getUid();
                                Log.d("Firebase user info", name + "\t" + email + "\t" + photoURL + "\t" + UID);
                                Intent tripIntent = new Intent(MainActivity.this, ViewTripsActivity.class);
                                startActivity(tripIntent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivityForResult(signUpIntent, REQ_CODE_ADD_PROFILE);
            }
        });

        /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userInfoListJsonString = sharedPreferences.getString("UserDetails", "");
        Log.d("email&pass", userInfoListJsonString);
        if (!userInfoListJsonString.equals("")) {
            Intent emailIntent = new Intent(MainActivity.this, ViewTripsActivity.class);
            startActivity(emailIntent);
            finish();
        } else {
            et_email = findViewById(R.id.et_username);
            et_password = findViewById(R.id.et_password);
            button_login = findViewById(R.id.button_login);
            button_signUp = findViewById(R.id.button_signUp);

            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    email = et_email.getText().toString();
                    password = et_password.getText().toString();
                    Log.d("email&pass", email + " " + password);

                    Gson gson = new Gson();
                    String userInfoListJsonString = gson.toJson(user);
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserDetails", userInfoListJsonString);
                    editor.commit();

                    //Intent emailIntent = new Intent(MainActivity.this, InboxActivity.class);
                    //startActivity(emailIntent);
                    //finish();

                }
            });

        }*/


    }
}
