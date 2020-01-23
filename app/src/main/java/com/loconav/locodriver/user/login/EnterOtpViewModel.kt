package com.loconav.locodriver.user.login

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.user.UserHttpService
import okhttp3.ResponseBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class EnterOtpViewModel : ViewModel(), KoinComponent {

    val userHttpService: UserHttpService by inject()
    val animator = ValueAnimator.ofFloat(00.60f, 00.00f)

    fun validateOTP(phoneNumber: String, otp: String): LiveData<DataWrapper<EnterOTPResponse>>? {
        return userHttpService.validateOTPFromServer(phoneNumber, otp)
    }

    fun getOTP(phoneNumber: String): LiveData<DataWrapper<ResponseBody>>? {
        return userHttpService.requestServerForOTP(phoneNumber)
    }


    fun startAnimation(
        animationListener: Animator.AnimatorListener,
        animatorUpdateListener: ValueAnimator.AnimatorUpdateListener
    ) {
        animator.duration = 60000
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