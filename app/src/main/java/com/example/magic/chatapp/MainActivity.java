package com.example.magic.chatapp;
// Kevin Corcoran C00110665

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//import android.text.format.DateFormat;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {

    private TextView name;
    private EditText messEdit;
    private DatabaseReference mobileDB;     //Database for the app
    private RecyclerView mobileMessList;    //Message List for the RecyclerView
    private FirebaseAuth mobileAuth;        //App firebase Authentication
    private FirebaseAuth.AuthStateListener mobileAuthListener;  //firebase Authentication Listener for the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.currentUser);      //finds currentUser id from activity main xml
        messEdit = findViewById(R.id.messageEdit);  // Gets text from id & puts it in the variable
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Messages"); // Gets the child Messages from the Realtime Database
        mobileMessList = this.findViewById(R.id.messRec);   //sets the RecyclerView by finding the id messRec in activity_main xml
        mobileMessList.setHasFixedSize(true); //sets the message list to a fixed size

        // Creates a new layout manager and Sets the list to start from the end of the list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mobileMessList.setLayoutManager(linearLayoutManager);   // Sets the layout for the list

        //gets the authentication
        mobileAuth = FirebaseAuth.getInstance();

        //creates a new listener
        mobileAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Checks if the user is equal to null and if so it redirects to register class
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            }
        };

        //name.setText((CharSequence) mobileAuth);

    }

    // Logout Button
    public void logoutButtonClick(View view) {

        mobileAuth.signOut();// Signs out current user
        startActivity(new Intent(MainActivity.this,LoginActivity.class));   //starts a new activity and send user to login in

    }

    // Send Button for sending the message to the database
    public void sendButtonClick(View view) {

        FirebaseUser currentUser = mobileAuth.getCurrentUser(); // gets the current user for authentication

        // Gets the user id off of the database that's in users
        DatabaseReference mobileDBUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        final String messValue = messEdit.getText().toString().trim();  // Gets the message and puts into a string and trims it

        // Checks if the message is not empty
        if (!TextUtils.isEmpty(messValue)){
            final DatabaseReference newPost = mobileDB.push();  //puts the content into the database

            mobileDBUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    newPost.child("content").setValue(messValue);   //setting the newPost content as whats in messValue
                    //gets the name of the user snapshot child name in the database
                    newPost.child("userName").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            // scrolls to the position of the message list after get itemCount get the number of messages from the database
            mobileMessList.scrollToPosition(mobileMessList.getAdapter().getItemCount());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // starts the Authentication listener for the app
        mobileAuth.addAuthStateListener(mobileAuthListener);

        // Creates a new RecyclerAdapter for the message and view holder
        FirebaseRecyclerAdapter <Message,MessViewHolder> FireBaseRecAdapt = new FirebaseRecyclerAdapter<Message, MessViewHolder>(
                Message.class,                  // Passes in the message class
                R.layout.singlemessagelayout,   // Passes in the message layout
                MessViewHolder.class,           // Passes in the viewholder
                mobileDB                        // Adds in the mobile database

        ) {
            // populates the viewholder username, message and its position
            @Override
            protected void populateViewHolder(MessViewHolder viewHolder, Message model, int position) {

                viewHolder.setContent(model.getContent()); // sets the viewholder content
                viewHolder.setUserName(model.getUserName()); // set the viewholder username
                //viewHolder.setTime((String) DateFormat.format("HH:mm", model.getTime()));
            }
        };
        // sets the firebase adapter for the message list
        mobileMessList.setAdapter(FireBaseRecAdapt);
    }

    //Recycler view for the message holder
    public static class MessViewHolder extends RecyclerView.ViewHolder{
        View mobileView;
        public MessViewHolder(View itemView){
            super(itemView);
            mobileView = itemView;
        }

        public void setContent(String content){
            // Sets the content into messContent from the id messageText thats in the single message layout
            TextView messContent = mobileView.findViewById(R.id.messageText);
            messContent.setText(content);
        }

        public void setUserName(String userName){
            // Sets the user into contentUser from the id usernameText thats in the single message layout
            TextView contentUser = mobileView.findViewById(R.id.usernameText);
            contentUser.setText(userName);
        }
/*
        public void setTime(String timeDisplay){

            TextView time = mobileView.findViewById(R.id.timeStamp);
            time.setText(timeDisplay);
        }*/
    }
}
