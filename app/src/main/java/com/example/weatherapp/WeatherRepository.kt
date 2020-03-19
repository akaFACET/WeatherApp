package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.db.WeatherDB
import com.example.weatherapp.network.NetworkModule
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.*
import java.lang.Exception

object WeatherRepository {

    private val appid="b7f532dcad2190c9ee565b091e2d8290"
    private val language = "ru"
    private val units = "metric"
    var isLoading = MutableLiveData<Boolean>()

    val db = WeatherDB.getInstance(App.instance).getSavedWeatherDAO()

    val weatherApiService = NetworkModule.weatherApiService

    fun saveData(weatherData: WeatherData){

        db.saveData(weatherData)
    }

    fun deleteData(weatherData: WeatherData){
        db.delete(weatherData)
    }


    suspend fun getWeatherByCity(query: String):List<WeatherData>{
        val result = weatherApiService.getWeatherByCity(appid, units, language,query)
        result.await()
        return Mapper.mapWeatherResponseToWeatherData(
            result.getCompleted().list?: emptyList())
    }

    suspend fun getWeatherByCoord(lat:Double, lon:Double):WeatherData{
        val result = weatherApiService.getWeatherByCoord(appid, units, language,lat = lat, lon = lon)
        result.await()
        return Mapper.mapWeatherListToWeatherData(
            result.getCompleted()
        )
    }

    suspend fun getWeatherByCityId(id: Int):WeatherData{
        val result = weatherApiService.getWeatherByCityId(appid, units, language,id)
        result.await()
        return Mapper.mapWeatherListToWeatherData(
            result.getCompleted())
    }


    private fun getIds(): List<Int>{
        val tmp = GlobalScope.async(Dispatchers.IO) {
            db.getIds()
        }
        val result = runBlocking {
            tmp.await()
        }
        return result
    }

    fun update(){
        isLoading.postValue(false)
        val ids = getIds()
        if (!ids.isEmpty()){
            val result = mutableListOf<WeatherData>()
                GlobalScope.launch {
                    ids.map {
                        async(Dispatchers.IO) {
                            try {
                                val tmp: WeatherData = getWeatherByCityId(it)
                                result.add(tmp)
                            }catch (ex:Throwable){
                                isLoading.postValue(false)
                            }
                        }
                    }.awaitAll()
                    db.update(result)
                    isLoading.postValue(false)
                }
        }else
            isLoading.postValue(false)
    }
}