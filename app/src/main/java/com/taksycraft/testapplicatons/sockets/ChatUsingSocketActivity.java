package com.taksycraft.testapplicatons.sockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.common.NetworkUtils;
import com.taksycraft.testapplicatons.sqlite.ChatObj;
import com.taksycraft.testapplicatons.sqlite.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.UUID;
import java.util.Vector;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.taksycraft.testapplicatons.retrofit.DBClient.LOCK;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.SEEN_MESSAGES;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.SEEN_UPDATED_MESSAGES;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.SENT_MESSAGE;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.SYNC_CHAT_HISTORY;

public class ChatUsingSocketActivity extends AppCompatActivity {

    private static final String URL = "http://192.168.0.121:3000";
    private String TAG = ChatUsingSocketActivity.class.getSimpleName();
    private EditText etInput;
    private TextView tvOutput;
    private Vector<ChatObj> vecChat = new Vector<ChatObj>();
    private RecyclerView rvChat;
    private String FROM_ID = "harsha";
    private String TO_ID = "vinod";
    private ChatAdapter adapter;
    //    private ChatListAdapter adapter;
    private Button btnSynck;
    private UserDO currentUser;
    private UserDO selectedUser;
    private ImageView ivIndicator;
    public static String IS_NETWORK_AVAILABLE = "IS_NETWORK_AVAILABLE";
    private DbHelper database;
    private String serverTime;
    public String DEFAULT_SYNC_TIME = "2019-01-01 00:00:00";
    private Button btnPickImage;
    private String lastSyncTime = "2019-01-01 00:00:00";
    private SyncDO syncDO = new SyncDO();
    private TextView tvTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SocketUtils.setContext(ChatUsingSocketActivity.this);
        database = new DbHelper(this, "chatdb.sqlite", null, 2);
        etInput = (EditText) findViewById(R.id.etInput);
        tvOutput = (TextView) findViewById(R.id.tvCurrentUserName);
        tvTyping = (TextView) findViewById(R.id.tvTyping);
        btnSynck = (Button) findViewById(R.id.btnSynck);
        btnPickImage = (Button) findViewById(R.id.btnPickImage);
        btnPickImage.setText("Update Msg Status");
        ivIndicator = (ImageView) findViewById(R.id.ivIndicator);
        btnSynck.setVisibility(View.GONE);
        currentUser = (UserDO) getIntent().getExtras().get("current_user");
        selectedUser = (UserDO) getIntent().getExtras().get("selected_user");
        tvOutput.setMovementMethod(new ScrollingMovementMethod());
        FROM_ID = currentUser.getCustomer_id();
        TO_ID = selectedUser.getCustomer_id();
        syncDO = database.getSyncDO(TO_ID);
        if(syncDO!=null )
        {
            lastSyncTime = syncDO.getSyncTime();
        }
        else {
            syncDO =new SyncDO();
            syncDO.setId(TO_ID);
        }

