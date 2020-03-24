package com.example.weatherapp.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.Location
import com.example.weatherapp.LocationData
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentWeatherViewModel(context: Context) : ViewModel() {

    private val location = Location(context)
    var currentWeather = MutableLiveData<WeatherData?>()
    val context = context
    //var currentLocation = MediatorLiveData<LocationData>()

    //var currentLocation = location.currentLocation

    init {
        location.currentLocation.observeForever(Observer {
            Log.e("err", "observeForever $it")
        })
//        currentLocation.addSource(location.currentLocation, Observer {
//            currentLocation.value = it
//            Log.e("err", "${it.latitude},${it.longitude}")
//            Log.e("err", "getLastLocation()")
//            getWeatherByLocation(it.latitude, it.longitude)
//        })
    }

    fun getLocation() {
        Log.e("err", "getLocation")
        location.getLastLocation()
        location.currentLocation.observeForever(Observer {
            Log.e("err", "observeForever $it")
            getWeatherByLocation(it.latitude,it.longitude)
        })
    }


    fun getWeatherByLocation(lat: Double, lon: Double) {
        Log.e("err", "getWeatherByLocation")
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCoord(lat, lon)
                    Log.e("err", "WeatherRepository.getWeatherByCoord(lat,lon)")
                    currentWeather.postValue(response)
                    Log.e("err", response.name)
                }
            }catch (ex: Exception){
                Log.e("ex", ex.toString())
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("err", "onCleared CurrentWeatherViewModel")
    }


}


