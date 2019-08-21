package com.taksycraft.testapplicatons.architecture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {
    private  List<Record> list;

    public RecordAdapter(ArrayList <Record> list) {
        this.list =list;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record,null);
        return new  RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvAmount.setText(list.get(position).amount);
        holder.tvName.setText(list.get(position).name);
        holder.tvOccupation.setText(list.get(position).occupation);

    }

    @Override
    public int getItemCount() {
        return list!=null ? list.size() : 0;
    }

    public void refresh(List<Record> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvOccupation;
        private final TextView tvAmount;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvOccupation = (TextView) itemView.findViewById(R.id.tvOccupation);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        }
    }
}
