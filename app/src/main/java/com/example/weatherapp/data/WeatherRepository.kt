package com.example.weatherapp.data

import android.util.Log
import com.example.weatherapp.App
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.db.WeatherDB
import com.example.weatherapp.network.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
        return db.delete(weatherData)
    }

    fun getWeatherDataByCityIdFromDb(cityId: Int): Single<WeatherData> {
        return db.getWeatherDataByCityId(cityId).map { weatherDataEntity ->
            Mapper.mapWeatherDataEntityToWeatherData(weatherDataEntity)
        }
    }

    fun getWeatherByCity(query: String): Single<List<FoundCities>> {
        return weatherApiService.getWeatherByCity(appid, units, language, query)
            .map { foundCitiesResponse ->
                Mapper.mapFoundCitiesResponseToFoundCities(foundCitiesResponse)
            }
    }

    fun getLastKnownWeather(): Single<WeatherData> {
        return db.getLastKnownWeatherData().map { weatherDataEntity ->
            Mapper.mapWeatherDataEntityToWeatherData(weatherDataEntity)
        }
    }

    fun getWeatherByCoord(lat: Double, lon: Double): Single<WeatherData> {
        updateParams()
        return weatherApiService.getWeatherByCoord(appid, units, language, lat = lat, lon = lon)
            .map { weatherResponse ->
                Mapper.mapWeatherResponseToWeatherData(weatherResponse, units)
            }
    }

    fun getWeatherByCityId(id: Int): Single<WeatherData> {
        updateParams()
        return weatherApiService.getWeatherByCityId(appid, units, language, id)
            .map { weatherResponse ->
                Mapper.mapWeatherResponseToWeatherData(weatherResponse, units)
            }
    }

    private fun updateParams() {
        units = preferencesManager.getSavedUnitsValue().toString()
        language = preferencesManager.getSavedLanguage()
    }

}