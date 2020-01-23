package com.loconav.locodriver.application

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.facebook.stetho.Stetho
import com.loconav.locodriver.network.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import timber.log.Timber
import com.loconav.locodriver.BuildConfig
import com.loconav.locodriver.Constants


class LocoDriverApplication : Application() {


    val apolloClient get() = setupApollo()


    override fun onCreate() {
        super.onCreate()
        instance = this
        initStetho()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(getApplicationContext())
        //fetch shared pref using cmd line
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
        .build());
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
                builder.header(Constants.SHARED_PREFERENCE.AUTH_TOKEN, "hQUxZDXd4yg1K5wEK4xR")
                chain.proceed(builder.build())
            }
            .build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}