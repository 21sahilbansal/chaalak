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

}