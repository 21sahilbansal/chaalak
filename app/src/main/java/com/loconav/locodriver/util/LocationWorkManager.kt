package com.loconav.locodriver.util

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.driver.CurrentCoordinate
import com.loconav.locodriver.expense.ImageUtil
import com.loconav.locodriver.expenses.ExpenseRepo
import com.loconav.locodriver.expenses.ExpenseRepo.getunSyncedExpenseListFromDb
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class LocationWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {
    val db: AppDatabase by inject()
    override fun doWork(): Result {
        val location = LocationUtil().getLocation()
        insertCoordinateToDb(location)
        postUploadExpenseForm()
        return Result.success()
    }

    private fun insertCoordinateToDb(location: Location?) {
        val locationAvaibility = LocationUtil().isGPSEnabled()
        val phoneBatteryPercentage = PhoneUtil.getBatteryPercentage(applicationContext)
        location?.let {
            db.currentCoordinateDao().insertAll(CurrentCoordinate(it.latitude, it.longitude, phoneBatteryPercentage, locationAvaibility))
        }
    }

    private fun postUploadExpenseForm(){
        val unsyncedExpenseList = getunSyncedExpenseListFromDb()
        if(unsyncedExpenseList.isNullOrEmpty()) return
        for(item in unsyncedExpenseList){
            val expenseType = ExpenseRepo.setUpMultipartRequest(item.expenseType!!)
            val expenseAmount = ExpenseRepo.setUpMultipartRequest(item.amount!!.toString())
            val expenseDate = ExpenseRepo.setUpMultipartRequest(item.expenseDate!!.toString())
            val imageMultiPartList = ImageUtil.getMultipartFromUri(item.documents?.expenseDocList)
            ExpenseRepo.uploadToServer(expenseType,expenseAmount,expenseDate,imageMultiPartList,item)
        }
    }
}