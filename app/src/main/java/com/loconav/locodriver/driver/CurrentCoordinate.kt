package com.loconav.locodriver.driver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordinates")
data class CurrentCoordinate(
    @ColumnInfo(name = "lat") var lat: Double?,
    @ColumnInfo(name = "lng") var lng: Double?,
    @ColumnInfo(name = "battery_percentage") var batteryPercentage: Int,
    @ColumnInfo(name = "location_availability") var locationAvailability:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis()
}