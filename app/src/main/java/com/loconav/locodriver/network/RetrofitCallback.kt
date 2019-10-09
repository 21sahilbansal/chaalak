package com.loconav.locodriver.network

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


abstract class RetrofitCallback<T> : Callback<T> {

    private val TAG = javaClass.canonicalName
    private val DEFAULT_ERROR_MESSAGE = "Something Went Wrong!!"
    private val NO_INTERNET = "No Internet Connection"

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (NetworkUtils.isUserOnline)
            handleFailure(call, Throwable(DEFAULT_ERROR_MESSAGE))
        else
            handleFailure(call, Throwable(NO_INTERNET))
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.i("http", "endService " + response.raw().request().url() + " : " + Date().time)
        if (response.isSuccessful()) {
            handleSuccess(call, response)
        } else {
            var error: String
            try {
                error = JSONObject(response.errorBody()?.string()).getString("message")
            } catch (e: JSONException) {
                error = DEFAULT_ERROR_MESSAGE
                e.printStackTrace()
            } catch (e: IOException) {
                error = DEFAULT_ERROR_MESSAGE
                e.printStackTrace()
            } catch (e: NullPointerException) {
                error = DEFAULT_ERROR_MESSAGE
                e.printStackTrace()
            }
            handleFailure(call, Throwable(error))
        }
    }

    abstract fun handleSuccess(call: Call<T>, response: Response<T>)

    abstract fun handleFailure(call: Call<T>, t: Throwable)

}
