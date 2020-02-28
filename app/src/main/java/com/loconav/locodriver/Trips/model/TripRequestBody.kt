package com.loconav.locodriver.Trips.model

import com.google.gson.annotations.SerializedName

data class TripRequestBody(
    @SerializedName("filter")
    var filter: HashMap<String, Any>? = null,
    @SerializedName("sort_order")
    var sortOrder:String?=null
)