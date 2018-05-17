package com.example.magic.chatapp;
// Name: Kevin Corcoran
// Student No: C00110665

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ChangeUsername  extends AppCompatActivity {

    private EditText oldName, newName, reConName;       // creates the variables
    private FirebaseAuth mobileAuth;                    // For authentication for the app
    DatabaseReference mobileDB;                         // referencing the database
    FirebaseAuth.AuthStateListener mobileAuthListener;
    private FirebaseUser user;
    private Object displayName;
    //private String curUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // sets the content layout from the register xml
        setContentView(R.layout.changeusername);

        // initialises name, email & password from finding the id's on the register xml
        oldName = (EditText) findViewById(R.id.oldUsername);
        newName = (EditText) findViewById(R.id.newUsername);
        reConName = (EditText) findViewById(R.id.reConUsername);
       

        mobileAuth = FirebaseAuth.getInstance();    // Gets authentication
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users");    // Gets the database reference from the child users

        //gets the authentication
        mobileAuth = FirebaseAuth.getInstance();

        //creates a new listener
        mobileAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Checks if the user is equal to null and if so it redirects to register class
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ChangeUsername.this, RegisterActivity.class));
                }
            }
        };

        FirebaseUser user = mobileAuth.getCurrentUser();

    }

    // Creates the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    //option menu select options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menuSignOut:
                mobileAuth.signOut();// Signs out current user
                startActivity(new Intent(ChangeUsername.this, LoginActivity.class));   //starts a new activity and send user to login in
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.author:
                Toast.makeText(this, "Author: Kevin Corcoran  Student No: C00110665", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signedIn:

                mobileAuth = FirebaseAuth.getInstance();
                final FirebaseUser theUser = mobileAuth.getCurrentUser();
                if (theUser !=null) {
                    String currUser = theUser.getEmail();
                    Toast.makeText(this,currUser + " is Logged In.", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.listOfUsers:
                startActivity(new Intent(ChangeUsername.this, UserList.class));
                break;
            case R.id.changePass:
                startActivity(new Intent(ChangeUsername.this, ChangePassword.class));
                break;
            case R.id.changeUsername:
                startActivity(new Intent(ChangeUsername.this, ChangeUsername.class));
                break;
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void confirmButtonClick(View view) {

        final String contentOldName, contentNewName, contentReConName;
        contentOldName = oldName.getText().toString().trim();
        contentNewName = newName.getText().toString().trim();
        contentReConName = reConName.getText().toString().trim();

       String curUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();  //Gets current active user email

        assert curUserEmail != null;
        AuthCredential credential = EmailAuthProvider
                .getCredential(curUserEmail,contentOldName);

        if (contentNewName.matches(contentReConName)) {

                        // checks if the variables are not empty
                        if (!TextUtils.isEmpty(contentOldName) && !TextUtils.isEmpty(contentNewName) && !TextUtils.isEmpty(contentReConName)) {


                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String userID = mobileAuth.getCurrentUser().getUid();   // gets current user id
                                        DatabaseReference currentUserDB = mobileDB.child(userID);   // gets database reference for the child userID
                                        currentUserDB.child("Name").setValue(newName);
                                    }
                                }
                            });
                        }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid Name Comparison", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);


    }
}