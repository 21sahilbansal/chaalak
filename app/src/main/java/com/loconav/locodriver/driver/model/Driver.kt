package com.loconav.locodriver.driver.model

import com.google.gson.annotations.SerializedName


data class Driver(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("pan_number")
    var panNumber: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("authentication_token")
    var authenticationToken: String? = null,
    @SerializedName("license_number")
    var licenseNumber: String? = null,
    @SerializedName("verified")
    var verified: Boolean? = null,
    @SerializedName("license_status")
    var licenseStatus: String? = null,
    @SerializedName("license_issue_date")
    var licenseIssueDate: Long? = null,
    @SerializedName("license_expiry_date")
    var licenseExpiryDate: Long? = null,
    @SerializedName("date_of_joining")
    var dateOfJoining: Long? = null,
    @SerializedName("dob")
    var dob: Long? = null,
    @SerializedName("current_monthly_earning")
    var currentMonthlyIncome: Long? = null,
    @SerializedName("transporter_name")
    var transporterName: String? = null,
    @SerializedName("avg_dist_travelled")
    var averageDistanceTravelled: String? = null,
    @SerializedName("vehicle_number")
    var vehicleNumber: String? = null,
    @SerializedName("current_address_attributes")
    var currentAddressAttributes: Address? = null,
    @SerializedName("pictures")
    var pictures: Pictures?=null
)

data class Address(
    //in case of null in address strings, initializing them with empty.
    @SerializedName("id")
    var addressId: Long? = null,
    @SerializedName("house_number")
    var houseNumber: String? = "",
    @SerializedName("state")
    var state: Int? = null,
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("pincode")
    var pincode: String? = "",
    @SerializedName("address_line_1")
    var addressLine1: String? = "",
    @SerializedName("address_line_2")
    var addressLine2: String? = "",
    @SerializedName("address_line_3")
    var addressLine3: String? = ""
)

data class Pictures(
    @SerializedName("profile_picture")
    var profilePicture:List<String>?=null
)

