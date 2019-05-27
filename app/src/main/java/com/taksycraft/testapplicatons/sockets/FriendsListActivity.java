package com.taksycraft.testapplicatons.sockets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CommonAdapter;
import com.taksycraft.testapplicatons.common.NetworkUtils;
import com.taksycraft.testapplicatons.sqlite.DbHelper;

import org.json.JSONArray;

import java.net.URISyntaxException;
import java.util.Vector;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.media.CamcorderProfile.get;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        database = new DbHelper(this, "chatdb.sqlite", null, 2);

        if(getIntent()!=null && getIntent().hasExtra("user"))
            userDO =(UserDO) getIntent().getExtras().get("user");
        try {
            setTitle("Friends of "+userDO.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        etInput =(EditText)findViewById(R.id.etInput);
        setList();
        setConnection();

    }
    private void setList() {
        rvUsers  =(RecyclerView) findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        adapter = new CommonAdapter(null, new CommonAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {
                UserDO obj = vec.get(position);
                holder.getTextView().setText(obj.getName());
            }
            @Override
            public void onItemClick(Object object, int position) {
                intent = new Intent(FriendsListActivity.this, ChatUsingSocketActivity.class);
                intent.putExtra("current_user",userDO);
                intent.putExtra("selected_user",(UserDO)object);
                etInput.setText(((UserDO)object).getName()+"");
                toast("item is clicked on position "+position );

            }
        });
        rvUsers.setAdapter(adapter);
        if(!isInternetAvailable()) {
            vec = database.getFriendsList(userDO.getCustomer_id());
            adapter.refresh(vec);
        }

    }
    private void setConnection() {
        SocketUtils.getInstance().emit(USER_LIST,

                new Ack() {
                    @Override
                    public void call(final Object... args) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
                                /*+args[0]*/);
                    }
                });
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
                    for(int i = 0 ;i<array.length();i++)
                    {
                        String str= array.get(i).toString();
                        UserDO obj = new Gson().fromJson(str, UserDO.class);
                        try {
                            if(!obj.getCustomer_id().equals(userDO.getCustomer_id()))
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

    public void onClkPush(View view)
    {
        if(intent!=null)
        {
//            finish();
            startActivity(intent);
        }else{
            toast("please select an user");
        }

    }
    private void toast(String str) {
        try {
            Toast.makeText(FriendsListActivity.this,str,Toast.LENGTH_SHORT ).show();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
    BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null && intent.getBooleanExtra(IS_NETWORK_AVAILABLE,true))
            {
//                ivIndicator.setBackgroundColor(Color.GREEN);
                SocketUtils.getInstance().emit(USER_LIST,

                        new Ack() {
                            @Override
                            public void call(final Object... args) {
                                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
                                        /*+args[0]*/);
                            }
                        });
            }
            else
//                ivIndicator.setBackgroundColor(Color.RED);
                Log.e(TAG, "SocketId "+SocketUtils.getInstance().id());

        }



    };
    private boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(FriendsListActivity.this);
    }

}
