package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.App
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.network.WeatherData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedWeatherViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var weatherRepository: WeatherRepository
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
                weatherRepository.deleteData(weatherData)
            }
        }
    compositeDisposable.add(
        Completable.fromAction{weatherRepository.deleteData(weatherData)}
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
