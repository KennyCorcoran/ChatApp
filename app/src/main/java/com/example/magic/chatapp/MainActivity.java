package com.example.magic.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText messEdit;
    private DatabaseReference mobileDB;
    private RecyclerView mobileMessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messEdit = findViewById(R.id.messageEdit);
        mobileDB = FirebaseDatabase.getInstance().getReference().child("Messages");
        mobileMessList = this.findViewById(R.id.messRec);
        mobileMessList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mobileMessList.setLayoutManager(linearLayoutManager);

    }

    public void sendButtonClick(View view) {
        final String messValue = messEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(messValue)){
            final DatabaseReference newPost = mobileDB.push();
            newPost.child("content").setValue(messValue);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <Message,MessViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessViewHolder>(
                Message.class,
                R.layout.singlemessagelayout,
                MessViewHolder.class,
                mobileDB

        ) {
            @Override
            protected void populateViewHolder(MessViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
            }
        };
        mobileMessList.setAdapter(FBRA);
    }

    public static class MessViewHolder extends RecyclerView.ViewHolder{
        View mobileView;
        public MessViewHolder(View itemView){
            super(itemView);
            mobileView = itemView;
        }

        public void setContent(String content){
            TextView messContent = mobileView.findViewById(R.id.messageText);
            messContent.setText(content);
        }
    }
}
