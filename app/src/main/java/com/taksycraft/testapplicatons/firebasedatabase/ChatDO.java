package com.taksycraft.testapplicatons.firebasedatabase;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public  class ChatDO implements Serializable {
    public String key = ""; //key to update an object
    public String msg = ""; //message to be delivered
    public String imageUrl = "";
    public String userName = "";//from
    public String friendName = ""; // to
    public String timeStamp = "";
    public Object serverTimeStamp = ServerValue.TIMESTAMP;
    public boolean isMsgRead= false;
}
