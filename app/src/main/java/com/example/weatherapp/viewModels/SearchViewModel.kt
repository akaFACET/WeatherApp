package com.example.weatherapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.network.FoundCities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class SearchViewModel : ViewModel() {

    var searchedWeather = MutableLiveData<List<FoundCities>>()

    var exception = MutableLiveData<Exceptions>()

    fun searchWeatherByCityName(query: String?) {
        if (query == null) return

        exception.value = Exceptions.noException

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCity(query)
                    searchedWeather.postValue(response)
                    if (response.isEmpty()) {
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

    fun saveData(foundCities: FoundCities) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = WeatherRepository.getWeatherByCityId(foundCities.cityId)
                    WeatherRepository.saveData(response)
                }
            } catch (ex: Throwable) {

            }
        }
    }

}
