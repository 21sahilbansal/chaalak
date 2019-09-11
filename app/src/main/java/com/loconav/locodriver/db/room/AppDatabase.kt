package com.loconav.locodriver.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loconav.locodriver.driver.CurrentCoordinate

@Database(entities = arrayOf(CurrentCoordinate::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentCoordinateDao(): CurrentCoordinateDao
}