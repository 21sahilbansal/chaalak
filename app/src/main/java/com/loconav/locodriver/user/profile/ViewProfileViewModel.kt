package com.loconav.locodriver.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.user.UserHttpService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ViewProfileViewModel : ViewModel(), KoinComponent {

    val userHttpService: UserHttpService by inject()

    fun getDriverData(driverId: Long): LiveData<DataWrapper<Driver>>? {
        return userHttpService.getDriverData(driverId)
    }

}