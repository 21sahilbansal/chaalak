package com.loconav.locodriver.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_ACTIVITY_END
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_ACTIVITY_START
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_CHECKPOINT_ENTRY
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_CHECKPOINT_EXIT
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_TRIP_END
import com.loconav.locodriver.Constants.SharedPreferences.Companion.IS_LOGGED_IN

import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.LandingActivity
import com.loconav.locodriver.user.login.LoginActivity
import com.loconav.locodriver.util.PhoneUtil
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {


    private val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var splashActivityViewModel: SplashActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        splashActivityViewModel =
            ViewModelProviders.of(this).get(SplashActivityViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.e("service provider", PhoneUtil.getServiceProviderName(baseContext))
        Log.e("Network Type", PhoneUtil.getNetworkClass(baseContext))
//        Log.e("Network Type network", NetworkUtil().getNetworkType(null).toString())

        if (sharedPreferenceUtil.getData(IS_LOGGED_IN, false)) {
            splashActivityViewModel?.getDriverCtatemplates()?.observe(this, Observer {
                it.data?.tripStart?.let {
                    sharedPreferenceUtil.saveData(Constants.SharedPreferences.DRIVER_CTA_LABEL_TRIP_START, it)
                }
                it.data?.checkpointEntry?.let {
                    sharedPreferenceUtil.saveData(DRIVER_CTA_LABEL_CHECKPOINT_ENTRY, it)
                }
                it.data?.activityStart?.let {
                    sharedPreferenceUtil.saveData(DRIVER_CTA_LABEL_ACTIVITY_START, it)
                }
                it.data?.activityEnd?.let {
                    sharedPreferenceUtil.saveData(DRIVER_CTA_LABEL_ACTIVITY_END , it)
                }
                it.data?.checkpointExit?.let {
                    sharedPreferenceUtil.saveData(DRIVER_CTA_LABEL_CHECKPOINT_EXIT, it)
                }
                it.data?.tripEnd?.let {
                    sharedPreferenceUtil.saveData(DRIVER_CTA_LABEL_TRIP_END , it)
                }
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)

            })
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}
