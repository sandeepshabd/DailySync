package com.sandeepshabd.dailysync.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Criteria
import android.location.LocationManager
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
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
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
    var mSensorManager: SensorManager? = null
    var accelerometer: Sensor? = null

    var credentialsProvider: CognitoCachingCredentialsProvider? = null
    val mainCalendar = GregorianCalendar()

    var iotObserable: Observable<Float>? = null
    var emitter: ObservableEmitter<Float>? = null


    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        COGNITO_POOL_ID = resources.getString(R.string.cognito_pool_id)
        CUSTOMER_SPECIFIC_ENDPOINT = resources.getString(R.string.customer_specific_endpoint)
        THING_NAME = resources.getString(R.string.thing_name)
        debug("onCreate")
        info("DailySyncService started.connectToAWS")
        connectToAWS()
        info("DailySyncService started.createObservables")
        createObservables()
        info("DailySyncService started.registerAccelSensors")
        registerAccelSensors()
        info("DailySyncService started.runTaskToSendData")
        runTaskToSendData()


        var current_hour = mainCalendar.get(Calendar.HOUR_OF_DAY)
        var current_minute = mainCalendar.get(Calendar.MINUTE)
        info("collection time started at:HR:" + current_hour)
        info("collection time started at:minues:" + current_minute)
    }


    fun connectToAWS() {
        debug("connectToAWS")
        try {
            credentialsProvider = CognitoCachingCredentialsProvider(
                    applicationContext,
                    COGNITO_POOL_ID, // Identity Pool ID
                    MY_REGION // Region
            )

            iotDataClient = AWSIotDataClient(credentialsProvider)
            iotDataClient?.endpoint = CUSTOMER_SPECIFIC_ENDPOINT
            info("iotDataClient:" + iotDataClient)
        } catch (e: Exception) {
            error { e }
        }
    }


    fun createObservables() {
        debug("createObservables")
        iotObserable = Observable.create({ observableEmitter -> emitter = observableEmitter })
    }


    fun registerAccelSensors() {
        debug("registerAccelSensors")
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        debug("mSensorManager:" + mSensorManager != null)
        accelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        debug("accelerometer:" + accelerometer != null)
        mSensorManager?.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                debug("sensor data received")
                if (event?.sensor?.getType() == Sensor.TYPE_ACCELEROMETER) {
                    info("accelearation value:" + event.values[0])
                    emitter?.onNext(event.values[0])
                }
            }
        }
                , accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun runTaskToSendData() {
        debug("runTaskToSendData called.")
        iotObserable?.
                filter { data -> (data < -4 || data > 4) }?. //send data to AWS for acceleration greater than 4.
                observeOn(Schedulers.newThread())?.
                flatMap { data ->
                    Observable.just(runInBackGroundThread(data))
                }?.
                flatMap { data ->
                    Observable.fromCallable({
                        debug("callable called.")
                        runInBackGroundThread(data)
                    })
                }?.
                observeOn(AndroidSchedulers.mainThread())?.
                subscribe()
    }


    private fun runInBackGroundThread(accelerationData: Float): Boolean {
        //TODO - speed implementation.
        debug("runInBackGroundThread")
        var speedControl = SpeedControl(State(Reported(0f, true, accelerationData), null))
        return pushDataToBO(speedControl)
    }

    private fun pushDataToBO(speedControl: SpeedControl): Boolean {
        try {
            debug("pushDataToBO")

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
            Log.e(DailySyncService::class.java.simpleName, "error while updating data", e)

        }
        return true
    }


    //for location manager
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
