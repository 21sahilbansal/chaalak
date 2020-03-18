package com.loconav.locodriver.user.login

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.loconav.locodriver.R
import com.loconav.locodriver.SmsRetrieverEvent
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.language.LanguageEventBus
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_NUMBER_LOGIN_FRAGMENT
import com.loconav.locodriver.util.LocaleHelper
import kotlinx.android.synthetic.main.fragment_enter_otp.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

class EnterOtpFragment : BaseFragment() {

    var enterOtpViewModel: EnterOtpViewModel? = null
    val sharedPreferenceUtil : SharedPreferenceUtil by inject()
    val MAX_OTP_LENGTH = 4


    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {

        enterOtpViewModel = ViewModelProviders.of(this).get(EnterOtpViewModel::class.java)

        tv_change_number.text = String.format(getString(R.string.change_number_text), "?")
        tv_change_number.setOnClickListener {
            EventBus.getDefault().post(LoginEvent(OPEN_NUMBER_LOGIN_FRAGMENT))
        }

        et_otp_first_number.addTextChangedListener(otpTextWatcher)
        et_otp_second_number.addTextChangedListener(otpTextWatcher)
        et_otp_third_number.addTextChangedListener(otpTextWatcher)
        et_otp_forth_number.addTextChangedListener(otpTextWatcher)

        tv_phone_number_title.text = String.format(
            getString(R.string.otp_sent_number_text),
            arguments?.getString(PHONE_NUMBER)
        )

        tv_resend_otp.setOnClickListener {
            resendOTP()
            startSmsReceiver()
            startCountAnimation()
        }

        tv_change_language.setOnClickListener {
            LocaleHelper.toggleBetweenHiAndEn(context!!)
            EventBus.getDefault()
                .post(LanguageEventBus(LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_LOGIN))
        }

        startCountAnimation()
        startSmsReceiver()
    }

    private val otpTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                if (et_otp_first_number.text.isNotEmpty()) {
                    et_otp_second_number.requestFocus()
                }
                if (et_otp_second_number.text.isNotEmpty()) {
                    et_otp_third_number.requestFocus()
                }
                if (et_otp_third_number.text.isNotEmpty()) {
                    et_otp_forth_number.requestFocus()
                }
            } else {
                if (et_otp_forth_number.text.isEmpty()) {
                    et_otp_third_number.requestFocus()
                }
                if (et_otp_third_number.text.isEmpty()) {
                    et_otp_second_number.requestFocus()
                }
                if (et_otp_second_number.text.isEmpty()) {
                    et_otp_first_number.requestFocus()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            error_message.visibility = GONE
            if(otpEntered()){
                arguments?.getString(PHONE_NUMBER)?.let { phoneNumber ->
                    progressBar.visibility = View.VISIBLE
                    enterOtpViewModel?.validateOTP(phoneNumber, s.toString())
                        ?.observe(this@EnterOtpFragment, Observer { dataWrapper ->
                            dataWrapper.data?.let { userDataResponse ->
                                enterOtpViewModel?.saveUserDataToShareDPref(userDataResponse)
                                enterOtpViewModel?.postOpenLandingActivityEvent()
                            } ?: run {
                                progressBar.visibility = GONE
                                error_message.visibility = View.VISIBLE
                                error_message.text = dataWrapper.throwable?.message
                            }
                        })
                }
            }
        }

    }

    private fun otpEntered():Boolean{
        return et_otp_first_number.length() == 1
                && et_otp_second_number.length() == 1
                && et_otp_third_number.length() == 1
                && et_otp_forth_number.length() == 1
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_enter_otp
    }

    companion object {
        fun getInstance(phoneNumber : String) : EnterOtpFragment {
            val enterOtpFragment = EnterOtpFragment()
            val bundle = Bundle()
            bundle.putString(PHONE_NUMBER, phoneNumber)
            enterOtpFragment.arguments = bundle
            return enterOtpFragment
        }
        const val PHONE_NUMBER = "phone_number"
    }



    private fun startCountAnimation() {
        enterOtpViewModel?.startAnimation(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
                tv_resend_otp.isEnabled = false
                tv_timer.visibility = View.VISIBLE
             }
            override fun onAnimationEnd(p0: Animator?) {
                tv_timer.visibility = View.GONE
                tv_resend_otp.isEnabled = true
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
        }, ValueAnimator.AnimatorUpdateListener {
            val time1 = String.format("00:%d", it.animatedValue)
            tv_timer.text = time1
        })
    }


    private fun startSmsReceiver() {
        val client  = SmsRetriever.getClient(context!!)
        enterOtpViewModel?.startSmsReceiver(client)
    }


    private fun resendOTP() {

        enterOtpViewModel?.getOTP(arguments?.getString(PHONE_NUMBER) ?: "")
            ?.observe(this, Observer { dataWrapper ->
                dataWrapper.data?.let { userDataResponse ->
                    error_message.visibility = View.GONE
                    Log.e("variable ", userDataResponse.string())
                } ?: run {
                    dataWrapper.throwable?.message?.let {
                        error_message.visibility = View.VISIBLE
                        error_message.text =
                            error_message.context.getString(R.string.invalid_number_error_message)
                    } ?: kotlin.run {
                        error_message.visibility = View.VISIBLE
                        error_message.text = dataWrapper.throwable?.message
                    }
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun parseOTP(event: SmsRetrieverEvent) {
        when (event.message) {
            SmsRetrieverEvent.READ_OTP -> {
                val otp = event.`object` as String
                val otpArray=otp.toCharArray()
                et_otp_first_number.setText(otpArray[0].toString(), TextView.BufferType.EDITABLE)
                et_otp_second_number.setText(otpArray[1].toString(), TextView.BufferType.EDITABLE)
                et_otp_third_number.setText(otpArray[2].toString(), TextView.BufferType.EDITABLE)
                et_otp_forth_number.setText(otpArray[3].toString(), TextView.BufferType.EDITABLE)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        enterOtpViewModel?.stopAnimation()
    }

}