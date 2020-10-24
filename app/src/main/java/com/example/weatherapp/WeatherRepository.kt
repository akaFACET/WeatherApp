package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.db.WeatherDB
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.network.NetworkModule
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.*
import java.lang.Exception

object WeatherRepository {

    private val appid="b7f532dcad2190c9ee565b091e2d8290"
    private val language = "ru"
    private var units = "metric"
    private lateinit var preferencesManager: PreferencesManager

    init {
        preferencesManager = PreferencesManager(App.instance)
        //units = preferencesManager.getSavedUnitsValue().toString()
    }


    val db = WeatherDB.getInstance(App.instance).getSavedWeatherDAO()

    val weatherApiService = NetworkModule.weatherApiService

//    fun getDataAndSave(foundCities: FoundCities){
//        GlobalScope.launch {
//            try {
//                withContext(Dispatchers.IO){
//                    val response = WeatherRepository.getWeatherByCityId(foundCities.cityId)
//                    saveData(response)
//                }
//            }catch (ex: Throwable){
//                Log.e("error", ex.message)
//            }
//        }
//    }



    fun saveData(weatherData: WeatherData){
        db.saveWeatherData(weatherData)
    }

    fun saveLastKnownLocation(weatherData: WeatherData){
        db.deleteLastKnownWeather()
        db.saveLastKnownLocation(weatherData)
    }
    fun deleteData(weatherData: WeatherData){
        db.delete(weatherData)
    }

//    fun deleteLastKnownWeather(){
//        db.deleteLastKnownWeather()
//    }


    suspend fun getWeatherDataByCityIdFromDb(cityId:Int):WeatherData{
        return Mapper.mapWeatherDataEntityToWeatherData(db.getWeatherDataByCityId(cityId))
    }

    suspend fun getWeatherByCity(query: String):List<FoundCities>{
        val result = weatherApiService.getWeatherByCity(appid, units, language,query)

        //updateParams()

        result.await()
        return Mapper.mapFoundCitiesResponseToFoundCities(
            result.getCompleted()
        )
    }

    suspend fun getLastKnownWeather(): WeatherData{
        return Mapper.mapWeatherDataEntityToWeatherData(db.getLastKnownWeatherData())
    }

    suspend fun getWeatherByCoord(lat:Double, lon:Double):WeatherData{

        //updateParams()

        val result = weatherApiService.getWeatherByCoord(appid, units, language,lat = lat, lon = lon)
        result.await()
        return Mapper.mapWeatherResponseToWeatherData(
            result.getCompleted()
        )
    }

    suspend fun getWeatherByCityId(id: Int):WeatherData{

        //updateParams()

        val result = weatherApiService.getWeatherByCityId(appid, units, language,id)
        result.await()
        return Mapper.mapWeatherResponseToWeatherData(
            result.getCompleted())
    }

//    private fun updateParams(){
//        units = preferencesManager.getSavedUnitsValue().toString()
//    }

}