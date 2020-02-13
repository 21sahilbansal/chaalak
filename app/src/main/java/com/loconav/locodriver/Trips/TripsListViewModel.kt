package com.loconav.locodriver.Trips

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.DRIVER_ID
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.network.RetrofitCallback
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripsListViewModel : ViewModel(),KoinComponent {

    var tripListDataResponse = MutableLiveData<DataWrapper<TripDataResponse>>()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    val tripState = arrayListOf("ongoing", "initialized")

    suspend fun getTripsList() {
        val dataWrapper = DataWrapper<TripDataResponse>()
        val driverId = sharedPreferenceUtil.getData(DRIVER_ID, 0L)
        if (driverId != 0L) {
            val response = TripsRepo().getTripListData(
                TripRequestBody(tripState = tripState, driverId = driverId)
            )
            response?.let {
                if (it.isSuccessful) {
                    dataWrapper.data = response.body()
                    tripListDataResponse.postValue(dataWrapper)
                }
            }
        }

    }
}