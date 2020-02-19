package com.loconav.locodriver.Trips.model

import com.google.gson.annotations.SerializedName

data class TripRequestBody(
    @SerializedName("states")
    var tripState: ArrayList<String>? = null,
    @SerializedName("driver_id")
    var driverId: Long? = null,
    @SerializedName("filter")
    var filter: ArrayList<String>?=null
)