package com.taksycraft.testapplicatons.retrofit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Vector;

@Dao
public interface RecordDAO {
    @Insert
    void insert(Vector<RecordDO> recordDO);

    @Update
    void update(RecordDO recordDO);

    @Delete
    void insert(RecordDO recordDO);

    @Query("SELECT * FROM recorddo")
    Vector<RecordDO> getAll();


}
