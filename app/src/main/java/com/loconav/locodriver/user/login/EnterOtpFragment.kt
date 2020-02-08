package com.loconav.locodriver.user.login

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent.getIntent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.IS_LOGGED_IN
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.PHOTO_LINK
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


    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {

        enterOtpViewModel = ViewModelProviders.of(this).get(EnterOtpViewModel::class.java)

        tv_change_number.text= String.format(getString(R.string.change_number_text)+"?")
        tv_change_number.setOnClickListener{
            EventBus.getDefault().post(LoginEvent(OPEN_NUMBER_LOGIN_FRAGMENT))
        }

        tv_phone_number_title.text = String.format(getString(R.string.otp_sent_number_text),arguments?.getString(PHONE_NUMBER))

        tv_resend_otp.setOnClickListener {
            resendOTP()
            startSmsReceiver()
            startCountAnimation()
        }

        tv_change_language.setOnClickListener {
            LocaleHelper.toggleBetweenHiAndEn(context!!)
            EventBus.getDefault().post(LanguageEventBus(LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_LOGIN))
        }

        startCountAnimation()
        startSmsReceiver()

        pinEntryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length == 4) {
                    arguments?.getString(PHONE_NUMBER)?.let { phoneNumber ->
                        progressBar.visibility = View.VISIBLE
                        enterOtpViewModel?.validateOTP(phoneNumber, pinEntryEditText.text.toString())?.observe(this@EnterOtpFragment, Observer{dataWrapper ->
                            dataWrapper.data?.let {userDataResponse ->
                                sharedPreferenceUtil.saveData(Constants.SHARED_PREFERENCE.AUTH_TOKEN, userDataResponse.driver?.authenticationToken?:"")
                                sharedPreferenceUtil.saveData(Constants.SHARED_PREFERENCE.DRIVER_ID, userDataResponse.driver?.id?:0L)
                                sharedPreferenceUtil.saveData(PHOTO_LINK, userDataResponse.driver?.profilePicture?:"")
                                sharedPreferenceUtil.saveData(IS_LOGGED_IN, true)
                                EventBus.getDefault().post(LoginEvent(LoginEvent.OPEN_LANDING_ACTIVITY))
                            } ?: run{
                                progressBar.visibility = GONE
                                Toast.makeText(context, dataWrapper.throwable?.message, Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
            }
        })

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
            val time1 = String.format("%02.02f", it.animatedValue)
            tv_timer.text = time1
        })
    }


    private fun startSmsReceiver() {
        val client  = SmsRetriever.getClient(context!!)
        enterOtpViewModel?.startSmsReceiver(client)
    }


    private fun resendOTP(){

        enterOtpViewModel?.getOTP(arguments?.getString(PHONE_NUMBER)?:"")?.observe(this, Observer { dataWrapper ->
            dataWrapper.data?.let {userDataResponse ->
                Log.e("variable ", userDataResponse.string())
            } ?: run{
                Toast.makeText(context, dataWrapper.throwable?.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun parseOTP(event: SmsRetrieverEvent) {
        when (event.message) {
            SmsRetrieverEvent.READ_OTP -> {
                pinEntryEditText.setText(event.`object` as String)
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