package com.example.magic.chatapp;
// Name: Kevin Corcoran
// Student No: C00110665

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

        mobileAuth = FirebaseAuth.getInstance();                                    // Gets authentication
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


    public void signUpButtonClick(View view){
        // gets the content from the texboxes and puts them into the corrrect variables
        final String contentName, contentPassword, contentEmail;
        contentName = name.getText().toString().trim();
        contentPassword = password.getText().toString().trim();
        contentEmail = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPattern = "^(?=.*[0-9]).{5,}$";


        //Error catching
        if(contentEmail.matches(emailPattern)) {

            if (contentPassword.matches(passwordPattern)){

                // checks if the variables are not empty
                if (!TextUtils.isEmpty(contentName) && !TextUtils.isEmpty(contentPassword) && !TextUtils.isEmpty(contentEmail)) {
                    // Creates the user with method
                    mobileAuth.createUserWithEmailAndPassword(contentEmail, contentPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String userID = mobileAuth.getCurrentUser().getUid();   // gets current user id
                                DatabaseReference currentUserDB = mobileDB.child(userID);   // gets database reference for the child userID
                                currentUserDB.child("Name").setValue(contentName);  // sets the new user in the name child from the contentName
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));   // sets up the new user from register to login activity
                            }
                        }
                    });
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid Password Pattern (Min of 5 Chars and 1 Number)", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
        }

        //Auto hide for keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
    // skips signup if already in the dataase
    public void loginButtonClick(View view){

        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

    }
}
