package com.example.group22_hw07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText et_fname, et_lname, et_gender, et_emailId, et_password;
    ImageButton ib_photo;
    Button button_register, button_cancel;
    User newUser = new User();
    String firstName, lastName, emailId, gender, password;
    Bitmap profilePhoto;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        firebaseAuth = FirebaseAuth.getInstance();

        ib_photo = findViewById(R.id.ib_photo);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_gender = findViewById(R.id.et_gender);
        et_emailId = findViewById(R.id.et_emailId);
        et_password = findViewById(R.id.et_password);
        button_register = findViewById(R.id.button_register);
        button_cancel = findViewById(R.id.button_cancel);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = et_fname.getText().toString();
                lastName = et_lname.getText().toString();
                emailId = et_emailId.getText().toString();
                gender = et_gender.getText().toString();
                password = et_password.getText().toString();
                //profilePhoto = ib_photo.getDrawable();

                if(emailId.isEmpty()){
                    et_emailId.setError("Please enter Email ID");
                    et_emailId.requestFocus();
                } else if(password.isEmpty()){
                    et_password.setError("Please enter a password");
                    et_password.requestFocus();
                } else if(emailId.isEmpty() && password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if(!(emailId.isEmpty() && password.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                User user = new User();
                                user.setFirst_name(firstName);
                                user.setLast_name(lastName);
                                user.setGender(gender);
                                user.setEmailID(emailId);
                                user.setPassword(password);
                                //user.setProfile_pic_URL(profilePhoto);
                                Intent addProfileIntent = new Intent();
                                addProfileIntent.putExtra("User", user);
                                setResult(100, addProfileIntent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(cancelIntent);
                finish();
            }
        });

    }
}
