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
    var trip_id: JsonElement?,
    var type: String?
)

data class DynamicLinkParams(
    var trip_id: Int?,
    var type: String?
)
data class Expense(
    var account_id: Int?,
    var account_type: String?,
    var amount: Double?,
    var creator_id: Int?,
    var creator_type: String?,
    var creditor_name: String?,
    var debitor_id: Int?,
    var debitor_name: String?,
    var debitor_type: String?,
    var expense_date: Long?,
    var expense_type: String?,
    var id: Int?,
    var owner_id: Int?,
    var owner_type: String?,
    var remarks: String?,
    var trip_id: Int?,
    var trip_unique_id: String?,
    var truck_id: Int?,
    var uploads: Uploads?,
    var verification_status: String?
)

data class Uploads(
    var expense_document: List<Any?>?
)