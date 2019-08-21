package com.taksycraft.testapplicatons.backgroundtask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

//https://www.truiton.com/2015/01/android-bind-service-using-messenger/
public class InteractiveService extends Service {
    String TAG = "InteractiveService" ;
    Messenger interactiveMessanger = new Messenger(new IncomingHandler());
    public InteractiveService() {
        log();
    }

    @Override
    public IBinder onBind(Intent intent) {
        log();
        return interactiveMessanger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            log("handleMessage in "+TAG );
            Bundle data = msg.getData();
            String dataString = data.getString("MyString");
            Toast.makeText(getApplicationContext(),
                    dataString, Toast.LENGTH_SHORT).show();

            Messenger activityMessanger = msg.replyTo;

            TimeTickerThread thread = new TimeTickerThread(activityMessanger);
            thread.start();
//
//            Bundle b = new Bundle();
//            b.putString("timestamp", Calendar.getInstance().getTime().toString());
//            Message replyMsg = Message.obtain( );
//            replyMsg.setData(b);
//            try {
//                activityMessanger.send(replyMsg);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log();
        return START_STICKY;
    }

    private void log(String str)
    {
        Log.e(TAG,new Exception().getStackTrace()[1].getMethodName()+"->"+str );
    }
    private void log()
    {
        Log.e(TAG,new Exception().getStackTrace()[1].getMethodName());
    }

    private class TimeTickerThread extends Thread {
        private Messenger activityMessanger;

        public TimeTickerThread(Messenger msg ) {
        this.activityMessanger = msg;
        }

        @Override
        public void run() {
            super.run();

            while(true)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bundle b = new Bundle();
                b.putString("timestamp", Calendar.getInstance().getTime().toString());
                Message replyMsg = Message.obtain( );
                replyMsg.setData(b);
                try {
                    activityMessanger.send(replyMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
