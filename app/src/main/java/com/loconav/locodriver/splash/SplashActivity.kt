package com.loconav.locodriver.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.IS_LOGGED_IN

import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.LandingActivity
import com.loconav.locodriver.language.LanguageDataClass
import com.loconav.locodriver.user.login.LoginActivity
import com.loconav.locodriver.util.LocationUtil
import com.loconav.locodriver.util.PhoneUtil
import org.intellij.lang.annotations.Language
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {


    val sharedPreferenceUtil : SharedPreferenceUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(!LocationUtil().isGPSEnabled())
            openLocationSettings()
        Log.e("service provider", PhoneUtil.getServiceProviderName(baseContext))
        Log.e("Network Type", PhoneUtil.getNetworkClass(baseContext))
//        Log.e("Network Type network", NetworkUtil().getNetworkType(null).toString())

        var intent : Intent
        if(sharedPreferenceUtil.getData(IS_LOGGED_IN, false)) {
            intent = Intent(this, LandingActivity::class.java)
        }else
            intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)



//        TODO: versioning API
    }


    fun openLocationSettings(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
