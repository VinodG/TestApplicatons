package com.taksycraft.testapplicatons.sockets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity {
    private static final String URL = "http://192.168.0.121:3000";
    private String TAG = LoginActivity.class.getSimpleName();
    private EditText etInput;
    PreferenceUtils preferenceUtils =null;
    private String PREF_USER = "USER";
//    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getApplicationContext());
        String strObj = preferenceUtils.getStringFromPreference(PREF_USER, "");
        if(!TextUtils.isEmpty(strObj)){
            openFriendsList(strObj);
        }
        else{
            setContentView(R.layout.activity_registation);
            setTitle("Registration");
            etInput=(EditText)findViewById(R.id.etInput);
        }


    }
    public void onClkRegister(View view)
    {
        String name = etInput.getText().toString();
        if(!TextUtils.isEmpty(name))
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.putOpt("name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SocketUtils.getInstance().emit(SocketUtils.REGISTRATION,
                    jsonObject,
                    new Ack() {
                        @Override
                        public void call(final Object... args) {
                            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " registration  has done -->"
                                    /*+args[0]*/);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UserDO user = new Gson().fromJson(args[0] + "", UserDO.class);
                                    toast("Registration is done successfully");
                                    String strUser = new Gson().toJson(user);
                                    preferenceUtils.saveString(PREF_USER,strUser );
                                    openFriendsList(strUser );

                                }
                            });

                        }


                    });
        }

    }

    private void openFriendsList(String strUser) {
        Intent intent = new Intent(LoginActivity.this, FriendsListActivity.class);
        intent.putExtra("user", (UserDO)new Gson().fromJson(strUser,UserDO.class));
        startActivity(intent);
        finish();
    }

    private void toast(String str) {
        try {
            Toast.makeText(LoginActivity.this,str,Toast.LENGTH_SHORT ).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
