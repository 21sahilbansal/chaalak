package com.loconav.locodriver

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expense.ExpenseDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class ExpenseTableReadWriteTest {
    private lateinit var expenseDao: ExpenseDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        expenseDao = db.expenseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCoordinateAndReadInList() {
        val expense = Expense(
            expenseId = 4544
            , expenseRemarks = "testing"
            , expenseDate = 12345454555
            , expenseType = "fooding"
            , amount = 345.5
            , verificationStatus = "pending"
            , tripUniqueId = "loco/00187"
        )

        expenseDao.insertAll(expense)
        expense.expenseId?.let {
            val todoItem = expenseDao.findByExpenseId(it)
//            ViewMatchers.assertThat(todoItem, CoreMatchers.equalTo(expense))
        }
    }
}