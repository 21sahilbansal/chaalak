package com.loconav.locodriver.Trips

import androidx.lifecycle.*
import com.loconav.locodriver.Trips.model.TripData
import org.koin.standalone.KoinComponent

class TripsListViewModel : ViewModel(), KoinComponent {
    private val tripDataManager = TripDataManager()
    fun getTripList(): LiveData<List<TripData>>? {
        return tripDataManager.getTripsList()
    }

    fun getTransformedData(): LiveData<Boolean>? {
        return tripDataManager.fetchTrips()
    }
}