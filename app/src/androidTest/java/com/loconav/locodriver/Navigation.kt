package com.loconav.locodriver

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers

object Navigation {

    fun goToOtpScreen(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber))
            .perform(ViewActions.typeText(Constants.loginScreen.CORRECT_MOBILE_NUMBER))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_get_otp)).perform(ViewActions.click())
    }
}