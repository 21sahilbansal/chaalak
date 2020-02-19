package com.loconav.locodriver.Trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripDataManager : KoinComponent {

    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    var tripDataList: MutableLiveData<List<TripData>>? = MutableLiveData()

    var liveTripMap: MutableLiveData<HashMap<String, MutableLiveData<TripData>>>? = null

    val tripState: ArrayList<String> by lazy { arrayListOf("ongoing", "initialized") }

    val driverId = sharedPreferenceUtil.getData(Constants.SHARED_PREFERENCE.DRIVER_ID, 0L)

    fun getTripsList(): LiveData<List<TripData>>? {
        return tripDataList
    }

    fun getTripDataMap(): LiveData<HashMap<String, MutableLiveData<TripData>>>? {
        return liveTripMap
    }

    fun fetchTrips(): LiveData<Boolean>? {
        var newLiveData: LiveData<Boolean>? = null
        if (driverId != 0L) {
            val response = TripsRepo().getTripListData(
                TripRequestBody(
                    driverId = driverId,
                    tripState = tripState
                )
            )
            newLiveData = Transformations.map(response!!) { tripDataResponse ->
                tripDataList?.postValue(tripDataResponse.data?.tripDataList)
                true
            }
        }
        return newLiveData
    }

//    private fun initTripData(tripData: List<TripData>?):List<TripData>? {
//        tripDataList?.value=tripData
//        val tripMap:HashMap<String,MutableLiveData<TripData>>?=null
//        var index = 0
//        tripData?.let {
//            for (trip in it) {
//                val tripLiveData = MutableLiveData(it[index])
//                index++
//                tripMap?.put(trip.tripUniqueId!!, tripLiveData)
//            }
//            liveTripMap?.value=tripMap
//        }
//        return tripData
//    }
}