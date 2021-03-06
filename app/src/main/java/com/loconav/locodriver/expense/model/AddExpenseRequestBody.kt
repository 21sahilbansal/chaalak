package com.loconav.locodriver.expense.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddExpenseRequestBody(
    @SerializedName("expense_type")
    var expenseType: String? = null,
    @SerializedName("amount")
    var amount: Double? = null,
    @SerializedName("expense_date")
    var expenseDate: Long? = null,
    @SerializedName("remarks")
    var remarks: String? = null,
    var multipartList: List<MultipartBody.Part>? = null,
    var imageList: List<String>? = null
)