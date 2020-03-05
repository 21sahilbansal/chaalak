package com.loconav.locodriver.expense.model

import com.google.gson.annotations.SerializedName

data class AddExpenseRequestBody(
    @SerializedName("expense_type")
    var expenseType:String?=null,
    @SerializedName("amount")
    var amount : Int? =null,
    @SerializedName("expense_date")
    var expenseDate : Long?=null,
    @SerializedName("remarks")
    var remarks:String?=null
)