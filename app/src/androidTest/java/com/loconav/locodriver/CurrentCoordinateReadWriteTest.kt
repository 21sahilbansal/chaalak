package com.loconav.locodriver

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.room.CurrentCoordinateDao
import com.loconav.locodriver.driver.CurrentCoordinate
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CurrentCoordinateReadWriteTest {
    private lateinit var currentCoordinateDao: CurrentCoordinateDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        currentCoordinateDao = db.currentCoordinateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCoordinateAndReadInList() {
        val currentCoordinate = CurrentCoordinate(lat = 73.4, lng = 78.2, batteryPercentage = 89,locationAvailability = true)
        currentCoordinateDao.insertAll(currentCoordinate)
        currentCoordinate.lat?.let {
            val todoItem = currentCoordinateDao.findByLat(it)
            assertThat(todoItem, equalTo(currentCoordinate))
        }
    }
}