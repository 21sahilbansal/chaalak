package com.loconav.locodriver.expense.expenseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expenses.ExpenseRepo

class ExpenseDetailViewModel:ViewModel() {
    fun getIndividualExpense(expenseId:Long):LiveData<DataWrapper<Expense>>{
        return ExpenseRepo.getExpense(expenseId)
    }

    fun getIndividualExpenseFromDb(expenseautoId: Long):LiveData<Expense>{
        return ExpenseRepo.getExpenseFromDb(expenseautoId)
    }

    fun getExpenseIdFromAutoId(autoId:Long):Long?{
        return ExpenseRepo.getExpenseId(autoId)
    }
}