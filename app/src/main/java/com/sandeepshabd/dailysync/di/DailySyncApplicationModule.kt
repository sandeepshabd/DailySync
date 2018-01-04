package com.sandeepshabd.dailysync.di

import android.app.Application
import android.content.Context
import com.sandeepshabd.dailysync.DailySyncApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by sandeepshabd on 1/3/18.
 */
@Module
class DailySyncApplicationModule(private val app:DailySyncApplication){

    @Provides
    @Singleton
    fun provideApplication() : Application = app

    @Provides
    @Singleton
    fun provideContext() : Context = app

}