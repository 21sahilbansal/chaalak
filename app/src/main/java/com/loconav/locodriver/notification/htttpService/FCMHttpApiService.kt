package com.loconav.locodriver.notification.htttpService

import android.os.Build
import com.loconav.locodriver.AppUtils
import com.loconav.locodriver.Constants
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import com.loconav.locodriver.notification.fcmEvent.NotificationEventBus
import com.loconav.locodriver.notification.model.RegisterFCMDeviceIdConfig
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response

class FCMHttpApiService : KoinComponent {
    val httpApiService: HttpApiService by inject()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()


    fun registerFCMDeviceId(registerFCMDeviceIdConfig: RegisterFCMDeviceIdConfig) {
        httpApiService.registerDeviceIdToken(registerFCMDeviceIdConfig)
            .enqueue(object : RetrofitCallback<ResponseBody?>() {
                override fun handleSuccess(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    setFCMIDRegistered(true)
                }
                override fun handleFailure(call: Call<ResponseBody?>, t: Throwable) {
                    setFCMIDRegistered(false)
                }
            })
    }

    fun deleteFCMDeviceId() {
        if (!isFCMIDRegistered()) return
        httpApiService.deleteFCMToken(AppUtils.getDeviceId())
            .enqueue(object : RetrofitCallback<ResponseBody?>() {
                override fun handleSuccess(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    setFCMIDRegistered(false)
                    EventBus.getDefault().post(
                        NotificationEventBus(NotificationEventBus.DELETE_FCM_ID, response.body())
                    )
                }

                override fun handleFailure(call: Call<ResponseBody?>, t: Throwable) {
                    EventBus.getDefault().post(
                        NotificationEventBus(NotificationEventBus.DELETE_FCM_ID_FAILURE, t.message)
                    )
                }
            })

    }


    fun setupFCMToken() {
        if (isFCMIDRegistered())
            return
        val token = sharedPreferenceUtil.getDataForNonDeletingPrefs(Constants.SharedPreferences.FCM_TOKEN, "")
        if (!token.isNullOrEmpty()) {
            val deviceId = AppUtils.getDeviceId()
            val androidVersion = AppUtils.getVersionCode()
            val osVersion = Build.VERSION.SDK_INT.toString()
            val registerFCMDeviceIdConfig = RegisterFCMDeviceIdConfig()
            registerFCMDeviceIdConfig.androidVersion = androidVersion
            registerFCMDeviceIdConfig.deviceToken = token
            registerFCMDeviceIdConfig.macAddress = deviceId
            registerFCMDeviceIdConfig.osVersion = osVersion
            registerFCMDeviceId(registerFCMDeviceIdConfig)
        }
    }

    private fun clearAllUserData() {
        sharedPreferenceUtil.deleteAllData()
        setFCMIDRegistered(false)
    }

    fun isFCMIDRegistered(): Boolean {
        return sharedPreferenceUtil.getDataForNonDeletingPrefs(Constants.SharedPreferences.FCM_TOKEN_REGISTERED, false) && sharedPreferenceUtil.getDataForNonDeletingPrefs(Constants.SharedPreferences.LAST_SAVED_APP_VERSION, "0").equals( AppUtils.getVersionCode())
    }
    fun setFCMIDRegistered(register: Boolean) {
        sharedPreferenceUtil.saveDataForNonDeletingPref(Constants.SharedPreferences.FCM_TOKEN_REGISTERED, register)
        if (register)
            sharedPreferenceUtil.saveDataForNonDeletingPref(
                Constants.SharedPreferences.LAST_SAVED_APP_VERSION,
                AppUtils.getVersionCode()
            )
    }

    companion object {
        const val NOTIFICATION_TAG = "notification_tag"
    }
}
