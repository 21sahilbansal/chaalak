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
import java.io.IOException
import java.util.*


class HeaderInterceptor(val sharedPreferenceUtil: SharedPreferenceUtil,
                        val haulSecret : String) : Interceptor, KoinComponent {

    private val TAG = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header("Authorization", sharedPreferenceUtil.getData(Constants.SHARED_PREFERENCE.AUTH_TOKEN, ""))
//            .header("X-Linehaul-V2-Secret", haulSecret)
            .header("App-Version", AppUtils.getVersionCode())
            .header("App-Id", BuildConfig.APPLICATION_ID)
            .header("User-Agent", BuildConfig.user_agent)
            .addHeader("Cache-Control", "no-cache")
            .method(original.method(), original.body())

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val url = request.url().toString()
        if (response.code() == 401) {
            try {
                AppUtils.logout()
            } catch (e: Exception) {
                Log.e(TAG, "intercept: " + "login failed...")
            }

        } else {
            if (response.isSuccessful) {
                handleSuccess(response)
            } else {
                handleError(IOException(), url)
            }
        }
        return response

    }

    private fun handleError(e: IOException, service: String) {
        Log.d(TAG, e.toString())
        Log.d(TAG, "endService error " + service + " : " + Date().time)
    }

    private fun handleSuccess(response: Response) {
        if (response.body() != null) {
            val responseString = response.body()!!.toString()
            Log.d(
                "http", "endService "
                        + response.request().url()
                        + " : "
                        + Date().time
                        + ":"
                        + responseString
            )
        }
    }
}