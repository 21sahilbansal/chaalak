package com.loconav.locodriver.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.security.Permission
import java.security.Permissions

class LocationUtil : KoinComponent {

    val context: Context by inject()

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocation(): Location? {
        var location: Location? = null
        return if (isGPSEnabled()) {
            Log.i("Location Manager State", "GPS ENABLE")
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location
        } else {
            Log.i("Location Manager State", "GPS DISABLE")
            null
        }
    }
}