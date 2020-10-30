package com.example.weatherapp

import com.example.weatherapp.db.WeatherDB
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.network.NetworkModule
import com.example.weatherapp.network.WeatherData

object WeatherRepository {

    private val appid = "b7f532dcad2190c9ee565b091e2d8290"
    private var language = "en"
    private var units = "metric"
    private var preferencesManager: PreferencesManager

    init {
        preferencesManager = PreferencesManager(App.instance)
    }

    val db = WeatherDB.getInstance(App.instance).getSavedWeatherDAO()

    val weatherApiService = NetworkModule.weatherApiService

    fun saveData(weatherData: WeatherData) {
        db.saveWeatherData(weatherData)
    }

    fun saveLastKnownLocation(weatherData: WeatherData) {
        db.deleteLastKnownWeather()
        db.saveLastKnownLocation(weatherData)
    }

    fun deleteData(weatherData: WeatherData) {
        db.delete(weatherData)
    }

    fun getWeatherDataByCityIdFromDb(cityId: Int): WeatherData {
        return Mapper.mapWeatherDataEntityToWeatherData(db.getWeatherDataByCityId(cityId))
    }

    suspend fun getWeatherByCity(query: String): List<FoundCities> {
        val result = weatherApiService.getWeatherByCity(appid, units, language, query)
        result.await()
        return Mapper.mapFoundCitiesResponseToFoundCities(
            result.getCompleted()
        )
    }

    fun getLastKnownWeather(): WeatherData {
        return Mapper.mapWeatherDataEntityToWeatherData(db.getLastKnownWeatherData())
    }

    suspend fun getWeatherByCoord(lat: Double, lon: Double): WeatherData {
        updateParams()
        val result =
            weatherApiService.getWeatherByCoord(appid, units, language, lat = lat, lon = lon)
        result.await()
        return Mapper.mapWeatherResponseToWeatherData(
            result.getCompleted(), units
        )
    }

    suspend fun getWeatherByCityId(id: Int): WeatherData {
        updateParams()
        val result = weatherApiService.getWeatherByCityId(appid, units, language, id)
        result.await()
        return Mapper.mapWeatherResponseToWeatherData(
            result.getCompleted(), units
        )
    }

    private fun updateParams() {
        units = preferencesManager.getSavedUnitsValue().toString()
        language = preferencesManager.getSavedLanguage()
    }

}