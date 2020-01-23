package com.loconav.locodriver.util

import android.content.Context
import android.location.LocationManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class LocationUtil : KoinComponent {

    val context : Context by inject()

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun isGPSEnabled() : Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}