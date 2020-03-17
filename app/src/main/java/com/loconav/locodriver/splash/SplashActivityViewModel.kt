package com.loconav.locodriver.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Trips.TripsRepo
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
import com.loconav.locodriver.base.DataWrapper

class SplashActivityViewModel : ViewModel() {
    fun getDriverCtatemplates(): LiveData<DataWrapper<DriverCtaTemplateResponse>>? {
        return TripsRepo.getDriverCtaTemplates()
    }
}