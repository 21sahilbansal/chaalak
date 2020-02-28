package com.loconav.locodriver.user.login

import com.loconav.locodriver.base.PubSubEvent

class LoginEvent : PubSubEvent {

    constructor(message: String, `object`: Any) : super(message, `object`)

    constructor(message: String) : super(message)

    companion object {
        const val OPEN_ENTER_OTP_FRAGMENT = "open_enter_otp_fragment"
        const val OPEN_LANDING_ACTIVITY = "open_landing_activity"
        const val OPEN_NUMBER_LOGIN_FRAGMENT = "open_number_login_fragment"
        const val OPEN_SPLASH_ACTIVITY = "open_splash_activity"
    }
}