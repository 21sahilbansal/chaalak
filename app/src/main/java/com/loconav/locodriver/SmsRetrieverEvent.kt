package com.loconav.locodriver

import com.loconav.locodriver.base.PubSubEvent


class SmsRetrieverEvent(private val message1: String, private val obj: Any) : PubSubEvent(message1, obj) {
    companion object {
        const val READ_OTP = "read_otp"
    }
}