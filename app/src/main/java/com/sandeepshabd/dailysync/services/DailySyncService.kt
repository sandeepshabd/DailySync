package com.sandeepshabd.dailysync.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class DailySyncService : Service(),AnkoLogger {

    override fun onCreate() {
        super.onCreate()
        info("DailySyncService started.")
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
