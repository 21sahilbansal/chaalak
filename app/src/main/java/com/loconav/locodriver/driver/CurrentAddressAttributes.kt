package com.loconav.locodriver.driver

import com.google.gson.annotations.SerializedName

class CurrentAddressAttributes {

    @SerializedName("state")
    var state: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("pincode")
    var pinCode: String? = null

    @SerializedName("address_line_1")
    var addressLine1: String? = null

    @SerializedName("address_line_2")
    var addressLine2: String? = null

    @SerializedName("address_line_3")
    var addressLine3: String? = null

    @SerializedName("house_number")
    var houseNumber: String? = null

}
