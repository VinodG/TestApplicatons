package com.taksycraft.testapplicatons.sockets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketUtils {
    public static final String CONNECTION_UPDATES = "CONNECTION_UPDATES";
    private static final String URL = "http://192.168.0.114:3000";
    private static final String TAG = SocketUtils.class.getSimpleName();
    private static Socket socket;
    public static final String REGISTRATION ="registration";
    public static final String USER_LIST="userList";
    public static final String LISTENER_USER_LIST="broadcast"; //lister For userlist
    public static final String USER_AUTHORIZATION="userAuthorization";
    public static final String SYNC_CHAT_HISTORY="chatHistory";
    public static final String LISTENER_FOR_MESSAGES="receiveMessage";
    public static final String SENT_MESSAGE="sendMessage";
    public static final String SEEN_MESSAGES="checkMessageSeen";
    public static final String SEEN_UPDATED_MESSAGES="seenStatus";
    public static final String TEXT_TYPING="isTyping";
    public static final String LISTENER_TEXTING="typing";

    public static  void setContext(Context con ) {
         context = con ;
    }

    private static Context context;
    public static Socket getNewInstance()
    {
        socket =null;
        return getInstance();
    }

    public static Socket getInstance() {
        if (socket == null) {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            try {
                socket = IO.socket(URL, opts);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
            }
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " --connected");
                    Intent intent = new Intent(CONNECTION_UPDATES);
                    intent.putExtra(ChatUsingSocketActivity.IS_NETWORK_AVAILABLE,true);
                    try {
                        context.sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final Intent intent = new Intent(CONNECTION_UPDATES);
                    intent.putExtra(ChatUsingSocketActivity.IS_NETWORK_AVAILABLE,false);
                    Runnable runnable = new Runnable() {
                        public void run() {
                            context.sendBroadcast(intent);
                        }
                    };
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  disconnect");
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-error-  ");
                    Intent intent = new Intent(CONNECTION_UPDATES);
                    intent.putExtra(ChatUsingSocketActivity.IS_NETWORK_AVAILABLE,false);
                    try {
                        context.sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.connect();
        }
        return socket;
    }

}
