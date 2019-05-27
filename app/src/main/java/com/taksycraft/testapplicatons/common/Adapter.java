package com.taksycraft.testapplicatons.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;

import java.util.Vector;

public class Adapter extends RecyclerView.Adapter<CommonViewHolder> {
    private Vector<String> vector;
    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        holder.getTextView().setText(vector.get(position));
    }

    @Override
    public int getItemCount() {
        return vector!=null ? vector.size(): 0;
    }
    public void refresh(Vector<String> vector)
    {
        this.vector =vector;
    }

    public Vector<String> getList() {
        return vector;
    }
}
