package com.loconav.locodriver.user.attendence

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
    @SerializedName("data")
    var attendanceList: ArrayList<Attendance>? = null
)

data class Attendance(
    @SerializedName("id")
    var attendanceId: Int? = null,
    @SerializedName("status")
    var attendanceStatus: String? = null,
    @SerializedName("driver_id")
    var driverid: Long? = null,
    @SerializedName("date")
    var attendanceDate: Long? = null,
    @SerializedName("reason")
    var attendanceReason: String? = null
)
