package com.taksycraft.testapplicatons.sockets;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.customviews.BackgroundDrawable;
import com.taksycraft.testapplicatons.sqlite.ChatObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


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
    private int  selectedPosition = -1;
    private PlayerUtils player = new PlayerUtils();


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
    public void onBindViewHolder(  final MyAdapter holder, final int position) {
        Log.e(TAG, "onBindViewHolder "+position);
        final ChatObj chatDO = vecChat.get(position);
        holder.tvMessage.setText(chatDO.message + "  -  " + position);
        if (chatDO.from.equalsIgnoreCase(currentUser.getCustomer_id())) {
            holder.tvFrom.setText(currentUser.getName());
        } else {
            holder.tvFrom.setText(selectedUser.getName());
        }
        holder.tvTimeStamp.setText(chatDO.sent_at + "");
        holder.tvMessage.setTag(position);
        updateSeenView(holder.tvMessage,lastSyncTime);
        holder.setSelectedPosition(position);
        if(selectedPosition  == position)
            player.attach(holder);
        else
            holder.getSkVoiceView().setProgress(0);

        if (chatDO.from.equalsIgnoreCase(FROM_ID)) {
//            if(!TextUtils.isEmpty(chatDO.file_url))
//            {
//                holder.llParent.setGravity(Gravity.LEFT);
//                holder.llDrawable.setVisibility(View.GONE);
//                holder.llVoice.setVisibility(View.VISIBLE);
//            }else
            {
                holder.llParent.setVisibility(View.VISIBLE);
                holder.llVoice.setVisibility(View.GONE);
                holder.llParent.setGravity(Gravity.LEFT);
                holder.llDrawable.setGravity(Gravity.LEFT);
                holder.llDrawable.setBackground(
                        new BackgroundDrawable()
                                .setCornerRadius(12)
                                .setBackgroundColor(Color.parseColor("#bdd0ef"))
                                .getDrawable()
                );
                holder.tvFrom.setTextColor(FROM_COLOR);
            }

        } else {
//            if(!TextUtils.isEmpty(chatDO.file_url))
//            {
//                holder.llParent.setGravity(Gravity.RIGHT);
//                holder.llDrawable.setVisibility(View.GONE);
//                holder.llVoice.setVisibility(View.VISIBLE);
//            }else
                {
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
        holder.getPausePlayView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition != position)
                {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    holder.setSelectedPosition(selectedPosition);
                    player.setUrl(chatDO.file_url);
                    player.start();
                }else
                {
                    selectedPosition = position;
                    holder.setSelectedPosition(selectedPosition);
                    player.setUrl(chatDO.file_url);
                    if(player.isCompleted())
                        player.start();
                    else if(player.isPaused())
                        player.resume();
                    else
                        player.pause();
                }
                player.attach(holder);

            }
        });
    }

    @Override
    public int getItemCount() {
        return vecChat != null ? vecChat.size() : 0;
    }

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

    }

    public void refreshNotifiy(Vector<ChatObj> vec) {
        vecChat =vec;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView rvChat) {
        this.rvChat= rvChat;
    }

    public class MyAdapter extends RecyclerView.ViewHolder implements PlayerUtils.Listener {
        public final TextView tvMessage,tvDuration;
        private final TextView tvTimeStamp;
        private final TextView tvFrom;
        private final LinearLayout llParent;
        private final LinearLayout llDrawable,llVoice;
        private final SeekBar skVoice;
        private final ImageView ivPausePlay;
        private int setSelectedPosition;
        private PlayerUtils player = new PlayerUtils();

        public MyAdapter(  View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
            llDrawable = (LinearLayout) itemView.findViewById(R.id.llDrawable);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);

            llVoice = (LinearLayout) itemView.findViewById(R.id.llVoice);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            skVoice = (SeekBar) itemView.findViewById(R.id.skVoice);
            ivPausePlay = (ImageView) itemView.findViewById(R.id.ivPausePlay);

            skVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    Log.e(TAG, "onProgressChanged "+i+", "+b);
//                    if(b)
                    if(selectedPosition == setSelectedPosition)
                        player.setProgress(i);
//                    if(player.isCompleted())
//                        skVoice.setProgress(0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.e(TAG, "onStartTrackingTouch "  );
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.e(TAG, "onStopTrackingTouch "  );
                }
            });

            trigger.addView(tvMessage);
        }

        public ImageView getPausePlayView() {
            return ivPausePlay;
        }

        public SeekBar getSkVoiceView() {
            return skVoice;
        }

        public TextView getTextView() {
            return tvDuration;
        }
        @Override
        public void onProgress(int progress, int duration) {
            Log.e(TAG, " on progress "+selectedPosition +" adapter "+setSelectedPosition);
            if(selectedPosition == setSelectedPosition)
            {
                getSkVoiceView().setMax(duration);
                getSkVoiceView().setProgress(progress);
                setProgress(progress,getTextView());
            }
            else
            {
                getSkVoiceView().setProgress(0);
            }

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
//            if(player.isCompleted())
                getSkVoiceView().setProgress(0);
        }

        public void setSelectedPosition(int selectedPosition) {
            this.setSelectedPosition = selectedPosition;
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
    private void setProgress(long millSeconds,TextView tvProgress) {
        millSeconds = millSeconds/1000;
        tvProgress.setText( ((int) millSeconds/60)+":"+ ((int) millSeconds%60));
    }
}