package com.loconav.locodriver.expense

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense")
    fun getAllExpense(): LiveData<List<Expense>>

    @Query("SELECT * FROM expense WHERE expense_Id = :expenseId")
    fun findByExpenseId(expenseId: Long): LiveData<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expense: Expense)

    @Query("DELETE FROM expense WHERE expense_Id = :expenseId")
    fun deleteSingleExpense(expenseId: Long)

    @Delete
    fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expense")
    fun deleteAll()

    @Update
    fun updateExpense(vararg expense: Expense)

}