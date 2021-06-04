package com.hellu.kabarcuaca.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hellu.kabarcuaca.entity.WeatherDetails

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDetail: WeatherDetails)

    @Query("SELECT * FROM ${WeatherDetails.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): WeatherDetails?

    @Query("SELECT * FROM ${WeatherDetails.TABLE_NAME}")
    suspend fun fetchAllWeatherDetails(): List<WeatherDetails>

}