package com.sandeepshabd.dailysync.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.IBinder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.error
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.LocationManager
import java.util.*
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.os.Bundle


class DailySyncService : Service(), AnkoLogger, SensorEventListener, LocationListener {

    var mGravity = FloatArray(3)

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.getType() === Sensor.TYPE_ACCELEROMETER) {
            // gravityFlag= true;
            for (i in 0..2) {
                mGravity[i] = event?.values[i]
                senddataToBO("accelerometer_x:"+mGravity[0])
                senddataToBO("accelerometer_y:"+mGravity[1])
                senddataToBO("accelerometer_z:"+mGravity[2])
            }
        }
    }

    private fun senddataToBO(s: String) {
        info(s)
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        info("DailySyncService started.")
        try {
            val mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            val accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(getProviderName(locationManager), 0,
                    0f, this)
        }catch (e:Exception){
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

    override fun onLocationChanged(location: Location?) {
        senddataToBO("speed:"+location?.speed?.times(2.23694))
        senddataToBO("latitude:"+location?.latitude)
        senddataToBO("longitude:"+location?.longitude)
        senddataToBO("altitude:"+location?.altitude)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}
