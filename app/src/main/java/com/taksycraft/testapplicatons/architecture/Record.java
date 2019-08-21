package com.taksycraft.testapplicatons.architecture;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Records", indices = {@Index(value = {"name" },
        unique = true)})
public class Record {
    @PrimaryKey
    @NonNull
    public  String name ="";
    public  String occupation="";
    public  String amount="0.0";

}
