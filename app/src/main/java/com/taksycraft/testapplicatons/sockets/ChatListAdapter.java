package com.taksycraft.testapplicatons.sockets;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.customviews.BackgroundDrawable;
import com.taksycraft.testapplicatons.sqlite.ChatObj;

public class ChatListAdapter extends ListAdapter<ChatObj, ChatListAdapter.MyAdapter> {

    private static  String FROM_ID = "";
    private static final int FROM_COLOR = Color.RED;
    private static final int TO_COLOR = Color.BLUE;
    private UserDO selectedUser;
    private UserDO currentUser;

    protected ChatListAdapter(UserDO currentUser, UserDO selectedUser ) {
        super(new DiffUtil.ItemCallback<ChatObj>() {
            @Override
            public boolean areItemsTheSame(@NonNull ChatObj oldItem, @NonNull ChatObj newItem) {
                return   oldItem._id.equals(newItem._id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ChatObj oldItem, @NonNull ChatObj newItem) {
                return    oldItem.equals(newItem) ;
            }
        });
        this.currentUser = currentUser;
        this.selectedUser = selectedUser;
        FROM_ID= currentUser.getCustomer_id();
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        ChatObj chatDO = getItem(position);
        holder.tvMessage.setText(chatDO.message + "  -" + position);
        if (chatDO.from.equalsIgnoreCase(currentUser.getCustomer_id())) {
            holder.tvFrom.setText(currentUser.getName());
        } else {
            holder.tvFrom.setText(selectedUser.getName());
        }
        holder.tvTimeStamp.setText(chatDO.sent_at + "");
//            holder.tvTimeStamp.setText(CalendarUtils.getData(Calendar.getInstance().getTimeInMillis(),
//                    CalendarUtils.DATE_MAIN));

        if (chatDO.status == 1)
        {
            holder.tvMessage.setTextColor(Color.BLACK);
        }
        else
            holder.tvMessage.setTextColor(Color.GRAY);

        if (chatDO.from.equalsIgnoreCase(FROM_ID)) {
            holder.llParent.setGravity(Gravity.LEFT);
            holder.llDrawable.setGravity(Gravity.LEFT);
            holder.llDrawable.setBackground(
                    new BackgroundDrawable()
                            .setCornerRadius(12)
                            .setBackgroundColor(Color.parseColor("#bdd0ef"))
                            .getDrawable()
            );
            holder.tvFrom.setTextColor(FROM_COLOR);
//                if(chatDO.isMsgRead ==false )
//                {
//                    chatDO.isMsgRead =true;
//                    try {
//                        myRef.child(chatDO.key).setValue(chatDO);
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
        } else {
            holder.llParent.setGravity(Gravity.RIGHT);
            holder.llDrawable.setGravity(Gravity.RIGHT);
            holder.llDrawable.setBackground(
                    new BackgroundDrawable()
                            .setCornerRadius(12)
                            .setBackgroundColor(Color.parseColor("#edd9b8"))
                            .getDrawable()
            );
            holder.tvFrom.setTextColor(TO_COLOR);
        }
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        public final TextView tvMessage;
        private final TextView tvTimeStamp;
        private final TextView tvFrom;
        private final LinearLayout llParent;
        private final LinearLayout llDrawable;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
            llDrawable = (LinearLayout) itemView.findViewById(R.id.llDrawable);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
        }
    }
}
