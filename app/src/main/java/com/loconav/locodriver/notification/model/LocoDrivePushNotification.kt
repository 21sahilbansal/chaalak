package com.loconav.locodriver.notification.model

import com.google.gson.JsonElement
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.expense.model.Expense

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
    var expense: Expense?,
    var type: String?,
    var trip : TripData
)

data class DynamicLinkParams(
    var trip_id: Int?,
    var type: String?
)


