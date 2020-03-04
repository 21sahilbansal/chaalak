package com.loconav.locodriver.expense

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "expense_Id")
    var expenseId: Long? = null,
    @SerializedName("truck_id")
    @ColumnInfo(name = "truck_id")
    var truckId: Long? = null,
    @SerializedName("remarks")
    @ColumnInfo(name = "remarks")
    var expenseRemarks: String? = null,
    @SerializedName("amount")
    @ColumnInfo(name = "amount")
    var amount: Int? = null,
    @SerializedName("expense_date")
    @ColumnInfo(name = "expense_date")
    var expenseDate: Long? = null,
    @SerializedName("expense_type")
    @ColumnInfo(name = "expense_type")
    var expenseType: String? = null,
    @SerializedName("verification_status")
    @ColumnInfo(name = "verification_status")
    var verificationStatus: String? = null,
    @SerializedName("trip_unique_id")
    @ColumnInfo(name = "trip_unique_id")
    var tripUniqueId: String? = null,
    @SerializedName("creditor_name")
    @ColumnInfo(name = "creditor_name")
    var creditorName: String? = null,
    @SerializedName("debitor_id")
    @ColumnInfo(name = "debitor_id")
    var debitorId: Long? = null,
    @SerializedName("debitor_type")
    @ColumnInfo(name = "debitor_type")
    var debitorType: String? = null,
    @SerializedName("debitor_name")
    @ColumnInfo(name = "debitor_name")
    var debitorName: String? = null,
    @SerializedName("creator_id")
    @ColumnInfo(name = "creator_id")
    var creatorId: Long? = null,
    @SerializedName("creator_type")
    @ColumnInfo(name = "creator_type")
    var creatorType: String? = null,
    @SerializedName("owner_id")
    @ColumnInfo(name = "owner_id")
    var ownerId: Long? = null,
    @SerializedName("owner_type")
    @ColumnInfo(name = "owner_type")
    var ownerType: String? = null,
    @SerializedName("account_id")
    @ColumnInfo(name = "account_id")
    var accountId: Long? = null,
    @SerializedName("account_type")
    @ColumnInfo(name = "account_type")
    var accountType: String? = null
//    @SerializedName("uploads")
//    var documents: Document? = null
)

//data class Document(
//    @SerializedName("expense_document")
//    val expenseDocList: List<String>? = null
//)