package com.loconav.locodriver.user.login

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.loconav.locodriver.Constants
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.user.UserHttpService
import okhttp3.ResponseBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class EnterOtpViewModel : ViewModel(), KoinComponent {

    val userHttpService: UserHttpService by inject()
    val animator = ValueAnimator.ofFloat(Constants.AnimationConstants.VALUEANIMATOR_START_ANIMATION_VALUE,Constants.AnimationConstants.VALUEANIMATOR_END_ANIMATION_VALUE)

    fun validateOTP(phoneNumber: String, otp: String): LiveData<DataWrapper<EnterOTPResponse>>? {
        return userHttpService.validateOTPFromServer(phoneNumber, otp)
    }

    fun getOTP(phoneNumber: String): LiveData<DataWrapper<ResponseBody>>? {
        if(!phoneNumber.isNullOrEmpty()){
            return userHttpService.requestServerForOTP(phoneNumber)
        }
        return null
    }


    fun startAnimation(
        animationListener: Animator.AnimatorListener,
        animatorUpdateListener: ValueAnimator.AnimatorUpdateListener
    ) {
        animator.duration = Constants.AnimationConstants.ANIMATION_CONSTANT_RESEND_OTP_COUNTER.toLong()
        animator.interpolator = LinearInterpolator()
        animator.addListener(animationListener)
        animator.addUpdateListener(animatorUpdateListener)
        animator.start()
    }

    fun stopAnimation() {
        animator.removeAllUpdateListeners()
        animator.end()
    }


    fun startSmsReceiver(client: SmsRetrieverClient) {
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener {}
        task.addOnSuccessListener {
            Log.i("SmsReceiver", " started")
        }
        task.addOnFailureListener {
            it.printStackTrace()
            Log.i("SmsReceiver", "error")
        }
    }


}