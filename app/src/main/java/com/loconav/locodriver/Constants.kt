package com.loconav.locodriver

import com.loconav.locodriver.expense.addExpense.MonthProperty
import com.loconav.locodriver.language.LanguageDataClass

class Constants {

    interface HTTP {
        companion object {
            const val NUMBER_LOGIN = "login/driver"
            const val DRIVER_OTP_VERIFY = "login/driver_otp_verify"
            const val GET_DRIVER = "drivers/{id}"
            const val GET_TRIPS_LIST = "trips"
            const val GET_DRIVER_CTA_TEMPLATE = "trip_events/labels"
            const val GET_EXPENSE_LIST = "drivers/uploadable_expenses"
            const val GET_EXPENSE = "drivers/uploadable_expenses/{id}"
            const val GET_EXPENSE_TYPE = "drivers/uploadable_expenses/expense_types"

        }
    }

    interface SharedPreferences {
        companion object {
            const val AUTH_TOKEN = "X-Auth-Token"
            const val DRIVER_ID = "driver_id"
            const val PHOTO_LINK = "photo_link"
            const val IS_LOGGED_IN = "is_logged_in"
            const val DRIVER_CTA_LABEL_TRIP_START = "trip_start"
            const val DRIVER_CTA_LABEL_CHECKPOINT_ENTRY = "cpt_entry"
            const val DRIVER_CTA_LABEL_ACTIVITY_START = "activity_start"
            const val DRIVER_CTA_LABEL_ACTIVITY_END = "activity_end"
            const val DRIVER_CTA_LABEL_CHECKPOINT_EXIT = "cpt_exit"
            const val DRIVER_CTA_LABEL_TRIP_END = "trip_end"
            const val DRIVER_CTA_LABEL_SOURCE_ENTRY = "source_entry"
            const val DRIVER_CTA_LABEL_SOURCE_EXIT = "source_exit"
            const val DRIVER_CTA_LABEL_DESTINATION_ENTRY = "destination_entry"
            const val DRIVER_CTA_LABEL_DESTINATION_EXIT = "destination_exit"
            const val DRIVER_CTA_CURRENT_STATE_LABEL = "current_label"
        }
    }


    interface FRAGMENT_TAG {
        companion object {
            const val ENTER_OTP_FRAGMENT = "enter_otp_fragment"
            const val NUMBER_LOGIN_FRAGMENT = "number_login_fragment"
            const val VIEW_PROFILE_FRAGMENT = "view_profile_fragment"
            const val TRIP_DETAIL_FRAGMENT = "trip_detail_fragment"
            const val EXPENSE_DETAIL_FRAGMENT = "expense_detail_fragment"
            const val ADD_EXPENSE_FRAGEMENT = "add_expense_fragment"
            const val IMAGE_CHOOSER_DIALOG = "image_chooser_dialog"
        }
    }

    interface AnimationConstants {
        companion object {
            const val ANIMATION_CONSTANT_RESEND_OTP_COUNTER = 30000
            const val VALUEANIMATOR_START_ANIMATION_VALUE = 00.30f
            const val VALUEANIMATOR_END_ANIMATION_VALUE = 00.00f

        }
    }

    interface TripConstants {
        companion object {
            const val SOURCE_TRIP = "trip"
            const val INTENT_ACTION_DIAL_TEXT = "tel:"
            const val CONTACT_PHONE_NUMBER = "9650793733"
            const val SORT_ORDER_ASCENDING = "asc"
            const val FILTER_DRIVER_ID = "filters[driver_id]"
            const val FILTER_STATES = "filter[states]"
            const val UNIQUE_ID = "filters[unique_id]"
            const val CHECKPOINT_IDENTIFIER = 1
            const val SOURCE_IDENTIFIER = 0
            const val DESTINATION_IDENTIFIER = 2
            val tripStateArray = arrayListOf(
                "initialized"
                , "ongoing"
                , "delayed"
            )
            val monthMap = arrayOf(
                MonthProperty(1, "January"),
                MonthProperty(2, "February"),
                MonthProperty(3, "March"),
                MonthProperty(4, "April"),
                MonthProperty(5, "May"),
                MonthProperty(6, "June"),
                MonthProperty(7, "July"),
                MonthProperty(8, "August"),
                MonthProperty(9, "September"),
                MonthProperty(10, "October"),
                MonthProperty(11, "November"),
                MonthProperty(12, "December")
            )

        }
    }

    interface ExpenseConstants {
        companion object {
            const val SOURCE_EXPENSE = "expense"
            const val VERIFICATION_PENDING = "Verfication Pending"
            const val REJECTED = "Rejected"
            const val VERIFIED = "Verified"
        }
    }

    interface PermisionsConstant {
        companion object {
            const val GALLERY_REQUEST_CODE = 1
            const val CAMERA_REQUEST_CODE = 2
        }
    }

    interface RegexConstants {
        companion object {
            const val VALID_PHONE_NUMBER_REGEX = "[2-9][0-9]{9}"
            const val DATE_TIME_FORMAT = "dd MMMM yyyy HH:mm:ss"
            const val DATE_FORMAT = "dd/MM/yyyy"
            const val TIME_FORMAT_12_HOUR = "hh:mm a"
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