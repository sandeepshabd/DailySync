package com.sandeepshabd.dailysync

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

/**
 * Created by sandeepshabd on 1/2/18.
 */
class DailySyncApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)
    }


}