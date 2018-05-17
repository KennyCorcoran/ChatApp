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

public class ChangePassword extends AppCompatActivity {

    private EditText userEmail, oldPassword, newPassword, reConPassword;
    private FirebaseAuth mobileAuth;                                    // For authentication for the app
    DatabaseReference mobileDB;                                         // referencing the database
    FirebaseAuth.AuthStateListener mobileAuthListener;
    private FirebaseUser user;
    private String curUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // sets the content layout from the register xml
        setContentView(R.layout.changepassword);

        // initialises email & password from finding the id's on the register xml
        userEmail = findViewById(R.id.useremail);
        oldPassword = findViewById(R.id.oldPass);
        newPassword = findViewById(R.id.newPass);
        reConPassword = findViewById(R.id.reConPass);

        mobileAuth = FirebaseAuth.getInstance();                                     // Gets authentication
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users");    // Gets the database reference from the child users

        //gets the authentication
        mobileAuth = FirebaseAuth.getInstance();

        //creates a new listener
        mobileAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Checks if the user is equal to null and if so it redirects to register class
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ChangePassword.this, RegisterActivity.class));
                }
            }
        };

        //gets current active user
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
                startActivity(new Intent(ChangePassword.this, LoginActivity.class));   //starts a new activity and send user to login in
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
                startActivity(new Intent(ChangePassword.this, UserList.class));
                break;
            case R.id.changePass:
                startActivity(new Intent(ChangePassword.this, ChangePassword.class));
                break;
            case R.id.changeUsername:
                startActivity(new Intent(ChangePassword.this, ChangeUsername.class));
                break;
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void confirmButtonClick(View view) {

        // gets the content from the texboxes and puts them into the corrrect variables
        final String contentOldPass, contentNewPass, contentReConPass, contentUserEmail;
        contentUserEmail = userEmail.getText().toString().trim();
        contentOldPass = oldPassword.getText().toString().trim();
        contentNewPass = newPassword.getText().toString().trim();
        contentReConPass = reConPassword.getText().toString().trim();
        String passwordPattern = "^(?=.*[0-9]).{5,}$";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        curUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();  //Gets current active user email

        AuthCredential credential = EmailAuthProvider
                .getCredential(curUserEmail,contentOldPass);

        //Error catching
        if (contentUserEmail.matches(emailPattern) && contentUserEmail.equals(curUserEmail)) {

            if (contentOldPass.matches(passwordPattern) && contentNewPass.matches(passwordPattern) && contentReConPass.matches(passwordPattern)) {

                if (contentNewPass.equals(contentReConPass)) {

                    if (!contentNewPass.equals(contentOldPass)) {

                        // checks if the variables are not empty
                        if (!TextUtils.isEmpty(contentOldPass) && !TextUtils.isEmpty(contentNewPass) && !TextUtils.isEmpty(contentReConPass)) {

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    user.updatePassword(contentNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),"Something wet wrong", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"Password reset successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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
        else {
            Toast.makeText(getApplicationContext(), "Invalid Email Pattern or Email address", Toast.LENGTH_SHORT).show();
        }
    }
}