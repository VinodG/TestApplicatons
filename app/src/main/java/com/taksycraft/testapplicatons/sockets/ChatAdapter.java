package com.taksycraft.testapplicatons.sockets;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.common.NameDO;
import com.taksycraft.testapplicatons.customviews.BackgroundDrawable;
import com.taksycraft.testapplicatons.recyclerview.CommonDiffAdapter;
import com.taksycraft.testapplicatons.recyclerview.TriggerRecyclerViewItemsActivity;
import com.taksycraft.testapplicatons.sqlite.ChatObj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import static com.taksycraft.testapplicatons.retrofit.DBClient.LOCK;

public   class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyAdapter> {
    private CompositeViews trigger;
    private String TAG = ChatAdapter.class.getSimpleName() ;
    private String FROM_ID;
    private UserDO selectedUser;
    private UserDO currentUser;
    public Vector<ChatObj> vecChat;
    private static final int FROM_COLOR = Color.RED;
    private static final int TO_COLOR = Color.BLUE;
    private String lastSyncTime="2019-01-01 00:00:00";
    private RecyclerView rvChat;


    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public ChatAdapter(/*Context context, */Vector<ChatObj> vecChat,UserDO currentUser, UserDO selectedUser ) {
//        this.vecChat = new Vector<ChatObj>();
//        this.vecChat.addAll(vecChat) ;
        this.vecChat=vecChat;
        this.currentUser = currentUser;
        this.selectedUser = selectedUser;
        trigger = new CompositeViews();
        FROM_ID= currentUser.getCustomer_id();
    }

