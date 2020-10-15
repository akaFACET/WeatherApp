package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Exceptions
import com.example.weatherapp.LoadingStatus
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class SearchViewModel : ViewModel() {

    var searchedWeather =  MutableLiveData<List<FoundCities>>()

    var exception = MutableLiveData<Exceptions>()

    fun searchWeatherByCityName(query:String?){
        if (query == null) return

        exception.value = Exceptions.noException

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val response = WeatherRepository.getWeatherByCity(query)
                    Log.e("ex", "${response}")
                    searchedWeather.postValue(response)
                    if (response.isEmpty()){
                        exception.postValue(Exceptions.noCity)
                    }
                }
            } catch (ex: UnknownHostException) {
                exception.value = Exceptions.noInternet
            } catch (ex: retrofit2.HttpException) {
                exception.value = Exceptions.noCity
            } catch (ex: Throwable) {
                exception.value = Exceptions.others

            }
        }
    }

    fun saveData(foundCities: FoundCities){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val response = WeatherRepository.getWeatherByCityId(foundCities.cityId)
                    WeatherRepository.saveData(response)
                }
            }catch (ex: Throwable){
                Log.e("error", ex.message)
            }
        }
    }

}
