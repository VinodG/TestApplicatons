package com.taksycraft.testapplicatons.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taksycraft.testapplicatons.R
import java.util.*


//https://javapapers.com/android/android-alarm-clock-tutorial/
class AlarmActivity : AppCompatActivity() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        createAlarm(10)
    }

    private fun createAlarm(duration: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, duration)
        alarmManager =   getSystemService(ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(this@AlarmActivity, AlarmWakeUpReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this@AlarmActivity, (System.currentTimeMillis()%10000).toInt(), myIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= 23) {
            // https://stackoverflow.com/questions/34378707/alarm-manager-does-not-work-in-background-on-android-6-0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  pendingIntent)
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
            }
        }
    }
}