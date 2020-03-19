package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {


    var searchedWeather =  MutableLiveData<List<WeatherData>>()

    fun searchWeatherByCityName(query:String?){
        if (query == null) return
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val response = WeatherRepository.getWeatherByCity(query)
                    searchedWeather.postValue(response)
                }
            }catch (ex: Throwable){
                Log.e("error", ex.message)
            }
        }
    }

    fun saveData(weatherData: WeatherData){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    WeatherRepository.saveData(weatherData)
                }
            }catch (ex: Throwable){
                Log.e("error", ex.message)
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.e("err","onCleared SearchViewModel")
    }
}
