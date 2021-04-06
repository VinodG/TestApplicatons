package com.taksycraft.testapplicatons.alarm

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.taksycraft.testapplicatons.backgroundtask.NotificationChrono


class AlarmService : Service() {

    private lateinit var alarmNotificationManager: NotificationManager
    private val TAG = "AlarmService"
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ")
        sendNotification("alarm")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.e(TAG, "onBind: ")
        return null
    }



    private fun sendNotification(msg: String) {
        NotificationChrono.updateNotification(this,
                false,
                1,   "1  seconds",
                "Title",
                "text " ,
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager))
        giveRing()

    }

    private fun giveRing() {
        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(this, alarmUri)
        ringtone.play()
        Handler().postDelayed({ ringtone.stop() }, 6000)

    }
}