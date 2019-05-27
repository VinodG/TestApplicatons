package com.taksycraft.testapplicatons.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.common.CommonAdapter;

import java.util.Calendar;
import java.util.Vector;

public class SqliteActivity extends AppCompatActivity {

    private DbHelper da;
    private RecyclerView rv;
    private Vector<ChatObj> vec;
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        rv =(RecyclerView) findViewById(R.id.rv);
        da =  new DbHelper(this, "chatdb.sqlite",null,3);
        vec = new Vector<ChatObj>();
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter = new CommonAdapter(null, new CommonAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {
                ChatObj obj = vec.get(position);;
                holder.getTextView().setText(obj.from+" - " + obj.message+" - "+obj.to);
            }

            @Override
            public void onItemClick(Object object, int position) {
            }
        });
        rv.setAdapter(adapter);

    }

    public void onClkAdd(View view) {
        for (int i = 0 ;i< 30;i++)
        {
            ChatObj obj = new ChatObj();
            obj._id = ""+(i+1);
            obj.from = "from "+(i+1);
            obj.to = "to "+(i+1);
            obj.created_at = CalendarUtils.getData(Calendar.getInstance().getTimeInMillis(),
                    CalendarUtils.DATE_MAIN);
            vec.add(obj);
        }
        da.addRecord(vec);
        adapter.refresh(vec);
    }

    public void onClkUpdate(View view) {
    }
}
