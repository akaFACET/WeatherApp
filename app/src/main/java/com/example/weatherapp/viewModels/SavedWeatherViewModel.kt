package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedWeatherViewModel : ViewModel() {

    val data = MutableLiveData<List<WeatherData>>()
    val isUpdate = WeatherRepository.isLoading

    fun deleteData(weatherData: WeatherData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                WeatherRepository.deleteData(weatherData)
            }
        }
    }

    fun update(){
        WeatherRepository.update()
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("err","onCleared SavedViewModel")
    }
}
