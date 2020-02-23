package com.loconav.locodriver.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.loconav.locodriver.Constants.SharedPreferences.Companion.IS_LOGGED_IN

import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.TripStateGeneratorUtil
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
                it.data?.let { driverCtaLabelTemplate ->
                    driverCtaLabelTemplate?.let {
                        TripStateGeneratorUtil.setDriverCtaTemplates(driverCtaLabelTemplate)
                    }
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
