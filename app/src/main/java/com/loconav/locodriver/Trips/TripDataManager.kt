package com.loconav.locodriver.Trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.TripConstants.Companion.FILTER_STATES
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
    val states= arrayListOf("initialized","ongoing","delayed")
    var filterState: HashMap<String, Any> = HashMap()

    private val driverId = sharedPreferenceUtil.getData(Constants.SharedPreferences.DRIVER_ID, 0L)

    fun getTripsList(): LiveData<List<TripData>>? {
        return tripDataList
    }

    fun getTripDataMap(): LiveData<HashMap<String, TripData>>? {
        return liveTripMap
    }

    fun fetchTrips(): LiveData<Boolean>? {
        var newLiveData: LiveData<Boolean>? = null
        filterState[FILTER_STATES]=states
        filterState[Constants.TripConstants.FILTER_DRIVER_ID]=driverId
        if (0L != driverId) {
            response = TripsRepo().getTripListData(
                TripRequestBody(
                    sortOrder = Constants.TripConstants.SORT_ORDER_ASCENDING,
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
        filterState[FILTER_STATES]=states
        filterState[Constants.TripConstants.FILTER_DRIVER_ID]=driverId
        filterState[Constants.TripConstants.UNIQUE_ID] = tripId
        if (driverId != 0L) {
            response = TripsRepo().getTripListData(
                TripRequestBody(
                    sortOrder = Constants.TripConstants.SORT_ORDER_ASCENDING,
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