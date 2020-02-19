package com.loconav.locodriver.Trips

import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response

class TripsRepo : KoinComponent {
    val httpApiService: HttpApiService by inject()

    fun getTripListData(tripRequestBody: TripRequestBody): MutableLiveData<DataWrapper<TripDataResponse>>? {
        val dataWrapper = DataWrapper<TripDataResponse>()
        val apiResponse = MutableLiveData<DataWrapper<TripDataResponse>>()
        tripRequestBody.driverId?.let { driverId ->
            tripRequestBody.tripState?.let { tripState ->
                httpApiService.getTripListData(driverId, tripState)
                    .enqueue(object : RetrofitCallback<TripDataResponse>() {
                        override fun handleSuccess(
                            call: Call<TripDataResponse>,
                            response: Response<TripDataResponse>
                        ) {
                            dataWrapper.data = response.body()
                            apiResponse.postValue(dataWrapper)
                        }

                        override fun handleFailure(call: Call<TripDataResponse>, t: Throwable) {
                            dataWrapper.throwable = t
                            apiResponse.postValue(dataWrapper)
                        }
                    })
                return apiResponse
            }
        }
        return null
    }
}