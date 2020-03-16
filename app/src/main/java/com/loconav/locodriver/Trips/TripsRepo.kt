package com.loconav.locodriver.Trips

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response

class TripsRepo : KoinComponent {
    private val httpApiService: HttpApiService by inject()
    private val sharedPreferenceUtil : SharedPreferenceUtil by inject()
    val apiResponse = MutableLiveData<DataWrapper<TripDataResponse>>()
    val dataWrapper = DataWrapper<TripDataResponse>()


    fun getTripListData(tripRequestBody: TripRequestBody): MutableLiveData<DataWrapper<TripDataResponse>>? {
        if (tripRequestBody.sortOrder == null
            || tripRequestBody.filter == null
        ) {
            return null
        }
        httpApiService.getTripListData(
            tripRequestBody.sortOrder!!,
            tripRequestBody.filter!!
        )
            .enqueue(object : RetrofitCallback<TripDataResponse>() {
                override fun handleSuccess(
                    call: Call<TripDataResponse>,
                    response: Response<TripDataResponse>
                ) {
                    response.body()?.let {
                        dataWrapper.data = it
                        apiResponse.postValue(dataWrapper)
                        saveTripResponse(it, TRIP_RESPONSE_SHARED_PF_KEY)
                    }
                }

                override fun handleFailure(call: Call<TripDataResponse>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        dataWrapper.data = getTripResponse(TRIP_RESPONSE_SHARED_PF_KEY)
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

    fun saveTripResponse(`object`: TripDataResponse, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        if(!jsonString.isNullOrEmpty()) {
            sharedPreferenceUtil.saveData(key, jsonString)
            dataWrapper.data = `object`
            apiResponse.postValue(dataWrapper)
        }
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
     fun getTripResponse(key: String): TripDataResponse?{
        val value = sharedPreferenceUtil.getData(key, "")
        try {
            return GsonBuilder().create().fromJson(value, TripDataResponse::class.java)
        }catch (e : Exception) {
            return null
        }
    }



    companion object {
        const val TRIP_RESPONSE_SHARED_PF_KEY =  "trip_response_shared_pf_key"
    }
}