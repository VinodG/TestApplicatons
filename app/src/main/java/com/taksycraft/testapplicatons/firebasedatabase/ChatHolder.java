package com.taksycraft.testapplicatons.firebasedatabase;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;


class ChatHolder  extends RecyclerView.ViewHolder {
    public final TextView tvMessage;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
    }
}
