package com.sandeepshabd.dailysync

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.sandeepshabd.dailysync.di.DaggerDailySyncAppComponent
import com.sandeepshabd.dailysync.di.DailySyncAppComponent
import com.sandeepshabd.dailysync.di.DailySyncApplicationModule


/**
 * Created by sandeepshabd on 1/2/18.
 */
class DailySyncApplication:Application() {

    lateinit var dailySyncAppComponent: DailySyncAppComponent


    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)
        dailySyncAppComponent = initDagger(this)

    }

    private fun initDagger(app: DailySyncApplication): DailySyncAppComponent =
            DaggerDailySyncAppComponent.builder()
                    .dailySyncApplicationModule(DailySyncApplicationModule(app))
                    .build()


}