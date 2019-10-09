package com.loconav.locodriver.driver.model

import com.google.gson.annotations.SerializedName
import com.loconav.locodriver.driver.CurrentAddressAttributes


class Driver {
    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("phone_number")
    var phoneNumber: String? = null
    @SerializedName("authentication_token")
    var authenticationToken: String? = null
    @SerializedName("license_number")
    var licenseNumber: String? = null
    @SerializedName("verified")
    var verified: Boolean? = null
    @SerializedName("license_status")
    var licenseStatus: String? = null
    @SerializedName("license_issue_date")
    var licenseIssueDate: Long? = null
    @SerializedName("license_expiry_date")
    var licenseExpiryDate: Long? = null
    @SerializedName("dob")
    var dob: Long? = null
    @SerializedName("profile_picture")
    var profilePicture: String? = null
    @SerializedName("transporter_name")
    var transporterName: String? = null
    @SerializedName("avg_dist_travelled")
    var averageDistanceTravelled: String? = null
    @SerializedName("current_address")
    var currentAddressAttributes : String?=null

}
