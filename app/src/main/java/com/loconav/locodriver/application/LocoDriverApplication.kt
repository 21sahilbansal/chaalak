package com.loconav.locodriver.application

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.loconav.locodriver.db.sharedPF.Constants
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.network.Constants.Companion.AUTH_TOKEN
import com.loconav.locodriver.network.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient

class LocoDriverApplication : Application() {


    val apolloClient = setupApollo()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object {
        lateinit var instance: LocoDriverApplication
            private set
    }


    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(),
                    original.body())
                builder.header(Constants.AUTH_TOKEN_KEY, "hQUxZDXd4yg1K5wEK4xR")
                chain.proceed(builder.build())
            }
            .build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}