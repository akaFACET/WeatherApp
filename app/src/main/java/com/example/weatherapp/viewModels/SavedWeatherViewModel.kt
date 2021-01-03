package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.network.WeatherData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedWeatherViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val data = MutableLiveData<List<WeatherData>>()

//    fun deleteData(weatherData: WeatherData) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                WeatherRepository.deleteData(weatherData)
//            }
//        }
//    }
    fun deleteData(weatherData: WeatherData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                WeatherRepository.deleteData(weatherData)
            }
        }
    compositeDisposable.add(
        Completable.fromAction{WeatherRepository.deleteData(weatherData)}
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({

        },{

        }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
