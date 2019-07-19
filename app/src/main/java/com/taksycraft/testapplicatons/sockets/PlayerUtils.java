package com.taksycraft.testapplicatons.sockets;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

public class PlayerUtils {
    private Handler handlerProgress = new Handler();
    private MediaPlayer mediaPlayer= new MediaPlayer();
    private String url;
    public Listener listener;
    private int duration;
    private boolean isCompleted =true;
    private boolean isPaused = false;


    public boolean isCompleted() {
        return isCompleted;
    }

    public void attach(Listener listener) {
        this.listener = listener;
    }

    public void setUrl(String url )
    {
        this.url = url ;
    }
    public  void start( )
    {
        String url = this.url;
        try {
            if (mediaPlayer != null){
                try {
                    mediaPlayer.release();
                    mediaPlayer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isCompleted = false;
                isPaused = false;

                handlerProgress.removeCallbacks(runnable);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                if(listener!=null)
                {
                    listener.onStart();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if(listener!=null){
                            listener.onProgress(0,duration);
                            listener.onFinish();
                            isCompleted = true;
                        }
                    }
                });
                mediaPlayer.start();
                duration = mediaPlayer.getDuration();
                handlerProgress.post(runnable);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null && listener!=null ){
                listener.onProgress(mediaPlayer.getCurrentPosition(),duration);
            }
            if(!isCompleted)
            {
                handlerProgress.postDelayed(this, 300);
            }

        }
    };
    public void pause()
    {
        isPaused =true;
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void resume()
    {
        if(mediaPlayer!=null  && isPaused ==true)
        {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
            isPaused=false;
        }
    }
    public void setProgress(int progress) {
        try {
            if(mediaPlayer!=null  && mediaPlayer.isPlaying() )
            {
                mediaPlayer.seekTo(progress);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface Listener
    {
        public void onProgress(int progress, int duration);
        public void onStart();
        public void onFinish();
    }
}
