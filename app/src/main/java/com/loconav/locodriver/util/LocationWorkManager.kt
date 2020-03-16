package com.loconav.locodriver.util

import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.driver.CurrentCoordinate
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class LocationWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {
    val db: AppDatabase by inject()
    override fun doWork(): Result {
        val location = LocationUtil().getLocation()
        insertCoordinateToDb(location)
        return Result.success()
    }

    private fun insertCoordinateToDb(location: Location?) {
        val locationAvaibility = LocationUtil().isGPSEnabled()
        val phoneBatteryPercentage = PhoneUtil.getBatteryPercentage(applicationContext)
        location?.let {
            db.currentCoordinateDao().insertAll(CurrentCoordinate(it.latitude, it.longitude, phoneBatteryPercentage, locationAvaibility))
        }
    }
}