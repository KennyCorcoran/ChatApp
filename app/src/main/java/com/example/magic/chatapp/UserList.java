package com.example.magic.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.magic.chatapp.R.id.usernameText;

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
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Users");
        userList = this.findViewById(R.id.userRec);
        userList.setHasFixedSize(true);

        // Creates a new layout manager and Sets the list to start from the end of the list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        userList.setLayoutManager(linearLayoutManager);   // Sets the layout for the list

        //gets the authentication
        mobileAuth = FirebaseAuth.getInstance();

        //creates a new auth listener
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.menuSignOut:
                mobileAuth.signOut();// Signs out current user
                startActivity(new Intent(UserList.this,LoginActivity.class));   //starts a new activity and send user to login in
                Toast.makeText(this,"Signed Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.author:
                Toast.makeText(this,"Author: Kevin Corcoran  Student No: C00110665", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signedIn:
                String sIn = mobileAuth.getCurrentUser().toString();

                Toast.makeText(this, sIn, Toast.LENGTH_SHORT).show();
                break;
            case R.id.changePass:
                startActivity(new Intent(UserList.this,ChangePassword.class));
                break;
            case R.id.changeUsername:
                startActivity(new Intent(UserList.this,ChangeUsername.class));
                break;
        }

        return true;
    }

    // Send Button for sending the message to the database
    public void startConvoClick(View view) {

        startActivity(new Intent(UserList.this,MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // starts the Authentication listener for the app
        mobileAuth.addAuthStateListener(mobileAuthListener);


        FirebaseRecyclerAdapter<Users,UserList.UserViewHolder> FireBaseRecAdapt = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.userlistlayout,
                UserList.UserViewHolder.class,
                mobileDB

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {

                viewHolder.setUserName(model.getUserName());
/*
                userList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(UserList.this,MainActivity.class));

                    }

                    });*/
                }

        };

        userList.setAdapter(FireBaseRecAdapt);
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mobileUserView;
        public UserViewHolder(View itemView){
            super(itemView);
            mobileUserView = itemView;
        }

        public void setUserName(String userName){

           TextView User = mobileUserView.findViewById(R.id.usernameList);
           User.setText(userName);

        }
    }
}
