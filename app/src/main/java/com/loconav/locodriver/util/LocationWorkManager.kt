package com.loconav.locodriver.util

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.UPLOADABLE_ATTRIBUTES_KEY
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.driver.CurrentCoordinate
import com.loconav.locodriver.expense.ImageUtil
import com.loconav.locodriver.expense.model.ExpenseType
import com.loconav.locodriver.expenses.ExpenseRepo
import com.loconav.locodriver.expenses.ExpenseRepo.getunSyncedExpenseListFromDb
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class LocationWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {
    val db: AppDatabase by inject()
    val sharedPreferenceUtil :SharedPreferenceUtil by inject()
    val gson:Gson by inject()
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
            db.currentCoordinateDao().insertAll(
                CurrentCoordinate(
                    it.latitude,
                    it.longitude,
                    phoneBatteryPercentage,
                    locationAvaibility
                )
            )
        }
    }

    private fun postUploadExpenseForm() {
        val unsyncedExpenseList = getunSyncedExpenseListFromDb()
        val json = sharedPreferenceUtil.getData(Constants.ExpenseConstants.EXPENSE_TYPE, "")
        val list = gson.fromJson(json, ExpenseType::class.java)
        var fakeExpenseType :String?=null
        if (unsyncedExpenseList.isNullOrEmpty()) return
        for (item in unsyncedExpenseList) {
            list?.let {
                if (!it.expenseType.isNullOrEmpty()) {
                    for (itemExpenseTypeList in it.expenseType) {
                        if(itemExpenseTypeList.value == item.expenseType){
                            fakeExpenseType = itemExpenseTypeList.key
                        }
                    }
                }
            }

            if(fakeExpenseType == null) break
            val expenseType = ExpenseRepo.setUpMultipartRequest(fakeExpenseType!!)
            val expenseAmount = ExpenseRepo.setUpMultipartRequest(item.amount!!.toString())
            val expenseDate = ExpenseRepo.setUpMultipartRequest(item.expenseDate!!.toString())
            val imageMultiPartList = ImageUtil.getMultipartFromUri(
                UPLOADABLE_ATTRIBUTES_KEY,
                item.documents?.expenseDocList
            )
            ExpenseRepo.uploadToServer(
                expenseType,
                expenseAmount,
                expenseDate,
                imageMultiPartList,
                item
            )
        }
    }
}