package com.loconav.locodriver.user

import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import com.loconav.locodriver.user.login.EnterOTPResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class UserHttpService(val httpService: HttpApiService) {

    fun requestServerForOTP(phoneNumber: String): MutableLiveData<DataWrapper<ResponseBody>> {
        val dataWrapper = DataWrapper<ResponseBody>()
        val apiResponse = MutableLiveData<DataWrapper<ResponseBody>>()
        if (phoneNumber.isEmpty()) {
            val emptyPhoneNumberThrowable = Throwable()
            dataWrapper.throwable = emptyPhoneNumberThrowable
            apiResponse.postValue(dataWrapper)
        } else {
            httpService.getOTPForLogin(phoneNumber)
                .enqueue(object : RetrofitCallback<ResponseBody>() {
                    override fun handleSuccess(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val serverResponse = response.body()
                        dataWrapper.data = serverResponse
                        apiResponse.postValue(dataWrapper)
                    }

                    override fun handleFailure(call: Call<ResponseBody>, t: Throwable) {
                        dataWrapper.throwable = t
                        apiResponse.postValue(dataWrapper)
                    }
                })
        }
        return apiResponse
    }


    fun validateOTPFromServer(
        phoneNumber: String,
        otp: String
    ): MutableLiveData<DataWrapper<EnterOTPResponse>> {
        val dataWrapper = DataWrapper<EnterOTPResponse>()
        val apiResponse = MutableLiveData<DataWrapper<EnterOTPResponse>>()

        httpService.validate(phoneNumber, otp)
            .enqueue(object : RetrofitCallback<EnterOTPResponse>() {
                override fun handleSuccess(
                    call: Call<EnterOTPResponse>,
                    response: Response<EnterOTPResponse>
                ) {
                    val serverResponse = response.body()
                    dataWrapper.data = serverResponse
                    apiResponse.postValue(dataWrapper)
                }

                override fun handleFailure(call: Call<EnterOTPResponse>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }


    fun getDriverData(driverId: Long): MutableLiveData<DataWrapper<Driver>> {
        val dataWrapper = DataWrapper<Driver>()
        val apiResponse = MutableLiveData<DataWrapper<Driver>>()

        httpService.getProfileData(driverId).enqueue(object : RetrofitCallback<Driver>() {
            override fun handleSuccess(call: Call<Driver>, response: Response<Driver>) {
                val serverResponse = response.body()
                dataWrapper.data = serverResponse
                apiResponse.postValue(dataWrapper)
            }

            override fun handleFailure(call: Call<Driver>, t: Throwable) {
                dataWrapper.throwable = t
                apiResponse.postValue(dataWrapper)
            }
        })
        return apiResponse
    }

    /**
     * this method can be used to save user data into sharedpf
     */
    fun saveUserData() {

    }
}