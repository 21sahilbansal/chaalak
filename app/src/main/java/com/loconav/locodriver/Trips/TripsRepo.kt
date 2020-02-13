package com.loconav.locodriver.Trips

import com.loconav.locodriver.Trips.model.TripRequestBody
import com.loconav.locodriver.network.HttpApiService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripsRepo : KoinComponent {
    val httpApiService: HttpApiService by inject()
    suspend fun getTripListData(tripRequestBody: TripRequestBody) =
        tripRequestBody.driverId?.let { driverId ->
            tripRequestBody.tripState?.let { tripState ->
                httpApiService.getTripListData(driverId, tripState)
            }
        }
}