package com.loconav.locodriver

class Constants {

    interface loginScreen {
        companion object {
            const val CORRECT_MOBILE_NUMBER = "9898900989"
            const val PHONE_NUMBER_LESS_THAN_TEN_DIGITS = "23"
            const val PHONE_NUMBER_APPENDED_WITH_ZERO = "0787234345"
            const val EMPTY_PHONE_NUMBER = ""
        }
    }

    interface otpScreen {
        companion object {
            const val CORRECT_OTP = "4321"
            const val WRONG_OTP = "1234"
        }
    }
}