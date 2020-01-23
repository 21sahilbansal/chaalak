package com.loconav.locodriver.application

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.loconav.locodriver.di.applicationModule
import org.koin.android.ext.android.startKoin


class LocoDriverApplication : MultiDexApplication() {


//    val geocoder get() =  Geocoder(applicationContext, Locale.getDefault())

    override fun onCreate() {
        super.onCreate()
        instance = this
        initStetho()
        initKoin()
    }

    private fun initKoin() {
        startKoin(this, applicationModule)
    }

    private fun initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(getApplicationContext())
            //fetch shared pref using cmd line
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
            .build())
    }


    companion object {
        lateinit var instance: LocoDriverApplication
            private set
    }

}
