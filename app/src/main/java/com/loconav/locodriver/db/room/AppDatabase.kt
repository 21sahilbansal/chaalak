package com.loconav.locodriver.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loconav.locodriver.driver.CurrentCoordinate
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expense.ExpenseDao
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import com.loconav.locodriver.expense.DocumentTypeConverter


/**
 * This class is used for accessing lacal db, if db won't be exist then it will initialise db with the following tables mentioned as entities
 */
@Database(entities = [CurrentCoordinate::class, Expense::class], version = 2)
@TypeConverters(DocumentTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currentCoordinateDao(): CurrentCoordinateDao
    abstract fun expenseDao(): ExpenseDao

    companion object {

        private const val DATABASE_NAME = "locodriver.db"
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS 'expense' (`truck_id` INTEGER,`trip_unique_id` TEXT,`amount` REAL,`expense_type` TEXT,`account_type` TEXT,`creator_type` TEXT,`owner_id` INTEGER,`expense_date`INTEGER,`debitor_type`TEXT,`verification_status` TEXT,`account_id`INTEGER,`creditor_name` TEXT,`creator_id`INTEGER,`debitor_name`TEXT,`expense_Id`INTEGER PRIMARY KEY ,`remarks` TEXT, `debitor_id`INTEGER,`owner_type` TEXT)")
            }
        }

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        //       a.invoke(i) ~ a(i)
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}