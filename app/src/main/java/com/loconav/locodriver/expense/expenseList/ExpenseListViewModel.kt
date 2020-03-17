package com.loconav.locodriver.expense.expenseList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expenses.ExpenseRepo

class ExpenseListViewModel : ViewModel() {
    fun getExpenseListFromDb(): LiveData<List<Expense>> {
        return ExpenseRepo.getExpenseListFromDb()
    }

    fun getFetchExpenseList(page: Int): LiveData<DataWrapper<List<Expense>>>? {
        return ExpenseRepo.getExpenseList(page)
    }
}