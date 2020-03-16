package com.loconav.locodriver.Trips.tripList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Trips.TripDataManager
import com.loconav.locodriver.Trips.model.TripData
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripsListViewModel : ViewModel(), KoinComponent {

    private val tripDataManager: TripDataManager by inject()

    fun getTripList(): LiveData<List<TripData>>? {
        return tripDataManager.getTripsList()
    }

    fun fetchTripListData(): LiveData<Boolean>? {
        return tripDataManager.fetchTrips()
    }
}