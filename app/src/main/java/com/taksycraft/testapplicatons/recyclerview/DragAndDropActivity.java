package com.taksycraft.testapplicatons.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taksycraft.testapplicatons.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.http.HEAD;

public class DragAndDropActivity extends AppCompatActivity {

    private RecyclerView rvUp,rvDown;
    private RecyclerViewAdapter adapterTop,adapterBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);
        rvUp = (RecyclerView) findViewById(R.id.rvUp);
        rvDown = (RecyclerView) findViewById(R.id.rvDown);
        List<ItemDO> list = getData();
        adapterTop = new RecyclerViewAdapter(list) ;
        setCustomGridLayout();
        setRecyclerView(rvUp, adapterTop);
//        adapterBottom = new RecyclerViewAdapter(list ) ;
//        setRecyclerView(rvDown, adapterBottom);
    }

    private void setRecyclerView(RecyclerView recyclerView,   RecyclerViewAdapter adapter) {
        ItemTouchHelper.Callback  callback = new SimpleItemTouchHelperCallBack(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void setCustomGridLayout() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // Create a custom SpanSizeLookup where the first item spans both columns
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                return position == 2 ? 3 : 1;
                return adapterTop.getList().get(position).type == RecyclerViewAdapter.HEADER ? 3 : 1;
            }
        });
        rvUp.setLayoutManager(layoutManager);
    }
    private List<ItemDO> getData(){
        List<ItemDO> list = new ArrayList<>();
        ItemDO obj;
        obj = new ItemDO();
        obj.name = "Quick Access ";
        obj.type = RecyclerViewAdapter.HEADER;
        list.add(obj);
        for (int i = 1; i<=8; i++){
            obj = new ItemDO();
            obj.name = "ITEM ------ "+(i);
            obj.type = RecyclerViewAdapter.ITEM;
            list.add(obj);
        }
        obj = new ItemDO();
        obj.name = "Drag And Drop(Store)";
        obj.type = RecyclerViewAdapter.HEADER;
        list.add(obj);
        for (int i = 10 ;i<=20;i++){
            obj = new ItemDO();
            obj.name ="ITEM ------ "+(i);
            obj.type =RecyclerViewAdapter.ITEM;
            list.add(obj);
        }
        return list;
    }

    public interface ItemTouchHelperAdapter{
        void onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter{
        private static final String TAG = "RecyclerViewAdapter";
        private static final int HEADER = 0;
        private static final int ITEM = 1;
        List<ItemDO> list = new ArrayList<>();
        public RecyclerViewAdapter(List<ItemDO> list) {
            this.list = list;
        }

        public List<ItemDO> getList() {
            return list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == HEADER)
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false)) ;
            else
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_with_image, null, false)) ;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemDO obj = list.get(position);
            if(obj.type == HEADER){
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.tvMessage.setText(obj.name);
            }else{
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.tvId.setText(obj.name);
                myViewHolder.tvTitle.setText(obj.name);
                myViewHolder.pbLoader.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return list==null ? 0 : list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).type;
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            if( list.get(fromPosition).type == ITEM && toPosition !=0){
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                notifyItemMoved(fromPosition, toPosition);
            }

        }

        @Override
        public void onItemDismiss(int position) {
            Log.e(TAG, "onItemDismiss: "+position  );
            list.remove(position);
            notifyItemRemoved(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private  ProgressBar pbLoader;
            private  TextView tvId,tvTitle;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = (TextView)itemView.findViewById(R.id.tvId);
                tvTitle = (TextView)itemView.findViewById(R.id.title);
                pbLoader = (ProgressBar)itemView.findViewById(R.id.pbLoader);
            }
        }
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            private  TextView  tvMessage;
            private  View  view;

            public HeaderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvMessage = (TextView)itemView.findViewById(R.id.tvMessage);
                view =itemView;
            }
        }
    }
//    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter{
//        private static final String TAG = "RecyclerViewAdapter";
//        List<ItemDO> list = new ArrayList<>();
//        public RecyclerViewAdapter(List<ItemDO> list) {
//            this.list = list;
//
//        }
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            if(viewType == 0)
//                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_with_image, null, false)) ;
//            else
//                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false)) ;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            ItemDO obj = list.get(position);
//            holder.tvId.setText(obj.name);
//            holder.tvTitle.setText(obj.name);
//            holder.pbLoader.setVisibility(View.GONE);
//        }
//
//        @Override
//        public int getItemCount() {
//            return list==null ? 0 : list.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return list.get(position).type;
//        }
//
//        @Override
//        public void onItemMove(int fromPosition, int toPosition) {
//            if (fromPosition < toPosition) {
//                for (int i = fromPosition; i < toPosition; i++) {
//                    Collections.swap(list, i, i + 1);
//                }
//            } else {
//                for (int i = fromPosition; i > toPosition; i--) {
//                    Collections.swap(list, i, i - 1);
//                }
//            }
//            notifyItemMoved(fromPosition, toPosition);
//        }
//
//        @Override
//        public void onItemDismiss(int position) {
//            Log.e(TAG, "onItemDismiss: "+position  );
//            list.remove(position);
//            notifyItemRemoved(position);
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            private  ProgressBar pbLoader;
//            private  TextView tvId,tvTitle;
//
//            public MyViewHolder(@NonNull View itemView) {
//                super(itemView);
//                tvId = (TextView)itemView.findViewById(R.id.tvId);
//                tvTitle = (TextView)itemView.findViewById(R.id.title);
//                pbLoader = (ProgressBar)itemView.findViewById(R.id.pbLoader);
//            }
//        }
//    }

    class SimpleItemTouchHelperCallBack extends ItemTouchHelper .Callback{
        private   final String TAG = "SimpleItemTouchHelper";
        private  RecyclerViewAdapter adapter;
        public SimpleItemTouchHelperCallBack(RecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.ANIMATION_TYPE_DRAG|ItemTouchHelper.END|ItemTouchHelper.START;
//            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            int swipeFlags = 0 ;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int startPosition = viewHolder.getAdapterPosition();
            int endPosition = target.getAdapterPosition();
            Log.e(TAG, "onMove: "+startPosition+" - "+endPosition  );
            adapter.onItemMove(startPosition , endPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }
    class ItemDO{
        String name;
        int type;
    }
}