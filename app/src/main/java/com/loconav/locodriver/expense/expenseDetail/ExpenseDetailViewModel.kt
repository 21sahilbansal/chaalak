package com.loconav.locodriver.expense.expenseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expenses.ExpenseRepo

class ExpenseDetailViewModel:ViewModel() {
    val expenseRepo=ExpenseRepo()
    fun getIndividualExpense(expenseId:Long):LiveData<DataWrapper<Expense>>{
        return expenseRepo.getExpense(expenseId)
    }

    fun getIndividualExpenseFromDb(expenseId: Long):LiveData<Expense>{
        return expenseRepo.getExpenseFromDb(expenseId)
    }
}