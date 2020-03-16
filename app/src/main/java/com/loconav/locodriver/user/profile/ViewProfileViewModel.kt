package com.loconav.locodriver.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Constants
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.user.UserHttpService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ViewProfileViewModel : ViewModel(), KoinComponent {

    private val userHttpService: UserHttpService by inject()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    fun getDriverData(driverId: Long): LiveData<DataWrapper<Driver>>? {
        return userHttpService.getDriverData(driverId)
    }

    fun saveuserData(driverData: Driver) {
        sharedPreferenceUtil.saveData(
            Constants.SharedPreferences.AUTH_TOKEN,
            driverData.authenticationToken ?: ""
        )
        sharedPreferenceUtil.saveData(Constants.SharedPreferences.DRIVER_ID, driverData.id ?: 0L)
        if (!driverData.pictures?.profilePicture.isNullOrEmpty()) {
            sharedPreferenceUtil.saveData(
                Constants.SharedPreferences.PHOTO_LINK,
                driverData.pictures?.profilePicture!![0]
            )
        }
    }

}