package com.loconav.locodriver.network

import android.util.Log
import com.loconav.locodriver.AppUtils
import com.loconav.locodriver.BuildConfig
import com.loconav.locodriver.Constants
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.standalone.KoinComponent
import timber.log.Timber
import java.io.IOException
import java.util.*


class HeaderInterceptor(val sharedPreferenceUtil: SharedPreferenceUtil,
                        val haulSecret : String) : Interceptor, KoinComponent {

    private val TAG = this.javaClass.simpleName
    private val KEY_AUTHORIZATION = "Authorization"
    private val KEY_APP_VERSION = "App-Version"
    private val KEY_APP_ID = "App-Id"
    private val KEY_USER_AGENT = "User-Agent"
    private val KEY_CACHE_CONTROL = "Cache-Control"

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header(KEY_AUTHORIZATION, sharedPreferenceUtil.getData(Constants.SHARED_PREFERENCE.AUTH_TOKEN, ""))
//            .header("X-Linehaul-V2-Secret", haulSecret)
            .header(KEY_APP_VERSION, AppUtils.getVersionCode())
            .header(KEY_APP_ID, BuildConfig.APPLICATION_ID)
            .header(KEY_USER_AGENT, BuildConfig.user_agent)
            .addHeader(KEY_CACHE_CONTROL, "no-cache")
            .method(original.method(), original.body())

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        if (response.code() == 401) {
            try {
                AppUtils.logout()
            } catch (e: Exception) {
                Log.e(TAG, "intercept: " + "login failed...")
//                TODO : Log exception from here.
            }

        } else {
            if (response.isSuccessful) {
                handleSuccess(response)
            } else {
                handleError(IOException(), response)
            }
        }
        return response

    }

    private fun handleError(e: IOException, response : Response) {
        Log.d(TAG, e.toString())
        Timber.d(TAG, response.code())
        Log.d(TAG, "endService error " + response.request().body() + " : " + Date().time)
    }

    private fun handleSuccess(response: Response) {
        response.body()?.let {
            val responseString = it.toString()
            Log.d(
                "http", "endService "
                          + response.request().url()
                        + " : "
                        + Date().time
                        + ":"
                        + responseString
            )
        }?:kotlin.run {
            //            TODO: log server : "sent null response even when response code is [200,300)"
        }

    }
}