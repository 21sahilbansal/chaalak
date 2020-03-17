package com.loconav.locodriver.user.profile

import android.os.Bundle
import com.loconav.locodriver.AppUtils
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.ATTENDANCE_FRAGMENT

import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.VIEW_PROFILE_FRAGMENT
import com.loconav.locodriver.language.LanguageEventBus
import com.loconav.locodriver.user.attendence.AttendanceFragment
import com.loconav.locodriver.user.login.LoginEvent


class ProfileActivity : BaseFragmentActivity() {


    override val frameId: Int
        get() = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        inflateAddFragment(ViewProfileFragment.instance(), false, VIEW_PROFILE_FRAGMENT)
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
    fun onLanguageChanged(event: LanguageEventBus) {
        when (event.message) {
            LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_PROFILE -> AppUtils.relaunchApp()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEventRecieved(event: LoginEvent) {
        when (event.message) {
            LoginEvent.OPEN_ATTANDANCE_FRAGMENT -> {
                inflateReplaceFragment(
                    AttendanceFragment.getinstance(),
                    true,
                    ATTENDANCE_FRAGMENT
                )
            }
        }
    }
}
