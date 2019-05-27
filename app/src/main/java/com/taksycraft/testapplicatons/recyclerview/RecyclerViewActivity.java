package com.taksycraft.testapplicatons.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.Adapter;

import java.util.Vector;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LinearLayoutManager layoutMananger;
    private Adapter adapter;
    private boolean isScrolling;
    private int currentItems;
    private int totalItems;
    private int scrollOnItems;
    private ProgressBar pb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rv =(RecyclerView)findViewById(R.id.rv);
        pb =(ProgressBar)findViewById(R.id.pb);
        layoutMananger = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rv.setLayoutManager(layoutMananger);
        adapter = new Adapter();
        rv.setAdapter(adapter);
        adapter.refresh(getList());
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling =true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=layoutMananger.getChildCount();
                totalItems=layoutMananger.getItemCount();
                scrollOnItems= layoutMananger.findFirstVisibleItemPosition();

                if(isScrolling && currentItems + scrollOnItems == totalItems )
                {
                    isScrolling =false;
                    pb.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.getList().addAll(getList());
                            adapter.notifyDataSetChanged();
                            pb.setVisibility(View.GONE);

                        }
                    },5000);

                }
            }
        });
    }

    private Vector<String> getList() {
        Vector<String> vector = new Vector<String>();
        for(int i = 0;i<35;i++)
        {
            vector.add(Math.floor(Math.random()*100)+"");

        }
        return vector;
    }
}
