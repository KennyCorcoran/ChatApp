package com.example.magic.chatapp;
// Kevin Corcoran C00110665

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

    // Creates the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    //option menu select options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {

            case R.id.author:
                Toast.makeText(this,"Author: Kevin Corcoran  Student No: C00110665", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    public void loginButtonClick(View view){
        // gets the content from the texboxes and puts them into the corrrect variables
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.matches(emailPattern)){

            // checks if the variables are not empty
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ){

                // Logs in the user with this method
                mobileAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If successful it checks if the user exists
                        if(task.isSuccessful()){
                            ifUserExists();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"User does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid email or password", Toast.LENGTH_SHORT).show();
        }


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
    // Method checks if the user exists
    public void ifUserExists(){

        final String userID = mobileAuth.getCurrentUser().getUid(); // gets current user id
        mobileDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // If the user exists it will transfer the user to the main activity
                if(dataSnapshot.hasChild(userID)){
                    Intent loginIntent = new Intent(LoginActivity.this, UserList.class);   //change userlist to mainactivity to pass userlist
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
