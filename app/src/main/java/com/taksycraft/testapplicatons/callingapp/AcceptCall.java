package com.taksycraft.testapplicatons.callingapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class AcceptCall extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout answerButton;
    private LinearLayout rejectButton;
    private LinearLayout timerLayout;
    private TextView contactName;
    private TextView contactNumber;
    private TextView callType;
    private TextView timerValue;
    private ImageView profile;
    private String incomingnumber;
    private int state;
    private String contactId;
    private String name;
    private InputStream photo_stream;
    private long startTime;
    private long timeInMilliseconds;
    private long updatedTime;
    private long timeSwapBuff;
    private Handler customHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_call);
        customHandler =new Handler();
        answerButton = (LinearLayout) findViewById(R.id.callReceive);
        answerButton.setOnClickListener(this);

        rejectButton =  (LinearLayout) findViewById(R.id.callReject);
        rejectButton.setOnClickListener(this);

        timerLayout = (LinearLayout) findViewById(R.id.timerLayout);

        contactName = (TextView) findViewById(R.id.contactName);
        contactNumber  = (TextView) findViewById(R.id.contactNumber);

        callType  = (TextView) findViewById(R.id.callType);

        timerValue  = (TextView) findViewById(R.id.timerValue);

        profile   = (ImageView)findViewById(R.id.contactPhoto);

        Bundle bundle =  getIntent().getExtras();
        if(bundle != null)
        {
            incomingnumber  = bundle.getString("incomingnumber");
            state  = bundle.getInt("state");
        }

        contactslookup(incomingnumber);

//        contactName.setText(name);
        contactNumber.setText(incomingnumber);


        if (state == 2)
        {
        /*String uri = "tel:" + incomingnumber.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);*/
        }

        PhoneStateListener phoneStateListener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String incomingNumber)
            {
                //wen ringing
                if (state == TelephonyManager.CALL_STATE_RINGING)
                {
                    Log.e("CALL_STATE_RINGING","CALL_STATE_RINGING");
                }

                //after call cut
                else if(state == TelephonyManager.CALL_STATE_IDLE)
                {
                    RejectCall();
                }

                //wen speaking / outgoing call
                else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
                {
                    Log.e("CALL_STATE_OFFHOOK","CALL_STATE_OFFHOOK");
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null)
        {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


    private void contactslookup(String number)
    {

        Log.v("ffnet", "Started uploadcontactphoto...");

        //InputStream input = null;

        // define the columns I want the query to return
        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME,ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

        if (cursor.moveToFirst())
        {
            // Get values from contacts database:
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            name  =      cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        else
        {
            return; // contact not found
        }


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            Uri my_contact_Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri, true);
        }
        else
        {
            Uri my_contact_Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri);
        }

        if(photo_stream != null)
        {
            BufferedInputStream buf =new BufferedInputStream(photo_stream);
            Bitmap my_btmp = BitmapFactory.decodeStream(buf);
            profile.setImageBitmap(my_btmp);
        }
        else
        {
//            profile.setImageResource(R.drawable.contactpic);
        }

        cursor.close();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        if(v.getId() == answerButton.getId())
        {
            timerLayout.setVisibility(View.VISIBLE);

            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

            callType.clearAnimation();

            // Simulate a press of the headset button to pick up the call
            Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
            buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
            this.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");

            // froyo and beyond trigger on buttonUp instead of buttonDown
            Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
            buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
            this.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
        }

        if(v.getId() == rejectButton.getId())
        {
            RejectCall();
        }

    }

    private void RejectCall()
    {
        TelephonyManager telephony = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            // Java reflection to gain access to TelephonyManager's
            // ITelephony getter
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            Object telephonyService =  m.invoke(telephony);
            Method endCall = telephonyService.getClass().getDeclaredMethod("endCall");
            endCall.invoke(telephonyService);
            finish();

            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error", "FATAL ERROR: could not connect to telephony subsystem");
            Log.e("Error", "Exception object: " + e);
        }
    }

    private Runnable updateTimerThread = new Runnable()
    {

        public void run()
        {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime  =  timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText(""+ hours + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

}