        setRecyclerView();
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendOnetoOneMessage();
                    handled = true;
                }
                return handled;
            }
        });
        setTitle("From - " + currentUser.getName() + ",  To - " + selectedUser.getName());

        if (!isInternetAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    vecChat = database.getRecords(-1);
                    vecChat = database.getRecords(-1,FROM_ID,TO_ID);
                    refreshAdapter();
                }
            }).start();
        } else {
            database.deleteAllRecords(FROM_ID,TO_ID);
        }
        setOneToOneConnection();
        listenMessagesFromUser();
        changedMessagesStatus(null);
        Log.e(TAG, "SocketId " + SocketUtils.getInstance().id());
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendTyping();
            }
        });
    }
    private void setRecyclerView() {
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(vecChat, currentUser, selectedUser);
        adapter.setRecyclerView(rvChat);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                rvChat.scrollToPosition(positionStart - 1);
                postUnseenMessages();
                refreshAdapterForLastSeen();
            }

        });
        rvChat.setAdapter(adapter);
        if (!isInternetAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    vecChat = database.getRecords(-1,FROM_ID,TO_ID);
//                    vecChat = database.getRecords(-1);
                    Runnable runnable = new Runnable() {
                        public void run() {
                            adapter.refresh(vecChat);
                        }
                    };
                    new Handler(Looper.getMainLooper()).post(runnable);
                }
            }).start();

        }
    }

    private void postUnseenMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Vector<ChatObj> vec = new Vector<ChatObj>();
                for (int i = vecChat.size() - 1; i >= 0; i--) {
                    ChatObj obj = vecChat.get(i);
                    if (obj.to.equalsIgnoreCase(FROM_ID) &&
                            (TextUtils.isEmpty(obj.seen_at)))
                        vec.add(obj);
                }
                if (vec != null && vec.size() > 0)
                {
                    updateMessageSeenAt(vec);
                }

            }
        }).start();
    }

    private void setOneToOneConnection() {
        SocketUtils.getNewInstance().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                ivIndicator.setBackgroundColor(Color.GREEN);
                            }
                        };
                        runOnUiThread(runnable);
                        doLogin();
                    }
                });
            }
        });
    }

    private void listenMessagesFromUser() {
        SocketUtils.getInstance().on(SocketUtils.LISTENER_FOR_MESSAGES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, "message received");
                JSONObject json = null;
                try {
                    json = new JSONObject(args[0] + "");
                    serverTime = json.optString("server_time");
                    Object obj = json.opt("data");
                    if (obj instanceof JSONArray) {
                        try {
                            JSONArray arry = new JSONArray(obj + "");
                            if (arry != null) {
                                for (int i = 0; i < arry.length(); i++) {
                                    Object object = arry.get(i);
                                    if (isValidObj(arry.get(i))) {
                                        final ChatObj o = new Gson().fromJson(object + "", ChatObj.class);
                                        ChatObj chatObj = o;
                                        chatObj.text = o.message;
                                        chatObj.status = ChatObj.POSTED;
//                                        chatObj.server_time = serverTime;
                                        addToList(object, serverTime);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvOutput.setText(o.text);
                                            }
                                        });
                                        database.addRecord(vecChat);
                                    }
                                }
                                setLastSyncTime(serverTime);
                                refreshAdapterForLastSeen();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void doLogin() {
        Log.e(TAG, "trying to reLogin");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("customer_id", currentUser.getCustomer_id());
            jsonObject.putOpt("room_no", currentUser.getRoom_no());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketUtils.getInstance().emit(SocketUtils.USER_AUTHORIZATION,
                jsonObject,
                new Ack() {
                    @Override
                    public void call(final Object... args) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " Login  has done -->");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Vector<ChatObj> vecToUpload = database.getRecords(0);
                                Vector<ChatObj> vecToUpload  = database.getRecords(0,FROM_ID,TO_ID);;
                                if (vecToUpload != null && vecToUpload.size() > 0)
                                    sendOfflineMessages(vecToUpload);
                                else {
                                    refreshChat();
                                }
                            }
                        });
                    }
                });
        SocketUtils.getInstance().on(SocketUtils.LISTENER_TEXTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, "message is typing");
                JSONObject json = null;
                try {
                    json = new JSONObject(args[0] + "");
                    final String typeFrom = json.optString("from");
                    String typeTo= json.optString("to");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(TextUtils.isEmpty(tvTyping.getText().toString()))
                                tvTyping.setText(selectedUser.getName() +" is typing... ");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tvTyping.setText("");
                                }
                            },3000);
                        }
                    });
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    private void sendTyping()
    {
        Log.e(TAG, "trying to typing");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("from", FROM_ID);
            jsonObject.putOpt("to", TO_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketUtils.getInstance().emit(SocketUtils.TEXT_TYPING,
                jsonObject,
                new Ack() {
                    @Override
                    public void call(final Object... args) {
                    }
                });
    }

    private void refreshChat() {
//        database.deleteAllRecords();
        vecChat.clear();
        callOneToOneChatSync(DEFAULT_SYNC_TIME);
    }

    private void addToList(Object args, String serverTime) {
        try {
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " name new message " + args);
            final ChatObj obj = new Gson().fromJson(args + "", ChatObj.class);
            ChatObj chatObj = obj;
            chatObj.text = obj.message;
            chatObj.status = ChatObj.POSTED;
//            chatObj.server_time = serverTime;
            saveInDatabase(chatObj);
            vecChat.add(chatObj);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvOutput.setText(obj.text);
//                    adapter.vecChat = vecChat ;
                    adapter.refresh(vecChat);
//                    adapter.submitList(vecChat);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLastSyncTime(final String lastSyncTime) {
        this.lastSyncTime= lastSyncTime;
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncDO.setSyncTime(lastSyncTime);
                database.addSyncTime(syncDO);
            }
        }).start();
    }

    private void toast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(ChatUsingSocketActivity.this, s + "", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void sendOnetoOneMessage() {
        String str = etInput.getText().toString();
//        refreshAdapter();
        if (TextUtils.isEmpty(str)) {
            toast("Please enter a text to send ");
        } else if (isInternetAvailable()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.putOpt("from", FROM_ID);
                jsonObject.putOpt("to", TO_ID);
                jsonObject.putOpt("message", str);
                jsonObject.putOpt("sent_at", CalendarUtils.getCurrentDateAndtime());
//                jsonObject.putOpt("message_id", UUID.randomUUID().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONArray array = new JSONArray();
            array.put(jsonObject);
            SocketUtils.getInstance().emit(SENT_MESSAGE,
                    array,
                    new Ack() {
                        @Override
                        public void call(final Object... args) {
                            synchronized (LOCK) {
                                dataFromPersonalChat(args[0] + "", false);
                            }
                        }
                    });
        } else {
            ChatObj chatObj = new ChatObj();
            chatObj.from = FROM_ID;
            chatObj.text = str;
            chatObj.to = TO_ID;
            chatObj.message = str;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            chatObj.server_time = CalendarUtils.getData(calendar.getTimeInMillis(), CalendarUtils.DATE_MAIN);
            chatObj.sent_at = CalendarUtils.getCurrentDateAndtime();
            chatObj._id = UUID.randomUUID().toString();
            chatObj.status = ChatObj.NOT_POSTED;
            vecChat.add(chatObj);
            saveInDatabase(chatObj);
            refreshAdapter();
            clearInputText();
        }
    }

    private void sendOfflineMessages(Vector<ChatObj> vec) {
        if (vec != null && vec.size() > 0) {
            final JSONArray array = new JSONArray();
            if (isInternetAvailable()) {
                for (int i = 0; i < vec.size(); i++) {
                    ChatObj obj = vec.get(i);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.putOpt("from", FROM_ID);
                        jsonObject.putOpt("to", TO_ID);
                        jsonObject.putOpt("message", obj.message);
                        jsonObject.putOpt("sent_at", obj.sent_at);
//                        jsonObject.putOpt("message_id", obj._id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(jsonObject);
                }

                SocketUtils.getInstance().emit(SENT_MESSAGE,
                        array,
                        new Ack() {
                            @Override
                            public void call(final Object... args) {
                                Log.e(TAG, "Second  personalChat " + args[0]);
                                database.deleteAllRecordsBasedOnStatus(0);
                                refreshChat();
                            }
                        });
            }
        }
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    private void dataFromPersonalChat(final String response, final boolean doesItRequireSync) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(response + "");
                    serverTime = json.optString("server_time");
//                    serverTime = CalendarUtils.getData(Long.parseLong(serverTime),CalendarUtils.DATE_MAIN);
                    Object obj = json.opt("data");
                    if (obj instanceof JSONArray) {
                        try {
                            JSONArray arry = new JSONArray(obj + "");
                            if (arry != null) {
                                for (int i = 0; i < arry.length(); i++) {
                                    Object object = arry.get(i);
                                    if (isValidObj(object + "")) {
                                        addToList(object + "", serverTime);
                                        etInput.setText("");
                                    }
                                }

                            }
//                            refreshAdapterForLastSeen();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isValidObj(Object object) {
        ChatObj obj = new Gson().fromJson(object + "", ChatObj.class);
        boolean isValid = true;

        try {
            isValid = (obj.from.equals(currentUser.getCustomer_id()) && obj.to.equals(selectedUser.getCustomer_id()) ||
                    (obj.to.equals(currentUser.getCustomer_id()) && obj.from.equals(selectedUser.getCustomer_id())));
        } catch (Exception e) {
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " exception -  object");
            e.printStackTrace();
            isValid = false;
        } finally {
            return isValid;
        }
    }

    private boolean isValidObj(ChatObj obj) {
        boolean isValid = true;
        try {
            isValid = (obj.from.equals(currentUser.getCustomer_id()) && obj.to.equals(selectedUser.getCustomer_id()) ||
                    (obj.to.equals(currentUser.getCustomer_id()) && obj.from.equals(selectedUser.getCustomer_id())));
        } catch (Exception e) {
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " exception -  ChatObj");
            e.printStackTrace();
            isValid = false;
        } finally {
            return isValid;
        }
    }

    private boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(ChatUsingSocketActivity.this);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketUtils.getInstance().disconnect();
    }

    public void onClkPush(View view) {
        sendOnetoOneMessage();
    }

    public void onClkSync(View view) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " calling Sync");
        String str = etInput.getText().toString();
        ChatObj chatObj = new ChatObj();
        chatObj.from = FROM_ID;
        chatObj.text = str;
        refreshAdapter();
        String createdAt = DEFAULT_SYNC_TIME;
        ChatObj obj = null;
        if ((vecChat != null ? vecChat.size() : 0) > 0) {
            obj = vecChat.get(vecChat.size() - 1);
            createdAt = obj.server_time;
        }
        callOneToOneChatSync(createdAt);
    }

    private void refreshAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    adapter.refresh(vecChat);
                }
            }
        });
    }

    private void callOneToOneChatSync(String createdAt) {
        toast("syncing...");
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("sent_at", CalendarUtils.getTimeMilliSeconds(createdAt,CalendarUtils.DATE_MAIN));
            jsonObject.put("sent_at",  createdAt );
            jsonObject.put("from", currentUser.getCustomer_id());
            jsonObject.put("to", selectedUser.getCustomer_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " timestamp - last " + createdAt);

        SocketUtils.getInstance().emit(SYNC_CHAT_HISTORY, jsonObject, new Ack() {
            @Override
            public void call(final Object... args) {
                try {

                    JSONArray array = new JSONArray(args[0] + "");
                    Log.e(TAG, "Sync messages count " + array.length() + "-->" + args[0]);
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            String str = array.get(i) + "";
                            ChatObj obj = new Gson().fromJson(str, ChatObj.class);
                            obj.text = obj.message;
//                            obj.server_time = CalendarUtils.getData(Long.parseLong(obj.server_time) ,CalendarUtils.DATE_MAIN);
                            if (obj != null) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " sync --> " + str
                                        + "received msg " + i + " " + obj.sent_at);
                                if (isValidObj(obj))
                                    vecChat.add(obj);
                            }
                        }
                        database.addRecord(vecChat);
//                        refreshAdapterForLastSeen();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvOutput.setText(args[0] + "");
                                try {
                                    adapter.refresh(vecChat);
                                    refreshAdapterForLastSeen();
//                                    rvChat.smoothScrollToPosition(vecChat.size()-1);
                                    postUnseenMessages( );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        clearInputText();

    }

    private void updateMessageSeenAt(final Vector<ChatObj> vecUpdateMsgStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (ChatObj obj : vecUpdateMsgStatus)
                array.put(obj._id + "");
            jsonObject.put("data", array);
            jsonObject.put("to", TO_ID);
            jsonObject.put("seen_at", CalendarUtils.getCurrentDateAndtime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketUtils.getInstance().emit(SEEN_MESSAGES, jsonObject, new Ack() {
            @Override
            public void call(final Object... args) {
                try {
                    JSONObject json = new JSONObject(args[0] + "");
//                    setLastSyncTime(json.optString("server_time"));
                    String seen_at = json.optString("seen_at");
                    Object o = json.optJSONArray("data");
                    JSONArray array = new JSONArray(o + "");
                    Log.e(TAG, "updated Sent messages count " + array.length() + "-->" + args[0]);
                    Vector<ChatObj> vecUpdated = new Vector<ChatObj>();
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            String str = array.get(i) + "";
                            ChatObj obj = new ChatObj();
                            obj._id = str;
                            obj.seen_at = seen_at;
                            vecUpdated.add(obj);
                        }
                        database.addRecord(vecUpdated,  null );
//                        adapter.refresh(vecChat );
//                        setLastSyncTime(serverTime);
                    }
                    refreshAdapterForLastSeen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        clearInputText();
    }

    private void changedMessagesStatus(String createdAt) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());

        SocketUtils.getInstance().on(SEEN_UPDATED_MESSAGES, new Emitter.Listener() {
            String serverTime;

            @Override
            public void call(final Object... args) {
                try {
                    JSONObject json = new JSONObject(args[0] + "");
                    String seet_at = json.optString("seen_at");
                    serverTime= json.optString("updated_at");
//                    long  server_time= json.optLong("updated_at");
//                    serverTime = CalendarUtils.getData(server_time,CalendarUtils.DATE_MAIN);
                    setLastSyncTime(serverTime); //serverTime
                    Object o = json.optJSONArray("data");
                    JSONArray array = new JSONArray(o + "");
                    Log.e(TAG, "updated Sent messages count " + array.length() + "-->" + args[0]);
                    Vector<ChatObj> vecUpdated = new Vector<ChatObj>();
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            String str = array.get(i) + "";
                            ChatObj obj = new ChatObj();
                            obj._id = str;
                            obj.seen_at = seet_at;
                            vecUpdated.add(obj);
                        }
                        Log.e(TAG, "serverTime " + serverTime);
                        database.addRecord(vecUpdated, null );
                    }
                    refreshAdapterForLastSeen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        clearInputText();

    }

    private void refreshAdapterForLastSeen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.updateSeenStatus(getLastSyncTime());
            }
        });
    }

    private void clearInputText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etInput.setText("");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(SocketUtils.CONNECTION_UPDATES));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getBooleanExtra(IS_NETWORK_AVAILABLE, true)) {
                ivIndicator.setBackgroundColor(Color.GREEN);
            } else
                ivIndicator.setBackgroundColor(Color.RED);
            Log.e(TAG, "SocketId " + SocketUtils.getInstance().id());

        }


    };

    public void saveInDatabase(final ChatObj obj) {
        Log.e(TAG, new Exception().getStackTrace()[1].getMethodName() + " message is inserted");
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.addRecord(obj);
            }
        }).start();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("LayoutManager", " mate a IOOBE in RecyclerView");
            }
        }
    }
