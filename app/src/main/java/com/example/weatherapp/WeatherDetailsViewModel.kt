package com.example.weatherapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class WeatherDetailsViewModel(application: Application, private val cityId: Int) :
    AndroidViewModel(application) {

    var currentWeather = MutableLiveData<WeatherData?>()
    var listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    var weatherPerDay = MutableLiveData<WeatherPerDay?>()
    var isLoading = MutableLiveData<Boolean>()
    var exception = MutableLiveData<Exceptions>()

    fun getWeatherFromDb(){
        viewModelScope.launch {
            try {
                isLoading.value = true
                withContext(Dispatchers.IO){
                    val result = WeatherRepository.getWeatherDataByCityIdFromDb(cityId)
                    currentWeather.postValue(result)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(result))
                    weatherPerDay.postValue(Mapper.getWeatherPerDays(result)[0])
                }
                isLoading.value = false
            }catch (ex: Exception){
                Log.e("ex", ex.toString())
                isLoading.value = false
            }

        }
    }

    fun getWeatherFromInternet() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCityId(cityId)
                    WeatherRepository.saveData(response)
                    currentWeather.postValue(response)
                    listWeatherPerDay.postValue(Mapper.getWeatherPerDays(response))
                    weatherPerDay.postValue(Mapper.getWeatherPerDays(response)[0])

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
        Log.e("RefreshError", "DetailsWeatherViewModel has destroy")
        super.onCleared()
    }
}