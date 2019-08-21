package com.taksycraft.testapplicatons.retrofit;

import android.content.Context;

public class DBClient {
    public static final Object LOCK ="Lock" ;
    private   String TAG =DBClient.class.getSimpleName() ;
    private Context context;
    private  static DBClient mClient;

//    private static MyDatabase database;
//    public DBClient(Context context)
//    {
//        this.context=context;
////        if(database==null)
////            database = Room.databaseBuilder(context,MyRoomDatabase.class,"testingdb.db").build();
//        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+" cont");
//    }
//    public DBClient()
//    {
////        if(database==null)
////            database = Room.databaseBuilder(context,MyRoomDatabase.class,"testingdb.db").build();
//        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+" builder");
//    }
//
//    public MyDatabase getDatabase() {
//        return database;
//    }
//
//    public static synchronized DBClient getInstance( ) {
//        return mClient;
//    }
}
