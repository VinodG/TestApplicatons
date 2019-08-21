package com.taksycraft.testapplicatons.architecture;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {Record.class}, version = 1, exportSchema = false)
public abstract class MyRoomDatabase  extends RoomDatabase {
    private static  MyRoomDatabase db;

    public abstract RecordDAO getRecordDAO();
    public static MyRoomDatabase getInstance(Context appContext)
    {
        if(db ==null)
            db = Room.databaseBuilder(appContext,MyRoomDatabase.class,"mydatabase.db")
//                    .allowMainThreadQueries()
//                    .addCallback(new Callback() {
//                        @Override
//                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                            super.onCreate(db);
//                            Log.e("MyRoomDatabase", "database is created");
//                        }
//                    })
                    .build();

        return db;

    }
}
