package com.loconav.locodriver.Trips.model

import com.google.gson.annotations.SerializedName

data class DriverCtaTemplateResponse(
    @SerializedName("trip_start")
    var tripStart: String? = null,
    @SerializedName("cpt_entry")
    var checkpointEntry: String? = null,
    @SerializedName("activity_start")
    var activityStart: String? = null,
    @SerializedName("activity_end")
    var activityEnd: String? = null,
    @SerializedName("cpt_exit")
    var checkpointExit: String? = null,
    @SerializedName("trip_end")
    var tripEnd: String? = null
)