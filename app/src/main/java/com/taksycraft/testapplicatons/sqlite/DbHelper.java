package com.taksycraft.testapplicatons.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.taksycraft.testapplicatons.retrofit.DBClient;
import com.taksycraft.testapplicatons.sockets.SyncDO;
import com.taksycraft.testapplicatons.sockets.UserDO;

import java.util.HashMap;
import java.util.Vector;


public class DbHelper extends SQLiteOpenHelper {
    private String TAG = DbHelper.class.getSimpleName();

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
        sqLiteDatabase.execSQL("create table tblMessages (id varachar, msgfrom  varchar, msgto varchar, " +
                "msg varchar, createdAt varchar, updatedAt varchar, status integer,servertime VARCHAR,seenAt VARCHAR )");
        sqLiteDatabase.execSQL("create table tblSyncTimes( userId varachar,  syncTime VARCHAR )");
        sqLiteDatabase.execSQL("create table tblUsers( userId varachar,  name VARCHAR )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " from  " + oldVersion + " to " + newVersion);
        if (newVersion > oldVersion) {
//            sqLiteDatabase.execSQL("ALTER TABLE tblMessages ADD COLUMN seen_at VARCHAR ");
        }
    }

    public void addRecord(ChatObj chatDO) {
        if(chatDO!=null)
        {
            Vector<ChatObj> vec = new Vector<ChatObj>();
            vec.add(chatDO);
            addRecord(vec );
        }

    }
    public void addSyncTime(SyncDO syncDO) {
        if(syncDO!=null)
        {
            Vector<SyncDO> vec = new Vector<SyncDO>();
            vec.add(syncDO);
            addSyncTime(vec );
        }

    }

