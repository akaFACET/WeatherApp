package com.example.weatherapp.viewModels

import androidx.lifecycle.*
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SavedWeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    init {
        getWeatherData()
    }

    private var _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>> = _weatherData

    private fun getWeatherData(){
        compositeDisposable.add(
            weatherRepository.getAllWeatherData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherData ->
                    _weatherData.value = weatherData
                },{
                })
        )
    }

    fun deleteData(weatherData: WeatherData) {
    compositeDisposable.add(
        Completable.fromAction{weatherRepository.deleteData(weatherData)}
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
