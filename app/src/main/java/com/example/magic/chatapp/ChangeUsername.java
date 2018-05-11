package com.example.magic.chatapp;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUsername  extends AppCompatActivity {

    private EditText userEmail, oldPassword, newPassword, reConPassword; // creates the variables
    private FirebaseAuth mobileAuth;        // For authentication for the app
    DatabaseReference mobileDB;     // referencing the database
    FirebaseAuth.AuthStateListener mobileAuthListener;
    private FirebaseUser user;
    //private String curUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // sets the content layout from the register xml
        setContentView(R.layout.changepassword);

        // initialises name, email & password from finding the id's on the register xml
        userEmail = (EditText) findViewById(R.id.oldUsername);
        oldPassword = (EditText) findViewById(R.id.newUsername);
        newPassword = (EditText) findViewById(R.id.reConUsername);
        //reConPassword = (EditText) findViewById(R.id.reConPass);

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

        user = FirebaseAuth.getInstance().getCurrentUser();

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
                String sIn = mobileAuth.getCurrentUser().getUid();

                Toast.makeText(this, sIn, Toast.LENGTH_SHORT).show();
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


    public void confirmButtonClick(View view) {
      /*  // gets the content from the texboxes and puts them into the corrrect variables
        final String contentOldPass, contentNewPass, contentReConPass, contentUserEmail;
        contentUserEmail = userEmail.getText().toString().trim();
        contentOldPass = oldPassword.getText().toString().trim();
        contentNewPass = newPassword.getText().toString().trim();
        contentReConPass = reConPassword.getText().toString().trim();
        String passwordPattern = "^(?=.*[0-9]).{5,}$";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //curUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (contentUserEmail.matches(emailPattern)) {

            if (contentOldPass.matches(passwordPattern) && contentNewPass.matches(passwordPattern) && contentReConPass.matches(passwordPattern)) {

                if (contentNewPass == contentReConPass) {

                    if (contentNewPass != contentOldPass) {

                        // checks if the variables are not empty
                        if (!TextUtils.isEmpty(contentOldPass) && !TextUtils.isEmpty(contentNewPass) && !TextUtils.isEmpty(contentReConPass)) {
                            final String email = user.getEmail();
                            AuthCredential credential = EmailAuthProvider.getCredential(contentUserEmail, contentOldPass);

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(contentNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "New Password and Old Password should not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid Password Pattern (Min of 5 Chars and 1 Number) in a Password Field", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid Email Pattern", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

                */
    }
}