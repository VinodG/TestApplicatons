package com.taksycraft.testapplicatons.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.taksycraft.testapplicatons.R;

public class RoomActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rv =(RecyclerView)findViewById(R.id.rvChat);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

    }
    public void onClkPush(View view )
    {
        
    }
}
