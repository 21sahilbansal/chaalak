package com.loconav.locodriver.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.loconav.locodriver.driver.CurrentCoordinate


/**
 * This class is used for accessing lacal db, if db won't be exist then it will initialise db with the following tables mentioned as entities
 */
@Database(entities = [CurrentCoordinate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currentCoordinateDao(): CurrentCoordinateDao

    companion object {

        private const val DATABASE_NAME = "locodriver.db"

        @Volatile private var instance: AppDatabase? = null

        private val LOCK = Any()

//       a.invoke(i) ~ a(i)
        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
}