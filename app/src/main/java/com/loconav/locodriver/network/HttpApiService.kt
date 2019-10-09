package com.loconav.locodriver.network

import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.HTTP.Companion.DRIVER_OTP_VERIFY
import com.loconav.locodriver.Constants.HTTP.Companion.GET_DRIVER
import com.loconav.locodriver.Constants.HTTP.Companion.NUMBER_LOGIN
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.user.login.EnterOTPResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HttpApiService {

    @FormUrlEncoded
    @POST(NUMBER_LOGIN)
    fun getOTPForLogin(@Field("phone_number") phoneNumber: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST(DRIVER_OTP_VERIFY)
    fun validate(@Field("phone_number") phoneNumber: String, @Field("otp") otp : String): Call<EnterOTPResponse>

    @GET(GET_DRIVER)
    fun getProfileData(@Path("id") driverId : Long) : Call<Driver>

}