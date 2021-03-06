package com.loconav.locodriver.user.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.loconav.locodriver.Constants.RetrofitConstants.Companion.DEFAULT_ERROR_MESSAGE
import com.loconav.locodriver.Constants.RetrofitConstants.Companion.NO_INTERNET
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.language.LanguageEventBus
import com.loconav.locodriver.language.LanguageEventBus.Companion.ON_LANGUAGE_CHANGED_FROM_LOGIN
import com.loconav.locodriver.user.login.LoginEvent.Companion.OPEN_ENTER_OTP_FRAGMENT
import com.loconav.locodriver.util.LocaleHelper
import com.loconav.locodriver.util.PhoneUtil
import kotlinx.android.synthetic.main.fragment_number_login.*
import org.greenrobot.eventbus.EventBus
import kotlinx.android.synthetic.main.fragment_number_login.error_message
import kotlinx.android.synthetic.main.fragment_number_login.tv_change_language


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
        val pendingIntent =
            Credentials.getClient(activity!!, options).getHintPickerIntent(hintRequest)
        button_get_otp.setOnClickListener {
            when {
                editTextNumber.text.isNullOrEmpty() -> {
                    displayNumberErrorMessage(R.string.blank_number_error_message)
                }
                PhoneUtil.isPhoneNumberValid(editTextNumber.text.toString()) -> {
                    error_message.visibility = View.GONE
                    editTextNumber.background =
                        ContextCompat.getDrawable(view.context, R.drawable.bg_edittext)
                    number_login_progressBar.visibility = View.VISIBLE
                    requestServerForOtp()
                }
                else -> {
                    displayNumberErrorMessage(R.string.invalid_number_error_message)
                }
            }
        }

        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error_message.visibility = View.INVISIBLE
                editTextNumber.background =
                    ContextCompat.getDrawable(view.context, R.drawable.bg_edittext)
            }
        })

        editTextNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editTextNumber.text.isNullOrBlank())
                startIntentSenderForResult(
                    pendingIntent.intentSender,
                    PHONE_REQUEST,
                    null,
                    0,
                    0,
                    0,
                    null
                )
        }

        tv_change_language.setOnClickListener {
            LocaleHelper.toggleBetweenHiAndEn(it.context)
            EventBus.getDefault().post(LanguageEventBus(ON_LANGUAGE_CHANGED_FROM_LOGIN))
        }
    }

    private fun requestServerForOtp() {
        numberLoginViewModel?.getOTP(editTextNumber.text.toString())
            ?.observe(this, Observer { dataWrapper ->
                dataWrapper.data?.let { userDataResponse ->
                    number_login_progressBar.visibility = View.GONE
                    EventBus.getDefault()
                        .post(LoginEvent(OPEN_ENTER_OTP_FRAGMENT, editTextNumber.text.toString()))
                    Log.e("variable ", userDataResponse.string())
                } ?: run {
                    number_login_progressBar.visibility = View.GONE
                    error_message.visibility = View.VISIBLE
                    editTextNumber.background = ContextCompat.getDrawable(
                        editTextNumber.context,
                        R.drawable.error_bg_edit_text
                    )
                    val shake = loadAnimation(context, R.anim.shake)
                    editTextNumber.startAnimation(shake)
                    dataWrapper?.throwable?.let {
                        when {
                            it.message == NO_INTERNET -> error_message.text =
                                error_message.context.getString(R.string.no_internet_connection)
                            it.message == DEFAULT_ERROR_MESSAGE -> error_message.text =
                                error_message.context.getString(R.string.something_went_wrong)
                            else -> error_message.text = it.message
                        }
                    }
                }
            })
    }

    private fun displayNumberErrorMessage(stringResId: Int) {
        val numberEditText = editTextNumber
        numberEditText.background =
            ContextCompat.getDrawable(numberEditText.context, R.drawable.error_bg_edit_text)
        error_message.visibility = View.VISIBLE
        error_message.text = getString(stringResId)
        val shake = loadAnimation(context, R.anim.shake)
        numberEditText.startAnimation(shake)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_number_login
    }

    companion object {
        fun getInstance(): NumberLoginFragment {
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
                editTextNumber.setSelection(phoneNumber?.length ?: 0)
                // Some of our device fail to get phoneNumber here.
            }
        }
    }
}