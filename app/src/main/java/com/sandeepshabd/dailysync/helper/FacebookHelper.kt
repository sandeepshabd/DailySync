package com.sandeepshabd.dailysync.helper

import android.content.Context
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.sandeepshabd.dailysync.activities.ILoginView
import com.sandeepshabd.dailysync.activities.SummaryActivity
import com.sandeepshabd.dailysync.services.DailySyncService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startService

/**
 * Created by sandeepshabd on 1/3/18.
 */
class FacebookHelper(var context: Context) : AnkoLogger {

    var callBackmanager: CallbackManager? = null
    var loginActivityToFinish: ILoginView? = null

    fun registerActivity(loginActivity: ILoginView?) {
        loginActivityToFinish = loginActivity
    }


    fun registerFacebook(): CallbackManager? {
        if (callBackmanager == null) {
            callBackmanager = CallbackManager.Factory.create()
        }
        return callBackmanager
    }

    fun provideFacebookCallBack(): FacebookCallback<LoginResult> {
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
                context.startService<DailySyncService>()
                context.startActivity<SummaryActivity>()
                loginActivityToFinish?.finishLoginActivity()

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