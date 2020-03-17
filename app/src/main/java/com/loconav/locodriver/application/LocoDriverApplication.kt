package com.loconav.locodriver.application

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.loconav.locodriver.BuildConfig
import com.loconav.locodriver.di.applicationModule
import org.koin.android.ext.android.startKoin


class LocoDriverApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            initStetho()
        }
        initKoin()
    }

    private fun initKoin() {
        startKoin(this, applicationModule)
    }

    private fun initStetho() {
        Stetho.initialize(
            Stetho.newInitializerBuilder(applicationContext)
                //fetch shared pref using cmd line
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(applicationContext))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
                .build()
        )
    }


    companion object {
        lateinit var instance: LocoDriverApplication
            private set
    }

}
