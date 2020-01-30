package com.loconav.locodriver.user.login

import android.content.Intent
import android.os.Bundle
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.NUMBER_LOGIN_FRAGMENT

import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import com.loconav.locodriver.landing.LandingActivity
import com.loconav.locodriver.language.LanguageEventBus
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_ENTER_OTP_FRAGMENT
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_LANDING_ACTIVITY
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_NUMBER_LOGIN_FRAGMENT
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LoginActivity : BaseFragmentActivity() {

    override val frameId: Int
        get() = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        openNumberLoginFragment()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvents(event: LoginEvent) {
        when (event.message) {
            OPEN_NUMBER_LOGIN_FRAGMENT -> openNumberLoginFragment()
            OPEN_ENTER_OTP_FRAGMENT -> inflateReplaceFragment(EnterOtpFragment.getInstance(event.`object` as String), true, Constants.FRAGMENT_TAG.ENTER_OTP_FRAGMENT)
            OPEN_LANDING_ACTIVITY -> {
                val intent = Intent(this, LandingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvents(event: LanguageEventBus) {
        when (event.message) {
            LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_LOGIN -> recreateCurrentStateOfActivity()
        }
    }




    private fun openNumberLoginFragment(){
        inflateReplaceFragment(NumberLoginFragment.getInstance(), false, NUMBER_LOGIN_FRAGMENT)
    }



    private fun recreateCurrentStateOfActivity() {
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0);
    }
}
