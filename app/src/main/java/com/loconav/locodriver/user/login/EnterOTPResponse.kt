package com.loconav.locodriver.user.login

import com.google.gson.annotations.SerializedName
import com.loconav.locodriver.driver.model.Driver

class EnterOTPResponse {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("driver")
    var driver: Driver? = null
}
