package com.example.magic.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserList extends AppCompatActivity{

    private DatabaseReference mobileDB;
    private RecyclerView userList;          //User List for the RecyclerView
    private FirebaseAuth mobileAuth;        //App firebase Authentication
    private FirebaseAuth.AuthStateListener mobileAuthListener;  //firebase Authentication Listener for the app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist);

        //name = findViewById(R.id.currentUser);      //finds currentUser id from activity main xml
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users"); // Gets the child Messages from the Realtime Database
        userList = this.findViewById(R.id.userRec);   //sets the RecyclerView by finding the id messRec in activity_main xml
        userList.setHasFixedSize(true); //sets the message list to a fixed size

        // Creates a new layout manager and Sets the list to start from the end of the list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        userList.setLayoutManager(linearLayoutManager);   // Sets the layout for the list

        //gets the authentication
        mobileAuth = FirebaseAuth.getInstance();

        //creates a new listener
        mobileAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Checks if the user is equal to null and if so it redirects to register class
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(UserList.this,RegisterActivity.class));
                }
            }
        };

    }

    // Logout Button
    public void logoutButtonClick(View view) {

        mobileAuth.signOut();// Signs out current user
        startActivity(new Intent(UserList.this,LoginActivity.class));   //starts a new activity and send user to login in

    }

}
