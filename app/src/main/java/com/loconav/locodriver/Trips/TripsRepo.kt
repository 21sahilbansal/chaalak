package com.loconav.locodriver.Trips

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
import com.loconav.locodriver.Trips.model.TripData
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

object TripsRepo : KoinComponent {

//    move method to sharedpf to save object and get object
//    global const : key



    private val httpApiService: HttpApiService by inject()
    private val sharedPreferenceUtil : SharedPreferenceUtil by inject()
    val apiResponse = MutableLiveData<DataWrapper<TripDataResponse>>()
    val dataWrapper = DataWrapper<TripDataResponse>()

    var filterState: HashMap<String, Any> = HashMap()

    var response: MutableLiveData<DataWrapper<TripDataResponse>>? = null
    private val driverId = sharedPreferenceUtil.getData(Constants.SharedPreferences.DRIVER_ID, 0L)


    fun getTripListData(tripRequestBody: TripRequestBody): MutableLiveData<DataWrapper<TripDataResponse>>? {
//        if (tripRequestBody.sortOrder == null
//            || tripRequestBody.filter == null
//        ) {
//            return null
//        }
        httpApiService.getTripListData(
            tripRequestBody.sortOrder!!,
            Constants.TripConstants.tripStateArray[0],
            Constants.TripConstants.tripStateArray[1],
            Constants.TripConstants.tripStateArray[2],
            driverId
        )
            .enqueue(object : RetrofitCallback<TripDataResponse>() {
                override fun handleSuccess(
                    call: Call<TripDataResponse>,
                    response: Response<TripDataResponse>
                ) {
                    response.body()?.let {
                        sharedPreferenceUtil.put(it, TRIP_RESPONSE_SHARED_PF_KEY)
                        dataWrapper.data = it
                        apiResponse.postValue(dataWrapper)
                    }
                }

                override fun handleFailure(call: Call<TripDataResponse>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        dataWrapper.data = sharedPreferenceUtil.get(TRIP_RESPONSE_SHARED_PF_KEY)
        apiResponse.postValue(dataWrapper)
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



    fun getFetchTripRequestBody(): TripRequestBody {
        filterState[Constants.TripConstants.FILTER_STATES] = Constants.TripConstants.tripStateArray
        filterState[Constants.TripConstants.FILTER_DRIVER_ID] = driverId
        return TripRequestBody(
            sortOrder = Constants.TripConstants.SORT_ORDER_ASCENDING,
            filter = filterState
        )
    }

    fun updateTripFromNotification(tripData:TripData){
        val tripList = sharedPreferenceUtil.get<TripDataResponse>(TRIP_RESPONSE_SHARED_PF_KEY)
        var tripIdPresentInList : Boolean = false
        if(!tripList?.tripDataList.isNullOrEmpty()){
            val tripArrayList = tripList?.tripDataList as ArrayList<TripData>

            for (item in tripArrayList){
                if(item.tripId == tripData.tripId){
                    tripArrayList.remove(item)
                    tripArrayList.add(tripData)
                    tripIdPresentInList = true
                }
            }
            if(!tripIdPresentInList){
                tripArrayList.add(tripData)
            }
            sharedPreferenceUtil.put(tripArrayList, TRIP_RESPONSE_SHARED_PF_KEY)
            dataWrapper.data = sharedPreferenceUtil.get(TRIP_RESPONSE_SHARED_PF_KEY)
            apiResponse.postValue(dataWrapper)
        }
    }

    const val TRIP_RESPONSE_SHARED_PF_KEY =  "trip_response_shared_pf_key"
}