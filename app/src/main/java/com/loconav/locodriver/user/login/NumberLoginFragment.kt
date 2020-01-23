package com.loconav.locodriver.user.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_number_login.*
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_ENTER_OTP_FRAGMENT
import org.greenrobot.eventbus.EventBus
import com.loconav.locodriver.R
import com.loconav.locodriver.language.LanguageEventBus
import com.loconav.locodriver.language.LanguageEventBus.Companion.ON_LANGUAGE_CHANGED_FROM_LOGIN
import com.loconav.locodriver.util.LocaleHelper


class NumberLoginFragment : BaseFragment() {


    var numberLoginViewModel: NumberLoginViewModel? = null


    val hintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .setEmailAddressIdentifierSupported(false)
        .build()
    val options = CredentialsOptions.Builder()
        .forceEnableSaveDialog()
        .build()


    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        numberLoginViewModel = ViewModelProviders.of(this).get(NumberLoginViewModel::class.java)
        val pendingIntent = Credentials.getClient(activity!!, options).getHintPickerIntent(hintRequest)
        button_get_otp.setOnClickListener {
            if(editTextNumber.text.isNullOrEmpty()) {
                val shake = loadAnimation(context, R.anim.shake)
                editTextNumber.startAnimation(shake)
            } else {
                requestServerForOtp()
                EventBus.getDefault().post(LoginEvent(OPEN_ENTER_OTP_FRAGMENT, editTextNumber.text.toString()))
            }
        }


        editTextNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                startIntentSenderForResult(pendingIntent.intentSender, PHONE_REQUEST, null, 0, 0, 0, null)
        }

        tv_change_language.setOnClickListener {
            LocaleHelper.toggleBetweenHiAndEn(context!!)
            EventBus.getDefault().post(LanguageEventBus(ON_LANGUAGE_CHANGED_FROM_LOGIN))
        }
    }

    private fun requestServerForOtp() {
        numberLoginViewModel?.getOTP(editTextNumber.text.toString())?.observe(this, Observer { dataWrapper ->
            dataWrapper.data?.let {userDataResponse ->
                Log.e("variable ", userDataResponse.string())
            } ?: run{
                Toast.makeText(context, dataWrapper.throwable?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_number_login
    }

    companion object {
        fun getInstance() : NumberLoginFragment {
            return NumberLoginFragment()
        }
        const val PHONE_REQUEST = 4000
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHONE_REQUEST) {
            if (data != null) {
                val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                val phoneNumber = credential?.id?.removePrefix("+91")
                editTextNumber.setText(phoneNumber)
                editTextNumber.setSelection(phoneNumber?.length?:0)
                // Some of our device fail to get phoneNumber here.
            }
        }
    }


}