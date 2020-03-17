package com.loconav.locodriver.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.Constants.SharedPreferences.Companion.ATTENDANCE_LIST
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import com.loconav.locodriver.user.attendence.AttendanceResponse
import com.loconav.locodriver.user.login.EnterOTPResponse
import okhttp3.ResponseBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response


class UserHttpService(val httpService: HttpApiService) : KoinComponent {

    private val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    fun requestServerForOTP(phoneNumber: String): LiveData<DataWrapper<ResponseBody>> {
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


    fun getAttendance(): LiveData<DataWrapper<AttendanceResponse>> {
        val dataWrapper = DataWrapper<AttendanceResponse>()
        val apiResponse = MutableLiveData<DataWrapper<AttendanceResponse>>()
        httpService.getAttendance().enqueue(object : RetrofitCallback<AttendanceResponse>() {
            override fun handleSuccess(
                call: Call<AttendanceResponse>,
                response: Response<AttendanceResponse>
            ) {
                response.body()?.let {
                    dataWrapper.data = it
                    sharedPreferenceUtil.put(it, ATTENDANCE_LIST)
                    apiResponse.postValue(dataWrapper)
                }
            }

            override fun handleFailure(call: Call<AttendanceResponse>, t: Throwable) {
                dataWrapper.throwable = t
                apiResponse.postValue(dataWrapper)
            }
        })
        return apiResponse
    }

    fun validateOTPFromServer(
        phoneNumber: String,
        otp: String
    ): LiveData<DataWrapper<EnterOTPResponse>> {
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


    fun getDriverData(driverId: Long): LiveData<DataWrapper<Driver>> {
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