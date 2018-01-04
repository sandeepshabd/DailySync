package com.sandeepshabd.dailysync.di

import android.content.Context
import com.sandeepshabd.dailysync.helper.FacebookHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by sandeepshabd on 1/3/18.
 */
@Module
class LoginModule {

    @Singleton
    @Provides
     fun provideFacebookHelper( context: Context): FacebookHelper{
        return FacebookHelper(context)
    }

}