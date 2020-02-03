package com.loconav.locodriver.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.IS_LOGGED_IN

import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.LandingActivity
import com.loconav.locodriver.user.login.LoginActivity
import com.loconav.locodriver.util.LocationUtil
import com.loconav.locodriver.util.LocationWorkManager
import com.loconav.locodriver.util.PhoneUtil
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {


    val sharedPreferenceUtil : SharedPreferenceUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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

}
