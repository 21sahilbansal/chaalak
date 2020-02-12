package com.loconav.locodriver

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.loconav.locodriver.user.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnterOTPScreenTest {
    @Rule
    @JvmField
    val activityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext


    @Test
    fun otpRecieverNumberSameAsSent(){
        Navigation.goToOtpScreen()
        onView(withId(R.id.tv_phone_number_title)).check(matches(withText("Enter OTP code sent to ${Constants.loginScreen.CORRECT_MOBILE_NUMBER}")))
    }

    @Test
    fun enterFourDigitOtp(){
        Navigation.goToOtpScreen()
        onView(withId(R.id.pinEntryEditText)).perform(typeText(Constants.otpScreen.CORRECT_OTP)).withFailureHandler { error, _ -> Log.i("Enter Otp Screen",error.message)}
            .check(matches(withText(Constants.otpScreen.CORRECT_OTP)))
    }

    @Test
    fun progressDialogVissible(){
        Navigation.goToOtpScreen()
        onView(withId(R.id.pinEntryEditText)).perform(typeText(Constants.otpScreen.CORRECT_OTP)).check(matches(withText(Constants.otpScreen.CORRECT_OTP)))
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }

    @Test
    fun invalidOtpTest(){
        Navigation.goToOtpScreen()
        onView(withId(R.id.pinEntryEditText)).perform(typeText(Constants.otpScreen.WRONG_OTP)).check(matches(withText(Constants.otpScreen.CORRECT_OTP)))
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        //error should not be shown by a toast (there should be error message)
    }

}
