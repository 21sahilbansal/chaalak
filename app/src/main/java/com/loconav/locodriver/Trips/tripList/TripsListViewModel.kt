package com.loconav.locodriver.Trips.tripList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.Trips.TripDataManager
import com.loconav.locodriver.Trips.TripsRepo
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.base.DataWrapper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripsListViewModel : ViewModel(), KoinComponent {

    private val tripDataManager: TripDataManager by inject()


    fun getTripList(): MutableLiveData<DataWrapper<TripDataResponse>>? {
        return TripsRepo.getTripListData(TripsRepo.getFetchTripRequestBody())
    }

    fun fetchTripListData(): LiveData<Boolean>? {
        return tripDataManager.fetchTrips()
    }
}