    @Override
    public MyAdapter onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(  MyAdapter holder, int position) {
        Log.e(TAG, "onBindViewHolder "+position);
        ChatObj chatDO = vecChat.get(position);
        holder.tvMessage.setText(chatDO.message + "  -  " + position);
        if (chatDO.from.equalsIgnoreCase(currentUser.getCustomer_id())) {
            holder.tvFrom.setText(currentUser.getName());
        } else {
            holder.tvFrom.setText(selectedUser.getName());
        }
        holder.tvTimeStamp.setText(chatDO.sent_at + "");
        holder.tvMessage.setTag(position);
        updateSeenView(holder.tvMessage,lastSyncTime);
//        if (chatDO.status == 1)
//        {
//            if((TextUtils.isEmpty(chatDO.seen_at ) ||  chatDO.seen_at.equalsIgnoreCase("null" ))
//                    && chatDO.from.equalsIgnoreCase(FROM_ID))
//                holder.tvMessage.setTextColor(Color.YELLOW);
//            else
//                holder.tvMessage.setTextColor(Color.BLACK);
//        }
//        else
//            holder.tvMessage.setTextColor(Color.GRAY);

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

    @Override
    public int getItemCount() {
        return vecChat != null ? vecChat.size() : 0;
    }

    //    public void refreshRange(Vector<ChatObj> vecChat,int start,int end ) {
//
//
//        synchronized (LOCK) {
//            //old
//            this.vecChat = vecChat;
//            notifyItemRangeChanged(start,end);
//            updateSeenStatus(lastSyncTime);
//        }
//
//    }
    public void refresh(Vector<ChatObj> vecChat) {


//        synchronized (LOCK) {

        try {
            int oldSize;
            if(vecChat==null || vecChat.size() == 0)
                oldSize = 0 ;
            else
                oldSize  = vecChat.size() ;

            int diff = vecChat.size() - this.vecChat.size();
            this.vecChat = vecChat;
            notifyDataSetChanged();
            for(int i= 0;i<=diff;i++)
            {
                notifyItemInserted(oldSize +i );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //old
//        this.vecChat = vecChat;
//        notifyItemInserted(vecChat.size());
//        }

    }
    public void updateSeenStatus (final String lastSyncTime){
//        synchronized (LOCK) {
        Log.e(TAG, "last sync time "+this.lastSyncTime+"  current sync "+lastSyncTime+ "  "+vecChat.get(vecChat.size()-1).server_time);
        this.lastSyncTime = lastSyncTime;
        if(!( TextUtils.isEmpty(lastSyncTime)  || lastSyncTime.equalsIgnoreCase("null") ))
        {
            trigger.start(new CompositeViews.Update() {
                @Override
                public void postOnView(View view) {
                    updateSeenView(view, lastSyncTime);
                }
            });
        }
//        }

    }

    private void updateSeenView(View view,String lastSyncTime) {
        int position= (int) view.getTag() ;
        ChatObj obj = vecChat.get(position) ;
        Log.e(TAG, "update position"+position+"  - servertime"+obj.server_time+ "lastSyncTime "+lastSyncTime);
        if(obj!=null )
        {
            if(obj.status == 1){
                if(!(TextUtils.isEmpty(lastSyncTime) || lastSyncTime.equalsIgnoreCase("null")))
                {
                    String serverTime = obj.server_time;
                    long currentMilliSeconds =  CalendarUtils.getTimeMilliSeconds(lastSyncTime) ;
                    if(!obj.from.equalsIgnoreCase(FROM_ID)){
                        ((TextView)view).setTextColor(Color.BLACK);
                    }else if((!(TextUtils.isEmpty(serverTime) ||   serverTime.equalsIgnoreCase("null" ))))
                    {
                        if (CalendarUtils.getTimeMilliSeconds(serverTime,CalendarUtils.DATE_MAIN) > currentMilliSeconds)
                            ((TextView)view).setTextColor(Color.YELLOW);
                        else
                            ((TextView)view).setTextColor(Color.BLACK);
                    }


                }
            }else
                ((TextView)view).setTextColor(Color.GRAY);

        }
//        ChatObj obj = (ChatObj) view.getTag() ;
//        Log.e(TAG, "updating view "+((TextView)view).getText().toString()+"  "+obj.message);
//        if(obj!=null )
//        {
//            if(obj.status == 1){
//                if(!(TextUtils.isEmpty(lastSyncTime) || lastSyncTime.equalsIgnoreCase("null")))
//                {
//                    String serverTime = obj.server_time;
//                    long currentMilliSeconds =  CalendarUtils.getTimeMilliSeconds(lastSyncTime) ;
//                    if(!obj.from.equalsIgnoreCase(FROM_ID)){
//                         ((TextView)view).setTextColor(Color.BLACK);
//                    }else if((!(TextUtils.isEmpty(serverTime) ||   serverTime.equalsIgnoreCase("null" ))))
//                    {
//                        if (CalendarUtils.getTimeMilliSeconds(serverTime,CalendarUtils.DATE_MAIN) > currentMilliSeconds)
//                            ((TextView)view).setTextColor(Color.YELLOW);
//                        else
//                            ((TextView)view).setTextColor(Color.BLACK);
//                    }
//
//
//                }
//            }else
//                ((TextView)view).setTextColor(Color.GRAY);
//
//        }

    }
   /* public void updateList(final Vector<ChatObj> vecNew) {
        ChatAdapter.MyDiffUtils diffUtils = new ChatAdapter.MyDiffUtils(vecChat, vecNew);
        final DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) DiffUtil.calculateDiff(diffUtils,true );
        vecChat.clear();
        vecChat.addAll(vecNew);
        diffResult.dispatchUpdatesTo( this);
    }*/

    public void refreshNotifiy(Vector<ChatObj> vec) {
        vecChat =vec;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView rvChat) {
        this.rvChat= rvChat;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        public final TextView tvMessage;
        private final TextView tvTimeStamp;
        private final TextView tvFrom;
        private final LinearLayout llParent;
        private final LinearLayout llDrawable;

        public MyAdapter(  View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
            llDrawable = (LinearLayout) itemView.findViewById(R.id.llDrawable);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
            trigger.addView(tvMessage);
        }
    }

    public static class CompositeViews {
        private List<View> listViews = new ArrayList<View>();

        public void addView(View view) {
            listViews.add(view);
        }

        public void start(ChatAdapter.CompositeViews.Update listener) {
            for (View v : listViews) {
                if (listener != null)
                    listener.postOnView(v);
            }
        }

        interface Update {
            void postOnView(View view);
        }

    }
}