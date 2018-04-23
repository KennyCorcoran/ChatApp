package com.example.magic.chatapp;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    // creates the variables
    private EditText emailLogin;
    private EditText passwordLogin;

    private FirebaseAuth mobileAuth;   // For authentication for the app
    private DatabaseReference mobileDB; // referencing the database
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        // sets the content layout from the login xml
        setContentView(R.layout.login);

        // initialises email & password from finding the id's on the login xml
        emailLogin = (EditText) findViewById(R.id.loginEmail);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);

        mobileAuth = FirebaseAuth.getInstance();    // Gets authentication
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users");    // Gets the database reference from the child users

    }

    public void loginButtonClick(View view){
        // gets the content from the texboxes and puts them into the corrrect variables
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        // checks if the variables are not empty
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            // Logs in the user with this method
            mobileAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If successful it checks if the user exists
                    if(task.isSuccessful()){
                        ifUserExists();
                    }
                }
            });
        }
    }
    // Method checks if the user exists
    public void ifUserExists(){

        final String userID = mobileAuth.getCurrentUser().getUid(); // gets current user id
        mobileDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // If the user exists it will transfer the user to the main activity
                if(dataSnapshot.hasChild(userID)){
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
