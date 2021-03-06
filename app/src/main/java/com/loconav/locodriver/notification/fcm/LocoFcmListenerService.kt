package com.loconav.locodriver.notification.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.loconav.locodriver.Constants
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.notification.htttpService.FCMHttpApiService
import com.loconav.locodriver.notification.model.LocoDrivePushNotification
import com.loconav.locodriver.notification.notificationManager.LocoNotificationManager
import org.koin.android.ext.android.inject


class LocoFcmListenerService : FirebaseMessagingService() {
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    val fcmHttpApiService: FCMHttpApiService by inject()
    val gson: Gson by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (!fcmHttpApiService.isFCMIDRegistered()) return
        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + message.data)
            message.data.let {
                try {
                    val jsonString = gson.toJson(it)
                    var locoDrivePushNotification: LocoDrivePushNotification =
                        gson.fromJson(jsonString, LocoDrivePushNotification::class.java)
                    locoDrivePushNotification?.let {
                        LocoNotificationManager.showPushNotification(it)
                    }
                } catch (exception: Exception) {
                    Log.e(TAG, "intercept: " + "message decoding exception")

                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        Log.e(TAG, "fcm token:" + token)
        //resetting flag for every new token generated
        fcmHttpApiService.setFCMIDRegistered(false)
        sharedPreferenceUtil.saveDataForNonDeletingPref(Constants.SharedPreferences.FCM_TOKEN, token)
       // fcmHttpApiService.setupFCMToken()
    }

    companion object {
        private const val TAG = "LocoFcmListenerService"
    }
}

