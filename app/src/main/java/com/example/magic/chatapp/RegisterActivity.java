package com.example.magic.chatapp;
// Kevin Corcoran C00110665

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{

    private EditText name, email, password; // creates the variables
    private FirebaseAuth mobileAuth;        // For authentication for the app
    private DatabaseReference mobileDB;     // referencing the database

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        // sets the content layout from the register xml
        setContentView(R.layout.register);

        // initialises name, email & password from finding the id's on the register xml
        name = (EditText) findViewById(R.id.usernameEdit);
        email = (EditText) findViewById(R.id.emailEdit);
        password = (EditText) findViewById(R.id.passwordEdit);

        mobileAuth = FirebaseAuth.getInstance();    // Gets authentication
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users");    // Gets the database reference from the child users

    }

    public void signUpButtonClick(View view){
        // gets the content from the texboxes and puts them into the corrrect variables
        final String contentName, contentPassword, contentEmail;
        contentName = name.getText().toString().trim();
        contentPassword = password.getText().toString().trim();
        contentEmail = email.getText().toString().trim();

        // checks if the variables are not empty
        if(!TextUtils.isEmpty(contentName) && !TextUtils.isEmpty(contentPassword) && !TextUtils.isEmpty(contentEmail)){
            // Creates the user with method
            mobileAuth.createUserWithEmailAndPassword(contentEmail, contentPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String userID = mobileAuth.getCurrentUser().getUid();   // gets current user id
                        DatabaseReference currentUserDB = mobileDB.child(userID);   // gets database reference for the child userID
                        currentUserDB.child("Name").setValue(contentName);  // sets the new user in the name child from the contentName
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));   // sets up the new user from register to login activity
                    }
                }
            });
        }
    }
    // skips signup if already in the dataase
    public void loginButtonClick(View view){

        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

    }
}
