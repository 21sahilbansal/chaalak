package com.loconav.locodriver.expense

import com.google.gson.annotations.SerializedName

data class ExpenseType(
    @SerializedName("data")
    val expenseType:List<String>?=null
)