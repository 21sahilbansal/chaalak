package com.loconav.locodriver.notification.fcmEvent

import com.loconav.locodriver.base.PubSubEvent

class NotificationEventBus : PubSubEvent {
    constructor(message: String) : super(message)
    constructor(message: String, cardType: Any?) : super(message, cardType)
    companion object{
        const val DELETE_FCM_ID = "delete_fcm_id"
        const val DELETE_FCM_ID_FAILURE = "DELETE_FCM_ID_FAILURE"
    }


}