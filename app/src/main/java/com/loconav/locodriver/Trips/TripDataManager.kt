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

    var tripDataList: MutableLiveData<List<TripData>> = MutableLiveData()

    var liveTripMap: MutableLiveData<HashMap<String, TripData>> =
        MutableLiveData()
    var filterState: HashMap<String, Any> = HashMap()

    private val driverId = sharedPreferenceUtil.getData(Constants.SharedPreferences.DRIVER_ID, 0L)


    fun getTripDataMap(): LiveData<HashMap<String, TripData>>? {
        return liveTripMap
    }

    fun fetchTrips(): LiveData<Boolean>? {
        var tranformationLiveData: LiveData<Boolean>? = null
        filterState[FILTER_STATES] = Constants.TripConstants.tripStateArray
        filterState[Constants.TripConstants.FILTER_DRIVER_ID] = driverId
        if (0L != driverId) {
            response = TripsRepo.getTripListData(
                TripRequestBody(
                    sortOrder = Constants.TripConstants.SORT_ORDER_ASCENDING,
                    filter = filterState
                )
            )
            response?.let {
                tranformationLiveData = Transformations.map(it) { tripDataResponse ->
                    val tripData = tripDataResponse.data?.tripDataList
                    tripDataList.postValue(tripData)
                    val tripMap = initTripData(tripData)
                    liveTripMap.postValue(tripMap)
                    true
                }
            }
        }
        return tranformationLiveData
    }

    fun fetchSingleTrip(tripId: String): LiveData<Boolean>? {
        var tranformationLiveData: LiveData<Boolean>? = null
        filterState[FILTER_STATES] = Constants.TripConstants.tripStateArray
        filterState[Constants.TripConstants.FILTER_DRIVER_ID] = driverId
        filterState[Constants.TripConstants.UNIQUE_ID] = tripId
        if (driverId != 0L) {
            response = TripsRepo.getTripListData(
                TripRequestBody(
                    sortOrder = Constants.TripConstants.SORT_ORDER_ASCENDING,
                    filter = filterState
                )
            )
            response?.let {
                tranformationLiveData = Transformations.map(it) { tripDataResponse ->
                    val tripMap = initTripData(tripDataResponse.data?.tripDataList)
                    liveTripMap?.postValue(tripMap)
                    true
                }
            }
        }
        return tranformationLiveData
    }


    private fun initTripData(tripData: List<TripData>?): HashMap<String, TripData>? {
        val tripMap: HashMap<String, TripData>? = HashMap()
        tripData?.let {
            for (trip in it) {
                trip.tripUniqueId?.let { uniqueId ->
                    tripMap?.put(uniqueId, trip)
                }
            }
        }
        return tripMap
    }
}