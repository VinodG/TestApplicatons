package com.taksycraft.testapplicatons.sockets;

import java.io.Serializable;

public class UserDO extends  Object implements Serializable {
    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private   String  customer_id="";
    private   String  name="";
    private   String  socket_id="";
    private   String  created_at="";
}
