package com.hellu.kabarcuaca.data.repository

import com.hellu.kabarcuaca.data.local.room.WeatherDatabase
import com.hellu.kabarcuaca.entity.WeatherDetails
import com.hellu.kabarcuaca.entity.WeatherResponse
import com.hellu.kabarcuaca.network.ApiServices
import com.hellu.kabarcuaca.network.SafeRequest

class WeatherRepository (
    private val api: ApiServices,
    private val db: WeatherDatabase
) : SafeRequest() {

    suspend fun findCityWeather(cityName: String): WeatherResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherDetail: WeatherDetails) {
        db.getWeatherDao().addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetails? =
        db.getWeatherDao().fetchWeatherByCity(cityName)

    suspend fun fetchAllWeatherDetails(): List<WeatherDetails> =
        db.getWeatherDao().fetchAllWeatherDetails()
}