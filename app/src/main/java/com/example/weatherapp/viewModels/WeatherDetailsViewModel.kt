package com.example.weatherapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.adapters.WeatherPerHour
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class WeatherDetailsViewModel(application: Application, private val cityId: Int) :
    AndroidViewModel(application) {

    private var _currentWeather = MutableLiveData<WeatherData?>()
    private var _listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    private var _weatherPerHour = MutableLiveData<WeatherPerHour>()

    private var _isLoading = MutableLiveData<Boolean>()
    private var _exception = MutableLiveData<Exceptions>()

    var currentWeather: LiveData<WeatherData?> = _currentWeather
    var listWeatherPerDay: LiveData<List<WeatherPerDay>?> = _listWeatherPerDay

    var weatherPerHour: LiveData<WeatherPerHour> = _weatherPerHour


    var isLoading: LiveData<Boolean> = _isLoading
    var exception: LiveData<Exceptions> = _exception

    fun updateWeatherPerHour(weatherPerHour: WeatherPerHour){
        _weatherPerHour.postValue(weatherPerHour)
    }

    fun getWeatherFromDb(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                withContext(Dispatchers.IO){
                    val result = WeatherRepository.getWeatherDataByCityIdFromDb(cityId)
                    _currentWeather.postValue(result)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(result))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(result)[0].weatherPerHour[0])
                }
                _isLoading.value = false
            }catch (ex: Exception){
                Log.e("ex", ex.toString())
                _isLoading.value = false
            }

        }
    }

    fun getWeatherFromInternet() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCityId(cityId)
                    WeatherRepository.saveData(response)
                    _currentWeather.postValue(response)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(response))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(response)[0].weatherPerHour[0])

                }
                _isLoading.value = false
            }catch (ex: UnknownHostException){
                Log.e("ex1", ex.toString())
                Log.e("ex1", "noInternet")
                _exception.value = Exceptions.noInternet
                _isLoading.value = false
            }catch (ex: Throwable) {
                Log.e("ex1", ex.toString())
                Log.e("ex1", "others")
                _exception.value = Exceptions.others
                _isLoading.value = false
            }

        }
    }


    override fun onCleared() {
        Log.e("RefreshError", "DetailsWeatherViewModel has destroy")
        super.onCleared()
    }
}