//    private static final String URL = "http://192.168.0.121:3000";
//    private String TAG = ChatUsingSocketActivity.class.getSimpleName();
//    private EditText etInput;
//    private TextView tvOutput;
//    private Vector<ChatObj> vecChat = new Vector<ChatObj>();
//    private RecyclerView rvChat;
//    private String FROM_ID = "harsha";
//    private String TO_ID = "vinod";
//    private ChatAdapter adapter;
//    //    private ChatListAdapter adapter;
//    private Button btnSynck;
//    private UserDO currentUser;
//    private UserDO selectedUser;
//    private ImageView ivIndicator;
//    public static String IS_NETWORK_AVAILABLE = "IS_NETWORK_AVAILABLE";
//    private DbHelper database;
//    private String serverTime;
//    public String DEFAULT_SYNC_TIME = "2019-01-01 00:00:00";
//    private Button btnPickImage;
//    private String lastSyncTime = "2019-01-01 00:00:00";
//    private SyncDO syncDO = new SyncDO();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        SocketUtils.setContext(ChatUsingSocketActivity.this);
//        database = new DbHelper(this, "chatdb.sqlite", null, 2);
//        etInput = (EditText) findViewById(R.id.etInput);
//        tvOutput = (TextView) findViewById(R.id.tvCurrentUserName);
//        btnSynck = (Button) findViewById(R.id.btnSynck);
//        btnPickImage = (Button) findViewById(R.id.btnPickImage);
//        btnPickImage.setText("Update Msg Status");
//        ivIndicator = (ImageView) findViewById(R.id.ivIndicator);
//        btnSynck.setVisibility(View.GONE);
//        currentUser = (UserDO) getIntent().getExtras().get("current_user");
//        selectedUser = (UserDO) getIntent().getExtras().get("selected_user");
//        tvOutput.setMovementMethod(new ScrollingMovementMethod());
//        FROM_ID = currentUser.getCustomer_id();
//        TO_ID = selectedUser.getCustomer_id();
//        syncDO = database.getSyncDO(TO_ID);
//        if(syncDO!=null )
//        {
//            lastSyncTime = syncDO.getSyncTime();
//        }
//        else {
//            syncDO =new SyncDO();
//            syncDO.setId(TO_ID);
//        }
//
//        setRecyclerView();
//        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    sendOnetoOneMessage();
//                    handled = true;
//                }
//                return handled;
//            }
//        });
//        setTitle("From - " + currentUser.getName() + ",  To - " + selectedUser.getName());
//
//        if (!isInternetAvailable()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    vecChat = database.getRecords(-1);
//                    vecChat = database.getRecords(-1,FROM_ID,TO_ID);
//                    refreshAdapter();
//                }
//            }).start();
//        } else {
//            database.deleteAllRecords(FROM_ID,TO_ID);
//        }
//        setOneToOneConnection();
//        listenMessagesFromUser();
//        changedMessagesStatus(null);
//        Log.e(TAG, "SocketId " + SocketUtils.getInstance().id());
//    }
//    private void setRecyclerView() {
//        rvChat = (RecyclerView) findViewById(R.id.rvChat);
//        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
//        rvChat.setLayoutManager(layoutManager);
//        adapter = new ChatAdapter(vecChat, currentUser, selectedUser);
//        adapter.setRecyclerView(rvChat);
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                rvChat.scrollToPosition(positionStart - 1);
//                postUnseenMessages();
//                refreshAdapterForLastSeen();
//            }
//
//        });
//        rvChat.setAdapter(adapter);
//        if (!isInternetAvailable()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    vecChat = database.getRecords(-1,FROM_ID,TO_ID);
////                    vecChat = database.getRecords(-1);
//                    Runnable runnable = new Runnable() {
//                        public void run() {
//                            adapter.refresh(vecChat);
//                        }
//                    };
//                    new Handler(Looper.getMainLooper()).post(runnable);
//                }
//            }).start();
//
//        }
//    }
//
//    private void postUnseenMessages() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Vector<ChatObj> vec = new Vector<ChatObj>();
//                for (int i = vecChat.size() - 1; i > 0; i--) {
//                    ChatObj obj = vecChat.get(i);
//                    if (obj.to.equalsIgnoreCase(FROM_ID) &&
//                            (TextUtils.isEmpty(obj.seen_at)))
//                        vec.add(obj);
//                }
//                if (vec != null && vec.size() > 0)
//                {
//                    updateMessageSeenAt(vec);
//                }
//
//            }
//        }).start();
//    }
//
//    private void setOneToOneConnection() {
//        SocketUtils.getNewInstance().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Runnable runnable = new Runnable() {
//                            public void run() {
//                                ivIndicator.setBackgroundColor(Color.GREEN);
//                            }
//                        };
//                        runOnUiThread(runnable);
//                        doLogin();
//                    }
//                });
//            }
//        });
//    }
//
//    private void listenMessagesFromUser() {
//        SocketUtils.getInstance().on(SocketUtils.LISTENER_FOR_MESSAGES, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e(TAG, "message received");
//                JSONObject json = null;
//                try {
//                    json = new JSONObject(args[0] + "");
//                    serverTime = json.optString("server_time");
//                    Object obj = json.opt("data");
//                    if (obj instanceof JSONArray) {
//                        try {
//                            JSONArray arry = new JSONArray(obj + "");
//                            if (arry != null) {
//                                for (int i = 0; i < arry.length(); i++) {
//                                    Object object = arry.get(i);
//                                    if (isValidObj(arry.get(i))) {
//                                        final ChatObj o = new Gson().fromJson(object + "", ChatObj.class);
//                                        ChatObj chatObj = o;
//                                        chatObj.text = o.message;
//                                        chatObj.status = ChatObj.POSTED;
//                                        chatObj.server_time = serverTime;
//                                        addToList(object, serverTime);
//
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                tvOutput.setText(o.text);
//                                            }
//                                        });
//                                        database.addRecord(vecChat);
//                                    }
//                                }
//                                setLastSyncTime(serverTime);
//                                refreshAdapterForLastSeen();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//    private void doLogin() {
//        Log.e(TAG, "trying to reLogin");
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.putOpt("customer_id", currentUser.getCustomer_id());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SocketUtils.getInstance().emit(SocketUtils.USER_AUTHORIZATION,
//                jsonObject,
//                new Ack() {
//                    @Override
//                    public void call(final Object... args) {
//                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " Login  has done -->");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                Vector<ChatObj> vecToUpload = database.getRecords(0);
//                                Vector<ChatObj> vecToUpload  = database.getRecords(0,FROM_ID,TO_ID);;
//                                if (vecToUpload != null && vecToUpload.size() > 0)
//                                    sendOfflineMessages(vecToUpload);
//                                else {
//                                    refreshChat();
//                                }
//                            }
//                        });
//                    }
//                });
//    }
//
//    private void refreshChat() {
////        database.deleteAllRecords();
//        vecChat.clear();
//        callOneToOneChatSync(DEFAULT_SYNC_TIME);
//    }
//
//    private void addToList(Object args, String serverTime) {
//        try {
//            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " name new message " + args);
//            final ChatObj obj = new Gson().fromJson(args + "", ChatObj.class);
//            ChatObj chatObj = obj;
//            chatObj.text = obj.message;
//            chatObj.status = ChatObj.POSTED;
//            chatObj.server_time = serverTime;
//            saveInDatabase(chatObj);
//            vecChat.add(chatObj);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvOutput.setText(obj.text);
////                    adapter.vecChat = vecChat ;
//                    adapter.refresh(vecChat);
////                    adapter.submitList(vecChat);
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setLastSyncTime(final String lastSyncTime) {
//        this.lastSyncTime= lastSyncTime;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                syncDO.setSyncTime(lastSyncTime);
//                database.addSyncTime(syncDO);
//            }
//        }).start();
//    }
//
//    private void toast(final String s) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Toast.makeText(ChatUsingSocketActivity.this, s + "", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//    private void sendOnetoOneMessage() {
//        String str = etInput.getText().toString();
////        refreshAdapter();
//        if (TextUtils.isEmpty(str)) {
//            toast("Please enter a text to send ");
//        } else if (isInternetAvailable()) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.putOpt("from", FROM_ID);
//                jsonObject.putOpt("to", TO_ID);
//                jsonObject.putOpt("message", str);
//                jsonObject.putOpt("sent_at", CalendarUtils.getCurrentDateAndtime());
//                jsonObject.putOpt("message_id", UUID.randomUUID().toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            final JSONArray array = new JSONArray();
//            array.put(jsonObject);
//            SocketUtils.getInstance().emit(SENT_MESSAGE,
//                    array,
//                    new Ack() {
//                        @Override
//                        public void call(final Object... args) {
//                            synchronized (LOCK) {
//                                dataFromPersonalChat(args[0] + "", false);
//                            }
//                        }
//                    });
//        } else {
//            ChatObj chatObj = new ChatObj();
//            chatObj.from = FROM_ID;
//            chatObj.text = str;
//            chatObj.to = TO_ID;
//            chatObj.message = str;
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.YEAR, 1);
//            chatObj.server_time = CalendarUtils.getData(calendar.getTimeInMillis(), CalendarUtils.DATE_MAIN);
//            chatObj.sent_at = CalendarUtils.getCurrentDateAndtime();
//            chatObj._id = UUID.randomUUID().toString();
//            chatObj.status = ChatObj.NOT_POSTED;
//            vecChat.add(chatObj);
//            saveInDatabase(chatObj);
//            refreshAdapter();
//            clearInputText();
//        }
//    }
//
//    private void sendOfflineMessages(Vector<ChatObj> vec) {
//        if (vec != null && vec.size() > 0) {
//            final JSONArray array = new JSONArray();
//            if (isInternetAvailable()) {
//                for (int i = 0; i < vec.size(); i++) {
//                    ChatObj obj = vec.get(i);
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.putOpt("from", FROM_ID);
//                        jsonObject.putOpt("to", TO_ID);
//                        jsonObject.putOpt("message", obj.message);
//                        jsonObject.putOpt("sent_at", obj.sent_at);
//                        jsonObject.putOpt("message_id", obj._id);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    array.put(jsonObject);
//                }
//
//                SocketUtils.getInstance().emit(SENT_MESSAGE,
//                        array,
//                        new Ack() {
//                            @Override
//                            public void call(final Object... args) {
//                                Log.e(TAG, "Second  personalChat " + args[0]);
//                                refreshChat();
//                            }
//                        });
//            }
//        }
//    }
//
//    public String getLastSyncTime() {
//        return lastSyncTime;
//    }
//
//    private void dataFromPersonalChat(final String response, final boolean doesItRequireSync) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONObject json = new JSONObject(response + "");
//                    serverTime = json.optString("server_time");
////                    serverTime = CalendarUtils.getData(Long.parseLong(serverTime),CalendarUtils.DATE_MAIN);
//                    Object obj = json.opt("data");
//                    if (obj instanceof JSONArray) {
//                        try {
//                            JSONArray arry = new JSONArray(obj + "");
//                            if (arry != null) {
//                                for (int i = 0; i < arry.length(); i++) {
//                                    Object object = arry.get(i);
//                                    if (isValidObj(object + "")) {
//                                        addToList(object + "", serverTime);
//                                        etInput.setText("");
//                                    }
//                                }
//
//                            }
////                            refreshAdapterForLastSeen();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private boolean isValidObj(Object object) {
//        ChatObj obj = new Gson().fromJson(object + "", ChatObj.class);
//        boolean isValid = true;
//
//        try {
//            isValid = (obj.from.equals(currentUser.getCustomer_id()) && obj.to.equals(selectedUser.getCustomer_id()) ||
//                    (obj.to.equals(currentUser.getCustomer_id()) && obj.from.equals(selectedUser.getCustomer_id())));
//        } catch (Exception e) {
//            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " exception -  object");
//            e.printStackTrace();
//            isValid = false;
//        } finally {
//            return isValid;
//        }
//    }
//
//    private boolean isValidObj(ChatObj obj) {
//        boolean isValid = true;
//        try {
//            isValid = (obj.from.equals(currentUser.getCustomer_id()) && obj.to.equals(selectedUser.getCustomer_id()) ||
//                    (obj.to.equals(currentUser.getCustomer_id()) && obj.from.equals(selectedUser.getCustomer_id())));
//        } catch (Exception e) {
//            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " exception -  ChatObj");
//            e.printStackTrace();
//            isValid = false;
//        } finally {
//            return isValid;
//        }
//    }
//
//    private boolean isInternetAvailable() {
//        return NetworkUtils.isNetworkConnected(ChatUsingSocketActivity.this);
//    }
//
//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SocketUtils.getInstance().disconnect();
//    }
//
//    public void onClkPush(View view) {
//        sendOnetoOneMessage();
//    }
//
//    public void onClkSync(View view) {
//        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " calling Sync");
//        String str = etInput.getText().toString();
//        ChatObj chatObj = new ChatObj();
//        chatObj.from = FROM_ID;
//        chatObj.text = str;
//        refreshAdapter();
//        String createdAt = DEFAULT_SYNC_TIME;
//        ChatObj obj = null;
//        if ((vecChat != null ? vecChat.size() : 0) > 0) {
//            obj = vecChat.get(vecChat.size() - 1);
//            createdAt = obj.server_time;
//        }
//        callOneToOneChatSync(createdAt);
//    }
//
//    private void refreshAdapter() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (LOCK) {
//                    adapter.refresh(vecChat);
//                }
//            }
//        });
//    }
//
//    private void callOneToOneChatSync(String createdAt) {
//        toast("syncing...");
//        JSONObject jsonObject = new JSONObject();
//        try {
////            jsonObject.put("sent_at", CalendarUtils.getTimeMilliSeconds(createdAt,CalendarUtils.DATE_MAIN));
//            jsonObject.put("sent_at",  createdAt );
//            jsonObject.put("from", currentUser.getCustomer_id());
//            jsonObject.put("to", selectedUser.getCustomer_id());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " timestamp - last " + createdAt);
//
//        SocketUtils.getInstance().emit(SYNC_CHAT_HISTORY, jsonObject, new Ack() {
//            @Override
//            public void call(final Object... args) {
//                try {
//
//                    JSONArray array = new JSONArray(args[0] + "");
//                    Log.e(TAG, "Sync messages count " + array.length() + "-->" + args[0]);
//                    if (array != null && array.length() > 0) {
//                        for (int i = 0; i < array.length(); i++) {
//                            String str = array.get(i) + "";
//                            ChatObj obj = new Gson().fromJson(str, ChatObj.class);
//                            obj.text = obj.message;
//                            if (obj != null) {
//                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " sync --> " + str
//                                        + "received msg " + i + " " + obj.sent_at);
//                                if (isValidObj(obj))
//                                    vecChat.add(obj);
//                            }
//                        }
//                        database.addRecord(vecChat);
////                        refreshAdapterForLastSeen();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                tvOutput.setText(args[0] + "");
//                                try {
//                                    adapter.refresh(vecChat);
//                                    refreshAdapterForLastSeen();
////                                    rvChat.smoothScrollToPosition(vecChat.size()-1);
//                                    postUnseenMessages( );
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        clearInputText();
//
//    }
//
//    private void updateMessageSeenAt(final Vector<ChatObj> vecUpdateMsgStatus) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            JSONArray array = new JSONArray();
//            for (ChatObj obj : vecUpdateMsgStatus)
//                array.put(obj._id + "");
//            jsonObject.put("data", array);
//            jsonObject.put("to", TO_ID);
//            jsonObject.put("seen_at", CalendarUtils.getCurrentDateAndtime());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SocketUtils.getInstance().emit(SEEN_MESSAGES, jsonObject, new Ack() {
//            @Override
//            public void call(final Object... args) {
//                try {
//                    JSONObject json = new JSONObject(args[0] + "");
////                    setLastSyncTime(json.optString("server_time"));
//                    String seen_at = json.optString("seen_at");
//                    Object o = json.optJSONArray("data");
//                    JSONArray array = new JSONArray(o + "");
//                    Log.e(TAG, "updated Sent messages count " + array.length() + "-->" + args[0]);
//                    Vector<ChatObj> vecUpdated = new Vector<ChatObj>();
//                    if (array != null && array.length() > 0) {
//                        for (int i = 0; i < array.length(); i++) {
//                            String str = array.get(i) + "";
//                            ChatObj obj = new ChatObj();
//                            obj._id = str;
//                            obj.seen_at = seen_at;
//                            vecUpdated.add(obj);
//                        }
//                        database.addRecord(vecUpdated,  null );
////                        adapter.refresh(vecChat );
////                        setLastSyncTime(serverTime);
//                    }
//                    refreshAdapterForLastSeen();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
////        clearInputText();
//    }
//
//    private void changedMessagesStatus(String createdAt) {
//        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
//
//        SocketUtils.getInstance().on(SEEN_UPDATED_MESSAGES, new Emitter.Listener() {
//            String serverTime;
//
//            @Override
//            public void call(final Object... args) {
//                try {
//                    JSONObject json = new JSONObject(args[0] + "");
//                    String seet_at = json.optString("seen_at");
//                    serverTime= json.optString("updated_at");
////                    long  server_time= json.optLong("updated_at");
////                    serverTime = CalendarUtils.getData(server_time,CalendarUtils.DATE_MAIN);
//                    setLastSyncTime(serverTime); //serverTime
//                    Object o = json.optJSONArray("data");
//                    JSONArray array = new JSONArray(o + "");
//                    Log.e(TAG, "updated Sent messages count " + array.length() + "-->" + args[0]);
//                    Vector<ChatObj> vecUpdated = new Vector<ChatObj>();
//                    if (array != null && array.length() > 0) {
//                        for (int i = 0; i < array.length(); i++) {
//                            String str = array.get(i) + "";
//                            ChatObj obj = new ChatObj();
//                            obj._id = str;
//                            obj.seen_at = seet_at;
//                            vecUpdated.add(obj);
//                        }
//                        Log.e(TAG, "serverTime " + serverTime);
//                        database.addRecord(vecUpdated, null );
//                    }
//                    refreshAdapterForLastSeen();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        clearInputText();
//
//    }
//
//    private void refreshAdapterForLastSeen() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.updateSeenStatus(getLastSyncTime());
//            }
//        });
//    }
//
//    private void clearInputText() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                etInput.setText("");
//            }
//        });
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(receiver, new IntentFilter(SocketUtils.CONNECTION_UPDATES));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(receiver);
//    }
//
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null && intent.getBooleanExtra(IS_NETWORK_AVAILABLE, true)) {
//                ivIndicator.setBackgroundColor(Color.GREEN);
//            } else
//                ivIndicator.setBackgroundColor(Color.RED);
//            Log.e(TAG, "SocketId " + SocketUtils.getInstance().id());
//
//        }
//
//
//    };
//
//    public void saveInDatabase(final ChatObj obj) {
//        Log.e(TAG, new Exception().getStackTrace()[1].getMethodName() + " message is inserted");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                database.addRecord(obj);
//            }
//        }).start();
//    }
//
//    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
//        public WrapContentLinearLayoutManager(Context context) {
//            super(context);
//        }
//
//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//            try {
//                super.onLayoutChildren(recycler, state);
//            } catch (IndexOutOfBoundsException e) {
//                Log.e("LayoutManager", " mate a IOOBE in RecyclerView");
//            }
//        }
//    }
}
