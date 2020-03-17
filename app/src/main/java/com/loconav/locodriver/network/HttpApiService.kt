package com.loconav.locodriver.network

import com.loconav.locodriver.Constants.HTTP.Companion.DRIVER_OTP_VERIFY
import com.loconav.locodriver.Constants.HTTP.Companion.GET_DRIVER
import com.loconav.locodriver.Constants.HTTP.Companion.GET_DRIVER_ATTENDANCE
import com.loconav.locodriver.Constants.HTTP.Companion.GET_DRIVER_CTA_TEMPLATE
import com.loconav.locodriver.Constants.HTTP.Companion.GET_EXPENSE
import com.loconav.locodriver.Constants.HTTP.Companion.GET_EXPENSE_LIST
import com.loconav.locodriver.Constants.HTTP.Companion.GET_EXPENSE_TYPE
import com.loconav.locodriver.Constants.HTTP.Companion.GET_TRIPS_LIST
import com.loconav.locodriver.Constants.HTTP.Companion.NUMBER_LOGIN
import com.loconav.locodriver.Constants.HTTP.Companion.UPLOAD_EXPENSE
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
import com.loconav.locodriver.Trips.model.TripDataResponse
import com.loconav.locodriver.driver.model.Driver

import com.loconav.locodriver.expense.model.AddExpenseRequestBody
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expense.model.ExpenseType
import com.loconav.locodriver.expense.model.UploadExpenseResponse

import com.loconav.locodriver.user.attendence.AttendanceResponse

import com.loconav.locodriver.user.login.EnterOTPResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HttpApiService {

    @FormUrlEncoded
    @POST(NUMBER_LOGIN)
    fun getOTPForLogin(@Field("phone_number") phoneNumber: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST(DRIVER_OTP_VERIFY)
    fun validate(@Field("phone_number") phoneNumber: String, @Field("otp") otp: String): Call<EnterOTPResponse>

    @GET(GET_DRIVER)
    fun getProfileData(@Path("id") driverId: Long): Call<Driver>

    @GET(GET_DRIVER_CTA_TEMPLATE)
    fun getDriverCtaTemplate(): Call<DriverCtaTemplateResponse>

    @GET(GET_TRIPS_LIST)
    fun getTripListData(@Query("sort_order") string: String, @QueryMap filters: HashMap<String, Any>): Call<TripDataResponse>

    @GET(GET_EXPENSE_LIST)
    fun getExpenseList(@Query("page") page: Int): Call<List<Expense>>

    @GET(GET_EXPENSE)
    fun getExpense(@Path("id") expenseId: Long): Call<Expense>

    @GET(GET_EXPENSE_TYPE)
    fun getExpenseType(): Call<ExpenseType>

    @Multipart
    @POST(UPLOAD_EXPENSE)
    fun uploadExpense(
        @Part("expense_type") expenseType: RequestBody, @Part("amount") amount: RequestBody, @Part(
            "expense_date"
        ) date: RequestBody, @Part part: List<MultipartBody.Part>?
    ): Call<UploadExpenseResponse>

    @GET(GET_DRIVER_ATTENDANCE)
    fun getAttendance(): Call<AttendanceResponse>
}