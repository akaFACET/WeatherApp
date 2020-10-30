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
    var isLoading = MutableLiveData<Boolean>()
    var exception = MutableLiveData<Exceptions>()

    init {
        getLastKnownLocation()
        location.currentLocation.observeForever(Observer {
            getWeatherByLocation(it.latitude, it.longitude)
        })
    }

    fun getLocation() {
        isLoading.value = true
        exception.value = Exceptions.noException
        location.getLastLocation()
    }

    private fun getLastKnownLocation() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val result = WeatherRepository.getLastKnownWeather()
                    currentWeather.postValue(result)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(result))
                }
            } catch (ex: Exception) {

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
                        it.isLastKnownLocation = true
                    }
                    WeatherRepository.saveLastKnownLocation(response)
                    currentWeather.postValue(response)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(response))
                }
                isLoading.value = false
            } catch (ex: UnknownHostException) {
                exception.value = Exceptions.noInternet
                isLoading.value = false
            } catch (ex: Throwable) {
                exception.value = Exceptions.others
                isLoading.value = false
            }
        }
    }

}


