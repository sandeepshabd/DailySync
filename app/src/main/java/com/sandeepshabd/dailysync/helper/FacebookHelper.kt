package com.sandeepshabd.dailysync.helper

import android.content.Context

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.sandeepshabd.dailysync.activities.SummaryActivity

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

/**
 * Created by sandeepshabd on 1/3/18.
 */
class FacebookHelper(var context: Context):AnkoLogger {

    var callBackmanager:CallbackManager? = null

    fun registerFacebook():CallbackManager?{
        if(callBackmanager == null){
            callBackmanager = CallbackManager.Factory.create()
        }
        return callBackmanager
    }

    fun provideFacebookCallBack(): FacebookCallback<LoginResult>{
        return object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
            info("success from facebook")
                info(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                )

                context.startActivity<SummaryActivity>()

           }

            override fun onCancel() {
                // App code
                info("on cancel fcebook")
            }

            override fun onError(exception: FacebookException) {
                // App code
                info("on error facebook")
            }
        }
    }
}