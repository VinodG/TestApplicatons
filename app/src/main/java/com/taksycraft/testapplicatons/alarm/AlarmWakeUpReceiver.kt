package com.taksycraft.testapplicatons.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.taksycraft.testapplicatons.backgroundtask.NotificationChrono


class AlarmWakeUpReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(con: Context, intent: Intent) {
        con.startForegroundService(  Intent(con, AlarmService::class.java))
        var x = con.getSystemService (Context.ALARM_SERVICE) as AlarmManager
        scheduleExactAlarm (con,x , 5000)
        sendNotification(con,"message")
    }

    private fun scheduleExactAlarm(context: Context, alarmManager: AlarmManager, period: Int) {
        val i = Intent(context, AlarmWakeUpReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + period, pi)
        }
    }
    private fun sendNotification(con: Context, msg: String) {
        NotificationChrono.updateNotification(con,
                false,
                1,   "1  seconds",
                "Title",
                "text " ,
                (con.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager))
    }
}