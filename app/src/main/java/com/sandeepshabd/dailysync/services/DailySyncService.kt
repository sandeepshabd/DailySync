package com.sandeepshabd.dailysync.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.location.Criteria
import android.location.LocationManager
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.iotdata.AWSIotDataClient
import com.amazonaws.services.iotdata.model.UpdateThingShadowRequest
import com.google.gson.Gson
import com.sandeepshabd.dailysync.R
import com.sandeepshabd.dailysync.models.Reported
import com.sandeepshabd.dailysync.models.SpeedControl
import com.sandeepshabd.dailysync.models.State
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.nio.ByteBuffer
import java.util.*


class DailySyncService : Service(), AnkoLogger {


    var COGNITO_POOL_ID = ""
    var CUSTOMER_SPECIFIC_ENDPOINT = ""
    var THING_NAME = ""

    var iotDataClient: AWSIotDataClient? = null
    var MY_REGION = Regions.US_EAST_2

    var mGravity = FloatArray(3)
    var credentialsProvider: CognitoCachingCredentialsProvider? = null


    fun connectToAWS() {
        try {
            credentialsProvider = CognitoCachingCredentialsProvider(
                    applicationContext,
                    COGNITO_POOL_ID, // Identity Pool ID
                    MY_REGION // Region
            )
            info("credentialsProvider:" + credentialsProvider)

            iotDataClient = AWSIotDataClient(credentialsProvider)
            iotDataClient?.endpoint = CUSTOMER_SPECIFIC_ENDPOINT
            info("iotDataClient:" + iotDataClient)
            var upDateTask = UpdateShadowTask()
            upDateTask.execute()
        } catch (e: Exception) {
            error { e }
        }
    }

    private inner class UpdateShadowTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean {
            try {
                var speedControl = SpeedControl(State(Reported(4,true), null))
                var updateState: String? = Gson().toJson(speedControl)
                val request = UpdateThingShadowRequest()
                info("request:" + request)
                info("updateState:" + updateState)
                request.thingName = THING_NAME

                val payloadBuffer = ByteBuffer.wrap(updateState!!.toByteArray())
                request.payload = payloadBuffer

                var updateResult = iotDataClient?.updateThingShadow(request)
                info("updateResult:" + updateResult)

            } catch (e: Exception) {
                Log.e(UpdateShadowTask::class.java.canonicalName, "updateShadowTask", e)

            }

            return true
        }

    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        COGNITO_POOL_ID = resources.getString(R.string.cognito_pool_id)
        CUSTOMER_SPECIFIC_ENDPOINT = resources.getString(R.string.customer_specific_endpoint)
        THING_NAME = resources.getString(R.string.thing_name)
        info("DailySyncService started.")
        try {
            connectToAWS()
        } catch (e: Exception) {
            error(e)
        }

        val mainCalendar = GregorianCalendar()


        var current_hour = mainCalendar.get(Calendar.HOUR_OF_DAY)
        var current_minute = mainCalendar.get(Calendar.MINUTE)
        info("collection time started at:HR:" + current_hour)
        info("collection time started at:minues:" + current_minute)
    }

    fun getProviderName(locationManager: LocationManager): String {

        val criteria = Criteria()
        criteria.powerRequirement = Criteria.POWER_LOW // Chose your desired power consumption level.
        criteria.accuracy = Criteria.ACCURACY_FINE // Choose your accuracy requirement.
        criteria.isSpeedRequired = true // Chose if speed for first location fix is required.
        criteria.isAltitudeRequired = true // Choose if you use altitude.
        criteria.isBearingRequired = true // Choose if you use bearing.

        return locationManager.getBestProvider(criteria, true)
    }

    @Throws(UnsupportedOperationException::class)
    override fun onBind(intent: Intent): IBinder {
        throw  UnsupportedOperationException("Not yet implemented")
    }


}
