package com.loconav.locodriver

import android.util.SparseArray
import com.loconav.locodriver.language.LanguageDataClass

class Constants {

    interface HTTP {
        companion object {
            const val NUMBER_LOGIN = "login/driver"
            const val DRIVER_OTP_VERIFY = "login/driver_otp_verify"
            const val GET_DRIVER = "drivers/{id}"
            const val GET_TRIPS_LIST = "trips"
        }
    }

    interface SHARED_PREFERENCE {
        companion object {
            const val AUTH_TOKEN = "X-Auth-Token"
            const val DRIVER_ID = "driver_id"
            const val PHOTO_LINK = "photo_link"
            const val IS_LOGGED_IN = "is_logged_in"
        }
    }


    interface FRAGMENT_TAG {
        companion object {
            const val ENTER_OTP_FRAGMENT = "enter_otp_fragment"
            const val NUMBER_LOGIN_FRAGMENT = "number_login_fragment"
            const val VIEW_PROFILE_FRAGMENT = "view_profile_fragment"
        }
    }

    interface AnimationConstants {
        companion object {
            const val ANIMATION_CONSTANT_RESEND_OTP_COUNTER = 60000
            const val VALUEANIMATOR_START_ANIMATION_VALUE = 00.60f
            const val VALUEANIMATOR_END_ANIMATION_VALUE = 00.00f

        }
    }

    interface ViewProfileScreen {
        companion object {
            const val VIEW_PROFILE_TOOLBAR_TITLE = "My Profile"
        }
    }

    interface RegexConstants {
        companion object {
            const val VALID_PHONE_NUMBER_REGEX = "[2-9][0-9]{9}"
        }
    }

    interface LanguageProperty {
        companion object {
            var languageArray = arrayOf(
                LanguageDataClass("en", "English"),
                LanguageDataClass("hi", "Hindi")
            )
        }
    }
}