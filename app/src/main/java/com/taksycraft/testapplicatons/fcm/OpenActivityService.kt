package com.taksycraft.testapplicatons.fcm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.taksycraft.testapplicatons.firebasedatabase.RegistationActivity
import kotlinx.coroutines.*

class OpenActivityService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        println("OpenActivityService - onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        println("OpenActivityService - onCreate")
        startActivity(Intent(this@OpenActivityService, RegistationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
//        GlobalScope.launch(Dispatchers.IO) {
//            for (i in 1..10){
//                delay(500)
//                println("oncreate ${i}")
//            }
//            runBlocking {
//                startActivity(Intent(this@OpenActivityService, RegistationActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                })
//            }
//
//        }


    }
}