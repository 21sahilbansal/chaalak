package com.loconav.locodriver.expense

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.loconav.locodriver.expense.model.Expense

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense")
    fun getAllExpense(): LiveData<List<Expense>>

    @Query("SELECT * FROM expense")
    fun getExpenses(): List<Expense>

    @Query("SELECT * FROM expense WHERE auto_id = :expenseId")
    fun findByExpenseId(expenseId: Long): LiveData<Expense>

    @Query("SELECT expense_Id FROM expense WHERE auto_id = :autoId")
    fun getExpenseIDFromAutoId(autoId: Long): Long

    @Query("DELETE FROM expense WHERE auto_id = :autoId ")
    fun delete(autoId:Int):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expense: Expense)

    @Delete
    fun deleteExpense(expense: Expense):Int

    @Query("DELETE FROM expense")
    fun deleteAll()

    @Update
    fun updateExpense(vararg expense: Expense)

    @Query("SELECT * FROM expense WHERE expense_Id = null")
    fun findUnsyncedExpenseList(): List<Expense>

}