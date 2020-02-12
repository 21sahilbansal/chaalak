package com.loconav.locodriver

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.loconav.locodriver.Constants.loginScreen.Companion.CORRECT_MOBILE_NUMBER
import com.loconav.locodriver.Constants.loginScreen.Companion.EMPTY_PHONE_NUMBER
import com.loconav.locodriver.Constants.loginScreen.Companion.PHONE_NUMBER_APPENDED_WITH_CHARACTER
import com.loconav.locodriver.Constants.loginScreen.Companion.PHONE_NUMBER_APPENDED_WITH_ZERO
import com.loconav.locodriver.Constants.loginScreen.Companion.PHONE_NUMBER_MORE_THEN_TEN_DIGIT
import com.loconav.locodriver.user.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Matcher
import java.util.regex.Pattern


@RunWith(AndroidJUnit4::class)
class NumberLoginFragmentTest {
    @Rule
    @JvmField
    val activityRule:ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun userCanEnterPhoneNumber(){
        textInputMatcher(CORRECT_MOBILE_NUMBER, CORRECT_MOBILE_NUMBER,R.id.editTextNumber)
    }
    @Test
    fun userShouldNotbeAbletoAddCharacters(){
        textInputMatcher("abhbhb","abhbhb",R.id.editTextNumber)
    }

    @Test
    fun userEnteredEmptyNumber(){
        invalidNumberMatcher(EMPTY_PHONE_NUMBER,R.string.blank_number_error_message)
    }

    @Test
    fun userEnteredInvalidNumber(){
        invalidNumberMatcher(Constants.loginScreen.PHONE_NUMBER_LESS_THAN_TEN_DIGITS,R.string.invalid_number_error_message)
        invalidNumberMatcher(PHONE_NUMBER_APPENDED_WITH_ZERO,R.string.invalid_number_error_message)
    }

    private fun invalidNumberMatcher(inputString:String,stringResid:Int){
        onView(withId(R.id.editTextNumber)).perform(typeText(inputString))
        onView(withId(R.id.button_get_otp)).perform(click())
        onView(withId(R.id.error_message))
            .withFailureHandler{error, _ -> Log.i("Number Test Failure",error.localizedMessage)}
            .check(matches(withText(stringResid)))
    }

    private fun textInputMatcher(inputString:String,expectedString:String,redId:Int){
        onView(withId(redId))
            .perform(typeText(inputString))
            .withFailureHandler{ error, _ -> Log.i("Number Test Failure",error.localizedMessage)}
            .check(matches(withText(expectedString)))
    }

    @Test
    fun checkForNumbertoBeValidForLessThanTenDigit(){
        assert(isPhoneNumberValid(Constants.loginScreen.PHONE_NUMBER_LESS_THAN_TEN_DIGITS))
    }
    @Test
    fun checkForNumberToBeValidForNumberAppendedWithCharacter(){
        assert(isPhoneNumberValid(PHONE_NUMBER_APPENDED_WITH_CHARACTER))
    }
    @Test
    fun checkForNumbertoBeValidForMoreThanTenDigit(){
        assert(isPhoneNumberValid(PHONE_NUMBER_MORE_THEN_TEN_DIGIT))
    }
    @Test
    fun checkForNumbertoBeValidForNumberBeginingWithZero(){
        assert(isPhoneNumberValid(PHONE_NUMBER_APPENDED_WITH_ZERO))
    }
    @Test
    fun checkForNumbertoBeValidForCorrectNumber(){
        assert(isPhoneNumberValid(CORRECT_MOBILE_NUMBER))
    }

    private fun isPhoneNumberValid(inputString:String):Boolean{
        val pattern = Pattern.compile("[2-9][0-9]{9}")
        val matcher = pattern.matcher(inputString)
        return matcher.matches()
    }
}