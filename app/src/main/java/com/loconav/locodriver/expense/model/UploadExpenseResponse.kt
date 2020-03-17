package com.loconav.locodriver.expense.model

import com.google.gson.annotations.SerializedName

data class UploadExpenseResponse(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var successMessage: String? = null,
    @SerializedName("obj")
    var expense: Expense? = null
)