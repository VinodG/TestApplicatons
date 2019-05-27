package com.taksycraft.testapplicatons.retrofit;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecordDO.class}, version = 1, exportSchema = false)
public abstract  class MyDatabase extends RoomDatabase {
    public abstract RecordDAO getRecordDAO();
}
