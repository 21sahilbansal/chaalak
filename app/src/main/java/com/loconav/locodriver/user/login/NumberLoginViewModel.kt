package com.loconav.locodriver.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.user.UserHttpService
import okhttp3.ResponseBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class NumberLoginViewModel : ViewModel(), KoinComponent{

    val userHttpService : UserHttpService by inject()

    fun getOTP(phoneNumber : String): LiveData<DataWrapper<ResponseBody>>? {
        return userHttpService.requestServerForOTP(phoneNumber)
    }


}