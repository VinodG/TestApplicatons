package com.taksycraft.testapplicatons.recyclerview;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
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

public class RecyclerViewWithVoiceChatItemsActivity extends AppCompatActivity {

    private   String TAG =RecyclerViewWithVoiceChatItemsActivity.class.getSimpleName() ;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private CompositeViews trigger;
    private RecyclerView rv;
    private Vector<MusicDO> vec = new Vector<MusicDO>();
    private Adapter adapter;
    private MediaPlayer mediaPlayer;
    private Handler handlerProgress;


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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onClkChange(null);
//                rv.postDelayed(this,1000);
//            }
//        },1000);

    }

    private void setList() {

        for (int i = -5; i < 100; i++) {
            Calendar calendar =Calendar.getInstance();
            calendar.add(Calendar.SECOND,i);
            MusicDO musicDO = new MusicDO();
//            musicDO.url ="https://www.searchgurbani.com/audio/sggs/"+(i+6)+".mp3";
//            musicDO.url ="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-"+(((i+6)%16)+1)+".mp3";
//            musicDO.url ="http://159.65.150.142:9112/orb-in-rooms/chatFiles/5d079bbe8e4c2.mp3";
            musicDO.url ="http://159.65.150.142:9112/orb-in-rooms/chatFiles/5d079c365e6c8.mp3";
            musicDO.type ="mp3";
            musicDO.duration = "10:00";
            vec.add(musicDO);
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

    public class Adapter extends RecyclerView.Adapter<Adapter.CommonViewHolder>  {
        private Vector<MusicDO> vector;
        private  PlayerUtils player = new PlayerUtils();
        private int selectedPosition =-1;

        public Adapter(Vector<MusicDO> vector) {
            this.vector = vector;
        }

        @NonNull
        @Override
        public Adapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_record_item, null);
            return new CommonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Adapter.CommonViewHolder holder, final int position) {
            final MusicDO musicDO = vector.get(position);
            holder.getTextView().setText(musicDO.url);
//            updateView(holder.getTextView());
//            holder.setSelectedPosition(selectedPosition);
            holder.setSelectedPosition(position);
            if(selectedPosition  == position)
                player.attach(holder);
            else
                holder.getSkVoiceView().setProgress(0);

            holder.getPausePlayView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedPosition != position)
                    {
                        notifyItemChanged(selectedPosition);
                        selectedPosition = position;
                        holder.setSelectedPosition(selectedPosition);
                        player.setUrl(musicDO.url);
                        player.start();
                    }else
                    {
                        selectedPosition = position;
                        holder.setSelectedPosition(selectedPosition);
                        player.setUrl(musicDO.url);
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
            return vector != null ? vector.size() : 0;
        }

        public void refresh(Vector<MusicDO> vector) {
            this.vector = vector;
        }

        public Vector<MusicDO> getList() {
            return vector;
        }

        public class CommonViewHolder extends RecyclerView.ViewHolder   implements PlayerUtils.Listener
        {
            private TextView tvDuration;
            private ImageView ivPausePlay;
            private SeekBar skVoice;
            private PlayerUtils.Listener listener;
            private int setSelectedPosition=-1;

            public ImageView getPausePlayView() {
                return ivPausePlay;
            }

            public SeekBar getSkVoiceView() {
                return skVoice;
            }

            public TextView getTextView() {
                return tvDuration;
            }

            public CommonViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
                skVoice = (SeekBar) itemView.findViewById(R.id.skVoice);
                ivPausePlay = (ImageView) itemView.findViewById(R.id.ivPausePlay);
                trigger.addView(tvDuration);
                skVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if(selectedPosition == setSelectedPosition)
                            player.setProgress(i);
                        if(player.isCompleted())
                            skVoice.setProgress(0);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
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
                getSkVoiceView().setProgress(0);
            }

            public void setSelectedPosition(int selectedPosition) {
                this.setSelectedPosition = selectedPosition;
            }
        }
    }
    private void setProgress(long millSeconds,TextView tvProgress) {
        millSeconds = millSeconds/1000;
        tvProgress.setText( ((int) millSeconds/60)+":"+ ((int) millSeconds%60));
    }

}
