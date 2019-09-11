package com.loconav.locodriver.db.room

import androidx.room.*
import com.loconav.locodriver.driver.CurrentCoordinate


@Dao
interface CurrentCoordinateDao {

    @Query("SELECT * FROM coordinates")
    fun getAll(): List<CurrentCoordinate>

    @Query("SELECT * FROM coordinates WHERE created_at > :time")
    fun findByTime(time: Long): CurrentCoordinate

    @Query("SELECT * FROM coordinates WHERE lat = :lat")
    fun findByLat(lat: Double): CurrentCoordinate




    @Insert
    fun insertAll(vararg currentCoordinate: CurrentCoordinate)

    @Delete
    fun delete(currentCoordinate: CurrentCoordinate)

    @Update
    fun updateCurrentCoordinate(vararg currentCoordinate: CurrentCoordinate)


}