package com.taksycraft.testapplicatons.common;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    private final TextView tv;

    public TextView getTextView() {
        return tv;
    }

    public CommonViewHolder(@NonNull View itemView) {
        super(itemView);
        tv = (TextView )itemView.findViewById(R.id.tvMessage);
    }
}
