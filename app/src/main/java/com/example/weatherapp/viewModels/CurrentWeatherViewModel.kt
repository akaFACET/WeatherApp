package com.example.weatherapp.viewModels

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.Exceptions
import com.example.weatherapp.Location
import com.example.weatherapp.Mapper
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.UnknownHostException


class CurrentWeatherViewModel(application: Application) : AndroidViewModel(
    application
) {

    private val location = Location(application)
    var currentWeather = MutableLiveData<WeatherData?>()
    var listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    //var weatherPerDay = MutableLiveData<WeatherPerDay?>()
    var isLoading = MutableLiveData<Boolean>()
    var exception = MutableLiveData<Exceptions>()
    //var currentLocation = MediatorLiveData<LocationData>()
    //var currentLocation = location.currentLocation

//    init {
//        location.currentLocation.observeForever(Observer {
//            Log.e("err", "observeForever $it")
//        })
////        currentLocation.addSource(location.currentLocation, Observer {
////            currentLocation.value = it
////            Log.e("err", "${it.latitude},${it.longitude}")
////            Log.e("err", "getLastLocation()")
////            getWeatherByLocation(it.latitude, it.longitude)
////        })
//    }


    init {
        getLastKnownLocation()
        location.currentLocation.observeForever(Observer {
            Log.e("err", "observeForever ${it.latitude}, ${it.longitude}")
            getWeatherByLocation(it.latitude,it.longitude)
        })
    }

    fun getLocation() {
        isLoading.value = true
        exception.value = Exceptions.noException
        location.getLastLocation()
    }


    private fun getLastKnownLocation(){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val result = WeatherRepository.getLastKnownWeather()
                    currentWeather.postValue(result)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(result.subWeather))
                    //weatherPerDay.postValue(Mapper.getWeatherPerDays(result.subWeather)[0])
                }
            } catch (ex: Exception){
                Log.e("getLastKnownLocation", ex.toString())
            }
        }
    }

    private fun getWeatherByLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            exception.value = Exceptions.noException
            try {
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCoord(lat, lon)
                    response.isLastKnownLocation = true
                    response.subWeather.forEach {
                        it.isLastKnownLocation=true
                    }
                    WeatherRepository.saveLastKnownLocation(response)
                    currentWeather.postValue(response)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(response.subWeather))
                    //weatherPerDay.postValue(Mapper.getWeatherPerDays(response.subWeather)[0])
                }
                isLoading.value = false
            }catch (ex: UnknownHostException){
                Log.e("ex1", ex.toString())
                Log.e("ex1", "noInternet")
                exception.value = Exceptions.noInternet
                isLoading.value = false
            }catch (ex: Throwable) {
                Log.e("ex1", ex.toString())
                Log.e("ex1", "others")
                exception.value = Exceptions.others
                isLoading.value = false
            }

        }
    }

    override fun onCleared() {
        Log.e("RefreshError", "CurrentWeatherViewModel has destroy")
        super.onCleared()
    }

}


