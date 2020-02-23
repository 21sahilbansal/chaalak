package com.loconav.locodriver.Trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripDataManager : KoinComponent {

    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    var response: MutableLiveData<DataWrapper<TripDataResponse>>? = null

    var tripDataList: MutableLiveData<List<TripData>>? = MutableLiveData()

    var liveTripMap: MutableLiveData<HashMap<String, TripData>>? =
        MutableLiveData()

    var filterState: HashMap<String, String> = HashMap()

    private val tripState: ArrayList<String> by lazy { arrayListOf("ongoing", "initialized") }

    private val driverId = sharedPreferenceUtil.getData(Constants.SharedPreferences.DRIVER_ID, 0L)

    fun getTripsList(): LiveData<List<TripData>>? {
        return tripDataList
    }

    fun getTripDataMap(): LiveData<HashMap<String, TripData>>? {
        return liveTripMap
    }

    fun fetchTrips(): LiveData<Boolean>? {
        var newLiveData: LiveData<Boolean>? = null
        filterState[Constants.TripConstants.SORT_ORDER_FILTER_STATE_KEY] =
            Constants.TripConstants.SORT_ORDER_ASCENDING
        if (driverId != 0L) {
            response = TripsRepo().getTripListData(
                TripRequestBody(
                    driverId = driverId,
                    tripState = tripState,
                    filter = filterState
                )
            )
            response?.let {
                newLiveData = Transformations.map(it) { tripDataResponse ->
                    tripDataList?.postValue(tripDataResponse.data?.tripDataList)
                    val tripMap = initTripData(tripDataResponse.data?.tripDataList)
                    liveTripMap?.postValue(tripMap)
                    true
                }
            }
        }
        return newLiveData
    }

    fun fetchSingleTrip(tripId: String): LiveData<Boolean>? {
        var newLiveData: LiveData<Boolean>? = null
        filterState[Constants.TripConstants.SORT_ORDER_FILTER_STATE_KEY] =
            Constants.TripConstants.SORT_ORDER_ASCENDING
        filterState[Constants.TripConstants.UNIQUE_ID] = tripId
        if (driverId != 0L) {
            response = TripsRepo().getTripListData(
                TripRequestBody(
                    driverId = driverId,
                    tripState = tripState,
                    filter = filterState
                )
            )
            response?.let {
                newLiveData = Transformations.map(it) { tripDataResponse ->
                    val tripMap = initTripData(tripDataResponse.data?.tripDataList)
                    liveTripMap?.postValue(tripMap)
                    true
                }
            }
        }
        return newLiveData
    }


    private fun initTripData(tripData: List<TripData>?): HashMap<String, TripData>? {
        val tripMap: HashMap<String, TripData>? = HashMap()
        var index = 0
        tripData?.let {
            for (trip in it) {
                val tripLiveData = it[index]
                index++
                trip.tripUniqueId?.let { uniqueId ->
                    tripMap?.put(uniqueId, tripLiveData)
                }
            }
        }
        return tripMap
    }
}