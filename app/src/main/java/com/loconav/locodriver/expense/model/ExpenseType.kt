package com.loconav.locodriver.expense.model

import com.google.gson.annotations.SerializedName

data class ExpenseType(
    @SerializedName("data")
    val expenseType: HashMap<String, String>? = null
)