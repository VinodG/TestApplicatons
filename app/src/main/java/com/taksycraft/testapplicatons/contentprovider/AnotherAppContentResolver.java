package com.taksycraft.testapplicatons.contentprovider;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

public class AnotherAppContentResolver extends AppCompatActivity {
    //below constant are taken from content provider class
     String PROVIDER_NAME = "com.vinod.contentproviderdemo.MyContentProvider";
     String URL = "content://" + PROVIDER_NAME + "/students";
     Uri CONTENT_URI = Uri.parse(URL);
     String _ID = "_id";
     String NAME = "name";
     String GRADE = "grade";

    private EditText etInput;
    private TextView tvCurrentUserName;
    private MyObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_app_content_resolver);
        initControls();
        observer = new MyObserver(null);
        setDataChangeListener();
    }

    private void setDataChangeListener() {
        getContentResolver().registerContentObserver(CONTENT_URI,true, observer  );
    }

    private void initControls() {
        etInput =  ((EditText)findViewById(R.id.etInput));
        tvCurrentUserName=  ((TextView)findViewById(R.id.tvCurrentUserName));
        tvCurrentUserName.setMovementMethod(new ScrollingMovementMethod());
    }

    public void onClkSend(View view) {
        ContentValues values = new ContentValues();
        values.put( NAME, etInput.getText().toString());
        values.put( GRADE, etInput.getText().toString()+"_GRADE");
        Uri uri = getContentResolver().insert( CONTENT_URI, values);
        toast(uri.toString());
    }
    public void onClkRead(View view) {
        // Retrieve student records
        Cursor c =  getContentResolver().query (CONTENT_URI, null, null, null, "name");
        updateUI(c);

    }

    private void updateUI(Cursor c) {
        try {
            final StringBuilder str =new StringBuilder();
            if (c.moveToFirst()) {
                do{
                    str.append( c.getString(c.getColumnIndex( _ID)) +
                            ", " +  c.getString(c.getColumnIndex(  NAME)) +
                            ", " + c.getString(c.getColumnIndex(  GRADE))+"\n");
                } while (c.moveToNext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCurrentUserName.setText(str);
                    }
                });


            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            c.close();
        }
    }

    private void toast(String str) {
        Toast.makeText(getBaseContext(),str+"", Toast.LENGTH_LONG).show();
    }
    class MyObserver extends ContentObserver {
        private String TAG = MyObserver.class.getSimpleName();

        public MyObserver(Handler handler) {
            super(handler);
            Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
            Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+"-"+1);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+"-URI" );
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            updateUI(cursor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().
                unregisterContentObserver(observer);
    }
}
