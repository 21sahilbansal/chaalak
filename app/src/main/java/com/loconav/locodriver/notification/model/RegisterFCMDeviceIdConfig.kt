package com.loconav.locodriver.notification.model

import com.google.gson.annotations.SerializedName

data class RegisterFCMDeviceIdConfig(
    @SerializedName("token")
    var deviceToken: String? = null,

    @SerializedName("device_identifier")
    var macAddress: String? = null,

    @SerializedName("app_version")
    var androidVersion: String? = null,

    @SerializedName("android_version")
    var osVersion: String? = null
)

