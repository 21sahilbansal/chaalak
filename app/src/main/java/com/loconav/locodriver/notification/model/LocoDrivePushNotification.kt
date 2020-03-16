package com.loconav.locodriver.notification.model
import com.google.gson.JsonElement

data class LocoDrivePushNotification(
    var body: String?,
    var dynamic_link_params: JsonElement?,
    var id: Long?,
    var meta: JsonElement?,
    var mobile_deep_link_path: String?,
    var page: String?,
    var title: String?,
    var ts: Long
)

data class Meta(
    var trip_id: Int?,
    var type: String?
)

data class DynamicLinkParams(
    var trip_id: Int?,
    var type: String?
)