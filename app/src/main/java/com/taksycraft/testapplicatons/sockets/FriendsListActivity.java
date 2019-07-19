package com.taksycraft.testapplicatons.sockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CommonAdapter;
import com.taksycraft.testapplicatons.common.NetworkUtils;
import com.taksycraft.testapplicatons.sqlite.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;

import static com.taksycraft.testapplicatons.sockets.ChatUsingSocketActivity.IS_NETWORK_AVAILABLE;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.LISTENER_USER_LIST;
import static com.taksycraft.testapplicatons.sockets.SocketUtils.USER_LIST;

public class FriendsListActivity extends AppCompatActivity {
    private static final String URL = "http://192.168.0.121:3000";
    private String TAG = FriendsListActivity.class.getSimpleName();

    private UserDO userDO;
    private EditText etInput;
    private RecyclerView rvUsers;
    //    private Socket socket;
    private Vector<UserDO> vec = new Vector<UserDO>();
    private CommonAdapter adapter;
    private Intent intent;
    private DbHelper database;
    private UserDO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        database = new DbHelper(this, "chatdb.sqlite", null, 3);

//        if(getIntent()!=null && getIntent().hasExtra("user"))
//            userDO =(UserDO) getIntent().getExtras().get("user");
        userDO = new UserDO();
        userDO.setName("Admin");
        userDO.setCustomer_id("ORB104IN");
        userDO.setRoom_no("0");
        currentUser = userDO;
        try {
            setTitle("Friends of " + userDO.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        etInput = (EditText) findViewById(R.id.etInput);
        setList();
        setConnection();
        doLogin();
    }

    private void doLogin() {
        Log.e(TAG, "trying to reLogin");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("customer_id", currentUser.getCustomer_id());
            jsonObject.putOpt("room_no", currentUser.getRoom_no()) ;
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
                                setConnection();
                            }
                        });
                    }
                });
    }

    private void setList() {
        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new WrapContentLinearLayoutManager(this));
        vec = database.getFriendsList("");
        adapter = new CommonAdapter(vec, new CommonAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {

                try {
                    UserDO obj = vec.get(position);
                    if(!TextUtils.isEmpty(obj.getRoom_no()))
                        holder.getTextView().setText(obj.getRoom_no());
                    else
                        holder.getTextView().setText(obj.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemClick(Object object, int position) {
                intent = new Intent(FriendsListActivity.this, ChatUsingSocketActivity.class);
                intent.putExtra("current_user", userDO);
                if(!TextUtils.isEmpty(((UserDO) object).getRoom_no()))
                    ((UserDO) object).setName(((UserDO) object).getRoom_no());
                else
                    ((UserDO) object).setName(((UserDO) object).getName());
//                ((UserDO) object).setName( ((UserDO) object).getRoom_no());
                ((UserDO) object).setName( vec.get(position).getName());
                intent.putExtra("selected_user", (UserDO) object);
                etInput.setText(((UserDO) object).getName() + "");
                toast("item is clicked on position " + position);

            }
        });
        rvUsers.setAdapter(adapter);
    }

    private void setConnection() {
//        SocketUtils.getInstance().emit(USER_LIST,
//
//                new Ack() {
//                    @Override
//                    public void call(final Object... args) {
//                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
//                                /*+args[0]*/);
//                    }
//                });
//        SocketUtils.getInstance().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " --connected");
//                SocketUtils.getInstance().emit("userList",
//
//                        new Ack() {
//                            @Override
//                            public void call(final Object... args) {
//                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
//                                        /*+args[0]*/);
//                            }
//                        });
//
//
//            }
//        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  disconnect");
//            }
//        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-error-  " + args[0]);
//            }
//        });
        SocketUtils.getInstance().on(LISTENER_USER_LIST, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " got list -->");
                try {
                    JSONArray array = new JSONArray(args[0] + "");
                    vec.clear();
                    for (int i = 0; i < array.length(); i++) {
                        String str = array.get(i).toString();
                        UserDO obj = new Gson().fromJson(str, UserDO.class);
                        obj.setName(obj.getRoom_no());
                        try {
                            if (!obj.getCustomer_id().equals(userDO.getCustomer_id()))
                                vec.add(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                            vec.add(obj);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.refresh(vec);
                            }
                        });
                    }
                    database.addUser(vec);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void onClkPush(View view) {
        if (intent != null) {
//            finish();
            startActivity(intent);
        } else {
            toast("please select an user");
        }

    }

    private void toast(String str) {
        try {
            Toast.makeText(FriendsListActivity.this, str, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(SocketUtils.CONNECTION_UPDATES));
        setConnection();
        doLogin();
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
                doLogin();
//                SocketUtils.getInstance().emit(USER_LIST,
//
//                        new Ack() {
//                            @Override
//                            public void call(final Object... args) {
//                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
//                                        /*+args[0]*/);
//                            }
//                        });
            } else
                Log.e(TAG, "SocketId " + SocketUtils.getInstance().id());

        }


    };

    private boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(FriendsListActivity.this);
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
//    private String TAG = FriendsListActivity.class.getSimpleName();
//
//    private UserDO userDO;
//    private EditText etInput;
//    private RecyclerView rvUsers;
//    //    private Socket socket;
//    private Vector<UserDO> vec = new Vector<UserDO>();
//    private CommonAdapter adapter;
//    private Intent intent;
//    private DbHelper database;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_database);
//        database = new DbHelper(this, "chatdb.sqlite", null, 2);
//
////        if(getIntent()!=null && getIntent().hasExtra("user"))
////            userDO =(UserDO) getIntent().getExtras().get("user");
//            userDO =new UserDO();
//            userDO.setName("Admin");
//            userDO.setCustomer_id("ORB104IN");
//        try {
//            setTitle("Friends of "+userDO.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        etInput =(EditText)findViewById(R.id.etInput);
//        setList();
//        setConnection();
//
//    }
//    private void setList() {
//        rvUsers  =(RecyclerView) findViewById(R.id.rvUsers);
//        rvUsers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
//        adapter = new CommonAdapter(null, new CommonAdapter.ListListener() {
//            @Override
//            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {
//                UserDO obj = vec.get(position);
//                holder.getTextView().setText(obj.getName());
//            }
//            @Override
//            public void onItemClick(Object object, int position) {
//                intent = new Intent(FriendsListActivity.this, ChatUsingSocketActivity.class);
//                intent.putExtra("current_user",userDO);
//                intent.putExtra("selected_user",(UserDO)object);
//                etInput.setText(((UserDO)object).getName()+"");
//                toast("item is clicked on position "+position );
//
//            }
//        });
//        rvUsers.setAdapter(adapter);
//        if(!isInternetAvailable()) {
//            vec = database.getFriendsList(userDO.getCustomer_id());
//            adapter.refresh(vec);
//        }
//
//    }
//    private void setConnection() {
//        SocketUtils.getInstance().emit(USER_LIST,
//
//                new Ack() {
//                    @Override
//                    public void call(final Object... args) {
//                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
//                                /*+args[0]*/);
//                    }
//                });
////        SocketUtils.getInstance().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
////            @Override
////            public void call(Object... args) {
////                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " --connected");
////                SocketUtils.getInstance().emit("userList",
////
////                        new Ack() {
////                            @Override
////                            public void call(final Object... args) {
////                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
////                                        /*+args[0]*/);
////                            }
////                        });
////
////
////            }
////        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
////            @Override
////            public void call(Object... args) {
////                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  disconnect");
////            }
////        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
////            @Override
////            public void call(Object... args) {
////                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-error-  " + args[0]);
////            }
////        });
//        SocketUtils.getInstance().on(LISTENER_USER_LIST, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " got list -->");
//                try {
//                    JSONArray array = new JSONArray(args[0] + "");
//                    vec.clear();
//                    for(int i = 0 ;i<array.length();i++)
//                    {
//                        String str= array.get(i).toString();
//                        UserDO obj = new Gson().fromJson(str, UserDO.class);
//                        try {
//                            if(!obj.getCustomer_id().equals(userDO.getCustomer_id()))
//                                vec.add(obj);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            vec.add(obj);
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.refresh(vec);
//                            }
//                        });
//                    }
//                    database.addUser(vec);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//    }
//
//    public void onClkPush(View view)
//    {
//        if(intent!=null)
//        {
////            finish();
//            startActivity(intent);
//        }else{
//            toast("please select an user");
//        }
//
//    }
//    private void toast(String str) {
//        try {
//            Toast.makeText(FriendsListActivity.this,str,Toast.LENGTH_SHORT ).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
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
//    BroadcastReceiver receiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent!=null && intent.getBooleanExtra(IS_NETWORK_AVAILABLE,true))
//            {
//                SocketUtils.getInstance().emit(USER_LIST,
//
//                        new Ack() {
//                            @Override
//                            public void call(final Object... args) {
//                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
//                                        /*+args[0]*/);
//                            }
//                        });
//            }
//            else
//                Log.e(TAG, "SocketId "+SocketUtils.getInstance().id());
//
//        }
//
//
//
//    };
//    private boolean isInternetAvailable() {
//        return NetworkUtils.isNetworkConnected(FriendsListActivity.this);
//    }

}
