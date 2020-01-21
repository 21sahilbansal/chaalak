package com.loconav.locodriver

class Constants {

    interface HTTP {
        companion object {
            const val NUMBER_LOGIN = "login/driver"
            const val DRIVER_OTP_VERIFY= "login/driver_otp_verify"
            const val GET_DRIVER = "drivers/{id}"
        }
    }

    interface SHARED_PREFERENCE {
        companion object {
            const val AUTH_TOKEN = "auth_token"
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
}