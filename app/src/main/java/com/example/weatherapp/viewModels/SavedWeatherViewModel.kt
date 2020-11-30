package com.example.weatherapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedWeatherViewModel : ViewModel() {

    val data = MutableLiveData<List<WeatherData>>()

    fun deleteData(weatherData: WeatherData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                WeatherRepository.deleteData(weatherData)
            }
        }
    }

}
