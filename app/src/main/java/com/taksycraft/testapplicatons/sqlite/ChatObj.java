package com.taksycraft.testapplicatons.sqlite;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class ChatObj implements Serializable {
    public String from = "";
    public String text = "";
    @SerializedName("message_id")
    public String _id = "";
    public String romm = "";
    public String created_at = "";
    public String server_time = ""; //server time
    public String updated_at = "";
    public String sent_at = ""; //created msg time (in table createdAt)
    public String seen_at = ""; //created msg time (in table createdAt)

    public String to = "";
    public String message = "";

    public int status= 1;
    public   static int POSTED =   1;
    public  static int  NOT_POSTED= 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatObj chatObj = (ChatObj) o;
        try {
            return status == chatObj.status &&
                    from.equals(chatObj.from) &&
                    text.equals(chatObj.text) &&
                    _id.equals(chatObj._id) &&
                    romm.equals(chatObj.romm) &&
                    created_at.equals(chatObj.created_at) &&
                    server_time.equals(chatObj.server_time) &&
                    updated_at.equals(chatObj.updated_at) &&
                    sent_at.equals(chatObj.sent_at) &&
                    seen_at.equals(chatObj.seen_at) &&
                    to.equals(chatObj.to) &&
                    message.equals(chatObj.message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, text, _id, romm, created_at, server_time, updated_at, sent_at, seen_at, to, message, status);
    }
}
