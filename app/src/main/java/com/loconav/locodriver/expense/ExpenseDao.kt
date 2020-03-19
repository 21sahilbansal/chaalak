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

    @Query("SELECT * FROM expense WHERE expense_Id = :expenseId")
    fun findByExpenseId(expenseId: String): LiveData<Expense>

    @Query("SELECT * FROM expense WHERE fake_id = :expenseId")
    fun findByFakeExpenseId(expenseId: String): LiveData<Expense>

    @Query("SELECT expense_Id FROM expense WHERE fake_id = :autoId")
    fun getExpenseIDFromAutoId(autoId: String): Long

    @Query("DELETE FROM expense WHERE fake_id = :fakeId ")
    fun delete(fakeId:String):Int

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