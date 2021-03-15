package com.taksycraft.testapplicatons.firebasedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taksycraft.testapplicatons.R;

public class UsersActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText etInput;
    private String TAG = UsersActivity.this.getClass().getSimpleName();
    private FirebaseRecyclerAdapter<String, ChatHolder> adapterUsers;
    RecyclerView rvChat;
    private String currentUserName;
    private RecyclerView rvUsers;
    private FirebaseRecyclerOptions<String> optionsUsers;
    private String selectedUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        currentUserName = getIntent().getStringExtra(DatabaseConstants.CURRENT_USER_NAME);
        setTitle("Friends List ");
        etInput =(EditText)findViewById(R.id.etInput);
        rvChat =(RecyclerView) findViewById(R.id.rvChat);
        rvUsers =(RecyclerView) findViewById(R.id.rvUsers);
        rvChat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        rvUsers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DatabaseConstants.USER_PATH);
        Query queryUser = myRef;
//        Query quryUser = myRef;
////                .limitToLast(50);
//        quryUser.addValueEventListener(userValueEventListener);

        optionsUsers = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(queryUser, String.class)
                .build();
        adapterUsers = new FirebaseRecyclerAdapter<String ,ChatHolder>(optionsUsers) {
            @Override
            protected void onBindViewHolder(@NonNull ChatHolder chatHolder, int i, @NonNull String chatDO) {
                chatHolder.tvMessage.setText(chatDO+"");
            }
            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ChatHolder(view);
            }
        };

        rvUsers.setAdapter(adapterUsers);
        adapterUsers.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapterUsers.getItemCount();
                rvChat.scrollToPosition(positionStart);
            }
        });
    }
    private ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e(TAG, " user - " );

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void onClkPush(View view) {

            Intent intent =new Intent(UsersActivity.this,ChatActivity.class);
            intent.putExtra(DatabaseConstants.CURRENT_USER_NAME,currentUserName);
            intent.putExtra(DatabaseConstants.SELECTED_USER_NAME,selectedUserName);
            startActivity(intent);
    }
    @Override
    public void onPause() {
        adapterUsers.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterUsers.startListening();
    }
    class ChatHolder  extends RecyclerView.ViewHolder {
        public final TextView tvMessage;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = (TextView)itemView.findViewById(R.id.tvMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedUserName= adapterUsers.getItem(ChatHolder.this.getAdapterPosition());
                    etInput.setText(selectedUserName+"");
                }
            });
        }
    }

}
