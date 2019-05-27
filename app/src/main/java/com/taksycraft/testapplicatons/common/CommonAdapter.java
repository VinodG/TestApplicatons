package com.taksycraft.testapplicatons.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taksycraft.testapplicatons.R;
import java.util.Vector;

public class CommonAdapter  extends  RecyclerView.Adapter<CommonAdapter.CommonHolder>
{
    private   ListListener listener;
    public Vector<? extends Object> vec;
    public CommonAdapter(Vector<? extends Object> vec, ListListener listener)
    {
        this.vec = vec;
        this.listener =listener;
    }

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_rv_item,null);
        return new CommonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, final int position) {
        listener.onBindViewHolder(holder,position);
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(vec.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vec!=null ? vec.size():0;
    }

    public class CommonHolder extends RecyclerView.ViewHolder {
        private   TextView tv;
        View view = null;
        public View getParentView() {
            return view;
        }
        public TextView getTextView() {
            return tv;
        }
        public CommonHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv = (TextView)view.findViewById(R.id.tvMessage);
        }
    }
    public void refresh(Vector<? extends Object> vec )
    {
        this.vec = vec;
        notifyDataSetChanged();
    }
    public interface ListListener {
        public void onBindViewHolder( CommonAdapter.CommonHolder holder, int position);
        public void onItemClick( Object  object, int position );
    }
}