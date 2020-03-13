package com.loconav.locodriver.network

import android.util.Log
import com.loconav.locodriver.R
import com.loconav.locodriver.application.LocoDriverApplication
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.*


abstract class RetrofitCallback<T> : Callback<T> {

    private val TAG = javaClass.canonicalName

    private val appContext = LocoDriverApplication.instance.applicationContext
    private val DEFAULT_ERROR_MESSAGE = appContext.resources.getString(R.string.something_went_wrong)
    private val NO_INTERNET = appContext.resources.getString(R.string.no_internet_connection)

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (NetworkUtil.isUserOnline)
            handleFailure(call, Throwable(DEFAULT_ERROR_MESSAGE))
        else
            handleFailure(call, Throwable(NO_INTERNET))
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Timber.e("endService " + response.raw().request().url() + " : " + Date().time)
        if (response.isSuccessful()) {
            handleSuccess(call, response)
        } else {
            var error: String
            try {
                error = JSONObject(response.errorBody()?.string()).getString("message")
            } catch (e: Exception) {
                when(e){
                    is JSONException, is IOException, is NullPointerException -> {
                        error = DEFAULT_ERROR_MESSAGE
                        e.printStackTrace()
                    }
                    else -> {
                        error = DEFAULT_ERROR_MESSAGE
//                        TODO log on Crashlytics
                    }
                }
            }
            handleFailure(call, Throwable(error))
        }
    }

    abstract fun handleSuccess(call: Call<T>, response: Response<T>)

    abstract fun handleFailure(call: Call<T>, t: Throwable)

}
