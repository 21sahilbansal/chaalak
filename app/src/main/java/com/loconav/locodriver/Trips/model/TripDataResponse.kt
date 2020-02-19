package com.loconav.locodriver.Trips.model

import com.google.gson.annotations.SerializedName

data class TripDataResponse(
    @SerializedName("data")
    var tripDataList: List<TripData>? = null
)

data class TripData(
//    @SerializedName("flag")
//    var actionableFlag:Boolean?=null,
    @SerializedName("id")
    var tripId: Long? = null,
    @SerializedName("vehicle_number")
    var vehicleNumber: String? = null,
    @SerializedName("vehicle_id")
    var vehicleId: Long? = null,
    @SerializedName("unique_id")
    var tripUniqueId: String? = null,
    @SerializedName("should_start_ts")
    var tripShouldStartTs: Long? = null,
    @SerializedName("start_time_ts")
    var tripStartTimeTs: Long? = null,
    @SerializedName("company")
    var company: String? = null,
    @SerializedName("end_time_ts")
    var TripEndTimeTs: Long? = null,
    @SerializedName("returned_unsuccessfully_at_ts")
    var returnedUnsuccefullyTs: Long? = null,
    @SerializedName("state")
    var tripState: String? = null,
    @SerializedName("owner_type")
    var tripOwnerType: String? = null,
    @SerializedName("owner_id")
    var tripOwnerId: Long? = null,
    @SerializedName("owner_email")
    var tripOwnerMail: String? = null,
    @SerializedName("owner_name")
    var tripOwnerName: String? = null,
    @SerializedName("transporter_name")
    var transporterName: String? = null,
    @SerializedName("source")
    var tripSource: CheckPointData? = null,
    @SerializedName("destination")
    var tripDestination: CheckPointData? = null,
    @SerializedName("check_points")
    var checkPointsList: List<CheckPointData>? = null,
    @SerializedName("driver_cta")
    var driverCta: Cta? = null
)

data class Cta(
    @SerializedName("name")
    var ctaName: String? = null
)

data class CheckPointData(

    @SerializedName("id")
    var checkPointId: Long? = null,
    @SerializedName("name")
    var checkPointName: String? = null,
    @SerializedName("coordinates")
    var checkPointCoordinates: String? = null,
    @SerializedName("address")
    var checkPointAddress: String? = null,
    @SerializedName("status")
    var checkPointStatus: String? = null,
    @SerializedName("position")
    var checkPointPosition: Int? = null,
    @SerializedName("identifier")
    var checkPointIdentifier: Int? = null,
    @SerializedName("eta")
    var entryEta: Long? = null,
    @SerializedName("exit_eta")
    var exitEta: Long? = null,
    @SerializedName("entry_at")
    var entryTs: Long? = null,
    @SerializedName("exit_at")
    var exitTs: Long? = null,
    @SerializedName("target_time")
    var targetTs: Long? = null,
    @SerializedName("activity")
    var chekPointActivity: CheckpointActivity? = null,
    @SerializedName("polygon_details")
    var polygonDetail: PolygonDetails? = null
)

data class PolygonDetails(
    @SerializedName("city")
    var checkPointCity: City? = null
)

data class City(
    @SerializedName("value")
    var cityName: String? = null,
    @SerializedName("type")
    var type: String? = null
)

data class CheckpointActivity(
    @SerializedName("name")
    var checkPointActivityName: String? = null,
    @SerializedName("start_ts")
    var checkPointActivityStartTs: Long? = null,
    @SerializedName("end_ts")
    var checkPointActivityEndTs: Long? = null
)