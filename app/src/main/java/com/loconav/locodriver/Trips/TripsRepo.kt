package com.loconav.locodriver.Trips

import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
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
    private val httpApiService: HttpApiService by inject()

    fun getTripListData(tripRequestBody: TripRequestBody): MutableLiveData<DataWrapper<TripDataResponse>>? {
        val dataWrapper = DataWrapper<TripDataResponse>()
        val apiResponse = MutableLiveData<DataWrapper<TripDataResponse>>()
        if (tripRequestBody.driverId == null
            || tripRequestBody.tripState == null
            || tripRequestBody.filter == null
        ) {
            return null
        }
        httpApiService.getTripListData(
            tripRequestBody.driverId!!,
            tripRequestBody.tripState!!,
            tripRequestBody.filter!!
        )
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

    fun getDriverCtaTemplates(): MutableLiveData<DataWrapper<DriverCtaTemplateResponse>>? {
        val dataWrapper = DataWrapper<DriverCtaTemplateResponse>()
        val apiResponse = MutableLiveData<DataWrapper<DriverCtaTemplateResponse>>()
        httpApiService.getDriverCtaTemplate()
            .enqueue(object : RetrofitCallback<DriverCtaTemplateResponse>() {
                override fun handleSuccess(
                    call: Call<DriverCtaTemplateResponse>,
                    response: Response<DriverCtaTemplateResponse>
                ) {
                    dataWrapper.data = response.body()
                    apiResponse.postValue(dataWrapper)
                }

                override fun handleFailure(call: Call<DriverCtaTemplateResponse>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })

        return apiResponse
    }
}