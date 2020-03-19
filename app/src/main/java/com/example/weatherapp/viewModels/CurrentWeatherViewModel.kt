package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.network.WeatherData

class CurrentWeatherViewModel : ViewModel() {

    var currentWeather = MutableLiveData<WeatherData>()

    override fun onCleared() {
        super.onCleared()
        Log.e("err","onCleared CurrentWeatherViewModel")
    }


}


