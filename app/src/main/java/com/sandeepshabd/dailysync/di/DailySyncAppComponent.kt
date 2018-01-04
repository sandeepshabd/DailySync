package com.sandeepshabd.dailysync.di

import com.sandeepshabd.dailysync.activities.LoginActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by sandeepshabd on 1/3/18.
 */

@Singleton
@Component(modules = [
        LoginModule::class,
        DailySyncApplicationModule::class
])
interface DailySyncAppComponent{
        fun inject(target: LoginActivity)
}