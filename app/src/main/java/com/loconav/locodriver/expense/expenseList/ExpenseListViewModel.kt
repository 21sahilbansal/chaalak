package com.loconav.locodriver.expense.expenseList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expenses.ExpenseRepo

class ExpenseListViewModel : ViewModel() {
    private val expenseRepo = ExpenseRepo()
    fun getExpenseListFromDb(): LiveData<List<Expense>> {
        return expenseRepo.getExpenseListFromDb()
    }

    fun getFetchExpenseList(page: Int): LiveData<DataWrapper<List<Expense>>>? {
        return expenseRepo.getExpenseList(page)
    }
}