    public void addSyncTime(Vector<SyncDO> vec) {
        synchronized (DBClient.LOCK) {
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        SyncDO obj = vec.get(i);
                        if (obj != null) {
                            ContentValues values = new ContentValues();
                            values.put("userId", obj.getId());
                            values.put("syncTime", obj.getSyncTime());

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblSyncTimes", values, "userId=?", new String[]{obj.getId()});
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblSyncTimes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public SyncDO getSyncDO(String userId) {
        synchronized (DBClient.LOCK) {
            SyncDO obj=null;
            String selectQuery= null;
            selectQuery = "SELECT  userId  , syncTime     FROM tblSyncTimes where userId = '"+userId+"'  "  ;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    int i = 0;
                    obj = new SyncDO();
                    obj.setId(cursor.getString(i++)+"") ;
                    obj.setSyncTime(cursor.getString(i++)+"");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;

        }
    }
    public void addUser(UserDO userDO) {
        if(userDO!=null)
        {
            Vector<UserDO> vec = new Vector<UserDO>();
            vec.add(userDO);
            addUser(vec );
        }

    }

    public void addUser(Vector<UserDO> vec) {
        synchronized (DBClient.LOCK) {
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        UserDO obj = vec.get(i);
                        if (obj != null) {
                            ContentValues values = new ContentValues();
                            values.put("userId", obj.getCustomer_id());
                            values.put("name", obj.getName());

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblUsers", values, "userId=?", new String[]{obj.getCustomer_id()});
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblUsers", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public Vector<UserDO> getFriendsList(String customer_id) {
        synchronized (DBClient.LOCK) {
            Vector<UserDO> vec = new Vector<UserDO>();
            String selectQuery= null;
//            tblUsers( userId varachar,  name VARCHAR )
            selectQuery = "SELECT userId,name   FROM tblUsers   order by  name"  ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int i = 0;
                        UserDO obj = new UserDO();
                        String id =  cursor.getString(i++) ;
                        if( ((!TextUtils.isEmpty(id)) && id.equals(customer_id+"")))
                        {

                        }else{
                            obj.setCustomer_id(id);
                            obj.setName(cursor.getString(i++)+"");
                            vec.add(obj);
                        }

                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vec;
        }
    }

    public void addRecord(Vector<ChatObj> vec) {
        synchronized (DBClient.LOCK) {
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        ChatObj chatDO = vec.get(i);
                        if (chatDO != null) {
                            ContentValues values = new ContentValues();
                            values.put("id", chatDO._id);
                            values.put("msgfrom", chatDO.from);
                            values.put("msgto", chatDO.to);
                            values.put("msg", chatDO.message);
                            values.put("createdAt", chatDO.sent_at);
                            values.put("status", chatDO.status);
                            values.put("servertime", chatDO.server_time);
                            values.put("seenAt", chatDO.seen_at);

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblMessages", values, "id=?", new String[]{chatDO._id});
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblMessages", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void addRecord(Vector<ChatObj> vec,String from,String to , UpdateListener listener ) {
        synchronized (DBClient.LOCK) {
            boolean isUpdated = false;
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        ChatObj chatDO = vec.get(i);
                        if (chatDO != null) {
                            ContentValues values = new ContentValues();
                            values.put("id", chatDO._id);
                            values.put("seenAt", chatDO.seen_at);

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblMessages", values, "id=?", new String[]{chatDO._id});
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblMessages", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                            else
                            {
                                isUpdated =true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(listener!=null)
                {
                    if(isUpdated  )
                        listener.retrievedData(getRecords(-1,from,to));
                    else
                        listener.retrievedData(null);
                }

            }
        }
    }
    public void addRecord(Vector<ChatObj> vec,UpdateListener listener ) {
        synchronized (DBClient.LOCK) {
            boolean isUpdated = false;
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        ChatObj chatDO = vec.get(i);
                        if (chatDO != null) {
                            ContentValues values = new ContentValues();
                            values.put("id", chatDO._id);
                            values.put("seenAt", chatDO.seen_at);

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblMessages", values, "id=?", new String[]{chatDO._id});
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblMessages", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                            else
                            {
                                isUpdated =true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(listener!=null)
                {
                    if(isUpdated  )
                        listener.retrievedData(getRecords(-1));
                    else
                        listener.retrievedData(null);
                }

            }
        }
    }
    public Vector<ChatObj> getRecords(String fromId, String toId) {
        return getRecords(-1,fromId,toId);
    }
    public Vector<ChatObj> getRecords(int status ,String fromId, String toId) {
        synchronized (DBClient.LOCK) {
            Vector<ChatObj> vec = new Vector<ChatObj>();
            String selectQuery= null;
            if (status==1 || status==0) {
                selectQuery = "SELECT  id  , msgfrom   , msgto  , msg  , createdAt  , updatedAt  , status ,servertime ,seenAt  " +
                        "  FROM tblMessages where status = "+status+
                        " and ((msgfrom='"+fromId+"' OR msgfrom='"+toId+"' ) and  (msgfrom='"+fromId+ "' OR msgfrom='"+toId+"' )) " +
                        " order by strftime('%Y-%m-%d %H:%M:%S',servertime) and strftime('%Y-%m-%d %H:%M:%S',createdAt)"  ;
            } if(status==-2){
                selectQuery = "SELECT  id  , msgfrom   , msgto  , msg  , createdAt  , updatedAt  , status ,servertime, seenAt " +
                        "   FROM tblMessages " +
                        " where ((msgfrom='"+fromId+"' OR msgfrom='"+toId+"' ) and  (msgfrom='"+fromId+ "' OR msgfrom='"+toId+"' )) " +
                        " order by strftime('%Y-%m-%d %H:%M:%S',servertime) and strftime('%Y-%m-%d %H:%M:%S',createdAt) "  ;
            } else {
                selectQuery = "SELECT  id  , msgfrom   , msgto  , msg  , createdAt  , updatedAt  , status ,servertime, seenAt " +
                        "   FROM tblMessages " +
                        " where ((msgfrom='"+fromId+"' OR msgfrom='"+toId+"' ) and  (msgfrom='"+fromId+ "' OR msgfrom='"+toId+"' )) " +
                        " order by strftime('%Y-%m-%d %H:%M:%S',servertime) and strftime('%Y-%m-%d %H:%M:%S',createdAt) "  ;
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            try {
                if (cursor.moveToFirst()) {
                    do {
                        int i = 0;
                        ChatObj obj = new ChatObj();
                        obj._id = cursor.getString(i++)+"";
                        obj.from= cursor.getString(i++)+"";
                        obj.to= cursor.getString(i++)+"";
                        obj.message= cursor.getString(i++)+"";
                        obj.sent_at= cursor.getString(i++)+"";
                        obj.updated_at= cursor.getString(i++)+"";
                        obj.status= cursor.getInt(i++);
                        obj.server_time= cursor.getString(i++);
                        obj.seen_at= cursor.getString(i++);
                        vec.add(obj);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vec;

        }
    }
    public Vector<ChatObj> getRecords() {
        return getRecords(-1);
    }
    public Vector<ChatObj> getRecords(int status ) {
        synchronized (DBClient.LOCK) {
            Vector<ChatObj> vec = new Vector<ChatObj>();
            String selectQuery= null;
            if (status==1 || status==0) {
                selectQuery = "SELECT  id  , msgfrom   , msgto  , msg  , createdAt  , updatedAt  , status ,servertime ,seenAt  " +
                        "  FROM tblMessages where status = "+status+" order by strftime('%Y-%m-%d %H:%M:%S',servertime) and strftime('%Y-%m-%d %H:%M:%S',createdAt)"  ;
            } else {
                selectQuery = "SELECT  id  , msgfrom   , msgto  , msg  , createdAt  , updatedAt  , status ,servertime, seenAt " +
                        "   FROM tblMessages order by strftime('%Y-%m-%d %H:%M:%S',servertime) and strftime('%Y-%m-%d %H:%M:%S',createdAt) "  ;
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int i = 0;
                        ChatObj obj = new ChatObj();
                        obj._id = cursor.getString(i++)+"";
                        obj.from= cursor.getString(i++)+"";
                        obj.to= cursor.getString(i++)+"";
                        obj.message= cursor.getString(i++)+"";
                        obj.sent_at= cursor.getString(i++)+"";
                        obj.updated_at= cursor.getString(i++)+"";
                        obj.status= cursor.getInt(i++);
                        obj.server_time= cursor.getString(i++);
                        obj.seen_at= cursor.getString(i++);
                        vec.add(obj);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vec;

        }
    }


    public void deleteAllRecords() {
        synchronized (DBClient.LOCK) {
            try {
                getWritableDatabase().execSQL("delete from tblMessages" ) ;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public void deleteAllRecords(String fromId, String toId ) {
        synchronized (DBClient.LOCK) {
            try {
                getWritableDatabase().execSQL("delete from tblMessages " +
                        " where ((msgfrom='"+fromId+ "' OR msgfrom='"+toId+"' ) and  (msgfrom='"+fromId+ "' OR msgfrom='"+toId+"' ))" ) ;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public void deleteAllRecordsBasedOnStatus(int  statusId ) {
        synchronized (DBClient.LOCK) {
            try {
                getWritableDatabase().execSQL("delete from tblMessages " +
                        " where   status="+statusId ) ;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
