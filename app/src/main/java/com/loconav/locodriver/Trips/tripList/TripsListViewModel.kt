package com.loconav.locodriver.Trips.tripList

import androidx.lifecycle.*
import com.loconav.locodriver.Trips.TripDataManager
import com.loconav.locodriver.Trips.model.TripData
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripsListViewModel : ViewModel(), KoinComponent {

    private val tripDataManager :TripDataManager by inject()

    fun getTripList(): LiveData<List<TripData>>? {
        return tripDataManager.getTripsList()
    }

    fun getTransformedData(): LiveData<Boolean>? {
        return tripDataManager.fetchTrips()
    }
}