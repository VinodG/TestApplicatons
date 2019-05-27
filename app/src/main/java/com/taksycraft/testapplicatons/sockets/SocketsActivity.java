package com.taksycraft.testapplicatons.sockets;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.customviews.BackgroundDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Vector;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketsActivity extends AppCompatActivity {

    private static final String URL = "http://192.168.0.121:3000";
    private String TAG = SocketsActivity.class.getSimpleName();
    private Socket socket;
    private EditText etInput;
    private TextView tvOutput;

    private Vector<ChatObj> vecChat = new Vector<ChatObj>();
    private RecyclerView rvChat;
        private String USER_NAME = "hareesh";
    private String TO_NAME = "vinod";
//    private String USER_NAME = "vinod";
//    private String TO_NAME = "harsha";
//    private String USER_NAME = "harsha";
//    private Object TO_NAME = "vinod";
    private static final int FROM_COLOR = Color.RED;
    private static final int TO_COLOR = Color.BLUE;
    private ChatAdapter adapter;
    private Button btnSynck;
    private UserDO currentUser;
    private UserDO selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        etInput = (EditText) findViewById(R.id.etInput);
        tvOutput = (TextView) findViewById(R.id.tvCurrentUserName);
        btnSynck = (Button) findViewById(R.id.btnSynck);
        btnSynck.setVisibility(View.VISIBLE);
        tvOutput.setMovementMethod(new ScrollingMovementMethod());
        setRecyclerView();
//        setSocketConnection();
        setOneToOneConnection();
    }

    private void setOneToOneConnection() {
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
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.putOpt("name", USER_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callOneToOneChatSync();
                    }
                });
                socket.emit("registration",
                        jsonObject,
                        new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  -->"
                                        /*+args[0]*/);

                            }
                        });
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  disconnect");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-error-  " + args[0]);
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                addToList(args[0]);
            }
        });
        socket.connect();
    }

    private void addToList(Object... args) {
        try {
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " name new message " + args[0]);
            final ChatObj obj = new Gson().fromJson(args[0] + "", ChatObj.class);
            ChatObj chatObj = obj;
            chatObj.from = obj.from;
            chatObj.to = obj.to;
            chatObj.text = obj.message;
            vecChat.add(chatObj);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvOutput.setText(obj.text);
                    adapter.refresh(vecChat);
                }
            });
            toast("message -> " + args[0]);
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " newMessage " + args[0]);
        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace()[0].getMethodName() + " newMessage " + args[0]);
            e.printStackTrace();
        }
    }

    private void toast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(SocketsActivity.this, s + "", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void sendOnetoOneMessage() {
        String str = etInput.getText().toString();
        ChatObj chatObj = new ChatObj();
        chatObj.from = USER_NAME;
        chatObj.text = str;
        adapter.refresh(vecChat);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("from", USER_NAME);
            jsonObject.putOpt("to", TO_NAME);
            jsonObject.putOpt("socket_id", socket.id());
            jsonObject.putOpt("message", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("personalChat",
                jsonObject,
                new Ack() {
                    @Override
                    public void call(final Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addToList(args[0]);
                                etInput.setText("");
                            }
                        });

                        toast("personalChat  -> " + args[0]);
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " join  -->"
                                /*+args[0]*/);
                    }
                });


    }

    private void setSocketConnection() {

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
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.putOpt("name", USER_NAME);
                    jsonObject.putOpt("room", "123");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("join",
                        jsonObject,
                        new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " join  -->"
                                        /*+args[0]*/);
//                        Ack ack = (Ack) args[args.length - 1];
//                        ack.call();
                            }
                        });
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  disconnect");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-error-  " + args[0]);
            }
        });
        socket.on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " name new message " + args[0]);
                    final ChatObj obj = new Gson().fromJson(args[0] + "", ChatObj.class);
                    ChatObj chatObj = new ChatObj();
                    chatObj.from = obj.from;
                    chatObj.text = obj.text;
                    vecChat.add(chatObj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvOutput.setText(obj.text);
                            adapter.refresh(vecChat);
                        }
                    });
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " newMessage " + args[0]);
                } catch (Exception e) {
                    Log.e(TAG, e.getStackTrace()[0].getMethodName() + " newMessage " + args[0]);
                    e.printStackTrace();
                }
            }
        });
        socket.connect();
    }

    private void setRecyclerView() {
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ChatAdapter(this, vecChat);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                rvChat.scrollToPosition(positionStart - 1);
            }
        });
        rvChat.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void onClkPush(View view) {
//        sendGroupMessage();
        sendOnetoOneMessage();

    }

    private void sendGroupMessage() {
        String str = etInput.getText().toString();
        ChatObj chatObj = new ChatObj();
        chatObj.from = USER_NAME;
        chatObj.text = str;
        adapter.refresh(vecChat);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("createMessage", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {
                try {
                    tvOutput.setText(args[0] + "");
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " createMessage - " + args[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        etInput.setText("");
    }




    public void onClkSync(View view) {
//        callGroupChatSync();
        callOneToOneChatSync();

    }

    private void callOneToOneChatSync() {
        String str = etInput.getText().toString();
        ChatObj chatObj = new ChatObj();
        chatObj.from = USER_NAME;
        chatObj.text = str;
        adapter.refresh(vecChat);
        JSONObject jsonObject = new JSONObject();
        String createdAt = "2019-01-01 00:00:00";
        ChatObj obj=null;
        if ((vecChat != null ? vecChat.size() : 0) > 0) {
            obj = vecChat.get(vecChat.size() - 1);
            createdAt = obj.created_at;
        }
        try {
            jsonObject.put("created_at", createdAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " timestamp - last " +
                (obj==null ? new Gson().toJson(obj) :createdAt) );

        socket.emit("syncNew", jsonObject, new Ack() {
            @Override
            public void call(final Object... args) {
                try {

                    JSONArray array = new JSONArray(args[0] + "");
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            String str = array.get(i) + "";
                            ChatObj obj = new Gson().fromJson(str, ChatObj.class);
                            obj.text =obj.message;
                            if (obj != null) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " sync --> "+str
                                        +  "received msg " + i + " " + obj.created_at);
                                vecChat.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvOutput.setText(args[0] + "");
                                adapter.refresh(vecChat);
                            }
                        });

                    }
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " createMessage - " + args[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        etInput.setText("");
    }

    private void callGroupChatSync() {
        String str = etInput.getText().toString();
        ChatObj chatObj = new ChatObj();
        chatObj.from = USER_NAME;
        chatObj.text = str;
        adapter.refresh(vecChat);
        JSONObject jsonObject = new JSONObject();
        String createdAt = "2019-01-01 00:00:00";
        if ((vecChat != null ? vecChat.size() : 0) > 0) {
            ChatObj obj = vecChat.get(vecChat.size() - 1);
            createdAt = obj.created_at;
        }
        try {
            jsonObject.put("created_at", createdAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("sync", jsonObject, new Ack() {
            @Override
            public void call(final Object... args) {
                try {

                    JSONArray array = new JSONArray(args[0] + "");
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            String str = array.get(i) + "";
                            ChatObj obj = new Gson().fromJson(str, ChatObj.class);
                            if (obj != null) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " sync --> received msg " + i + " " + obj.created_at);
                                vecChat.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvOutput.setText(args[0] + "");
                                adapter.refresh(vecChat);
                            }
                        });

                    }
                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " createMessage - " + args[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        etInput.setText("");
    }

    class ChatObj {
        public String from = "";
        public String text = "";
        public String _id = "";
        public String romm = "";
        public String created_at = "";

        public String to = "";
        public String message = "";
    }
    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyAdapter> {
        private Vector<ChatObj> vecChat;

        public ChatAdapter(Context context, Vector<ChatObj> vecChat) {
            this.vecChat = vecChat;
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
            ChatObj chatDO = vecChat.get(position);
            holder.tvMessage.setText(chatDO.text + "  -" + position);
            holder.tvFrom.setText(chatDO.from);
            holder.tvTimeStamp.setText(CalendarUtils.getData(Calendar.getInstance().getTimeInMillis(),
                    CalendarUtils.DATE_MAIN));

//            if(chatDO.isMsgRead)
//                holder.tvMessage.setTextColor(Color.BLACK);
//            else
//                holder.tvMessage.setTextColor(Color.GRAY);

            if (chatDO.from.equalsIgnoreCase(USER_NAME)) {
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

        @Override
        public int getItemCount() {
            return vecChat != null ? vecChat.size() : 0;
        }

        public void refresh(Vector<ChatObj> vecChat) {
            this.vecChat = vecChat;
            notifyItemInserted(vecChat.size());
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

}
