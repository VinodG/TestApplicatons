package com.taksycraft.testapplicatons.architecture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(Record... records);

    @Query("select * from Records order by name, amount")
    LiveData<List<Record>> getRecord();

}
