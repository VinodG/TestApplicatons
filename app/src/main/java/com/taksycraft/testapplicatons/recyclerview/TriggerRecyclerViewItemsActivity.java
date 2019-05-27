package com.taksycraft.testapplicatons.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class TriggerRecyclerViewItemsActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private CompositeViews trigger;
    private RecyclerView rv;
    private Vector<String> vec = new Vector<String>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_recycler_view_items);
        setList();
        trigger = new CompositeViews();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new Adapter(vec);
        rv.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onClkChange(null);
                rv.postDelayed(this,1000);
            }
        },1000);


    }

    private void setList() {

        for (int i = -5; i < 100; i++) {
            Calendar calendar =Calendar.getInstance();
            calendar.add(Calendar.SECOND,i);
            vec.add(CalendarUtils.getDataWithLocale(calendar.getTimeInMillis(),CalendarUtils.DATE_MAIN));
        }
    }

    public void onClkChange(View view) {
        Log.e("size", trigger.listViews.size() + " - no of listeners");
        trigger.start(new CompositeViews.Update() {
            @Override
            public void postOnView(View view) {
                updateView((TextView) view);
            }
        });

    }

    private void updateView(TextView view) {
//        String str = view.getText().toString();
        String str = (String) view.getTag() ;
        long currentMilliSeconds = Calendar.getInstance().getTimeInMillis();
        if (CalendarUtils.getTimeMilliSeconds(str,CalendarUtils.DATE_MAIN) > currentMilliSeconds)
            view.setTextColor(Color.RED);
        else
            view.setTextColor(Color.GREEN);
    }

    public static class CompositeViews {
        private List<View> listViews = new ArrayList<View>();

        public void addView(View view) {
            listViews.add(view);
        }

        public void start(Update listener) {
            for (View v : listViews) {
                if (listener != null)
                    listener.postOnView(v);
            }
        }

        interface Update {
            void postOnView(View view);
        }
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.CommonViewHolder> {
        private Vector<String> vector;

        public Adapter(Vector<String> vector) {
            this.vector = vector;
        }

        @NonNull
        @Override
        public Adapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
            return new CommonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.CommonViewHolder holder, int position) {
            holder.getTextView().setText(vector.get(position));
            holder.getTextView().setTag(vector.get(position));
            updateView(holder.getTextView());
        }

        @Override
        public int getItemCount() {
            return vector != null ? vector.size() : 0;
        }

        public void refresh(Vector<String> vector) {
            this.vector = vector;
        }

        public Vector<String> getList() {
            return vector;
        }

        public class CommonViewHolder extends RecyclerView.ViewHolder {
            private TextView tv;

            public TextView getTextView() {
                return tv;
            }

            public CommonViewHolder(@NonNull View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tvMessage);
                trigger.addView(tv);
            }
        }
    }

}
