package com.loconav.locodriver.language

import com.loconav.locodriver.base.PubSubEvent

class LanguageEventBus : PubSubEvent {

    constructor(message: String, `object`: Any?) : super(message, `object`)

    constructor(message: String) : super(message)


    companion object {
        const val ON_LANGUAGE_CHANGED_FROM_PROFILE = "on_language_changed_from_profile"
        const val ON_LANGUAGE_CHANGED_FROM_LOGIN = "on_language_changed_from_login"
    }
}