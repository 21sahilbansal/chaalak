package com.loconav.locodriver.Trips.tripDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Trips.TripDataManager
import com.loconav.locodriver.Trips.model.TripData
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripDetailFragmentViewModel : ViewModel(), KoinComponent {

    private val tripDataManager: TripDataManager by inject()

    fun getTrip(tripId: String): LiveData<TripData>? {
        val tripLiveData: LiveData<TripData>? = null
        val tripMap = tripDataManager.getTripDataMap()
        tripMap?.let {
            return Transformations.map(it) { tripMap ->
                tripMap[tripId]
            }
        }
        return tripLiveData
    }

    fun fetchSingleTrip(tripId: String): LiveData<Boolean>? {
        return tripDataManager.fetchSingleTrip(tripId)
    }

}