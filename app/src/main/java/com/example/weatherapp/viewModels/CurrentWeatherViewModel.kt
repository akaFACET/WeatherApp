package com.example.weatherapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.location.Location
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.adapters.WeatherPerHour
import com.example.weatherapp.location.LocationData
import com.example.weatherapp.network.WeatherData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

class CurrentWeatherViewModel(application: Application) : AndroidViewModel(
    application
) {
    private val location = Location(application)
    private var _currentWeather = MutableLiveData<WeatherData?>()
    private var _listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    private var _weatherPerHour = MutableLiveData<WeatherPerHour>()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _exception = MutableLiveData<Exceptions>()
    private val compositeDisposable = CompositeDisposable()

    private val locationObserver: Observer<LocationData> = Observer { locationData ->
        getWeatherByLocation(locationData.latitude, locationData.longitude)
    }

    var currentWeather: LiveData<WeatherData?> = _currentWeather
    var listWeatherPerDay: LiveData<List<WeatherPerDay>?> = _listWeatherPerDay
    var weatherPerHour: LiveData<WeatherPerHour> = _weatherPerHour
    var isLoading: LiveData<Boolean> = _isLoading
    var exception: LiveData<Exceptions> = _exception

    init {
        getLastKnownLocation()
        location.currentLocation.observeForever(locationObserver)
    }

    fun updateWeatherPerHour(weatherPerHour: WeatherPerHour){
        _weatherPerHour.postValue(weatherPerHour)
    }

    fun getLocation() {
        _isLoading.value = true
        _exception.value = Exceptions.noException
        location.getLastLocation()
    }

    private fun getLastKnownLocation() {
        compositeDisposable.add(
            WeatherRepository.getLastKnownWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherData ->
                    _currentWeather.postValue(weatherData)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])

                },
                    { throwable ->
                    })
        )
    }

    private fun getWeatherByLocation(lat: Double, lon: Double) {
        _exception.value = Exceptions.noException
        compositeDisposable.add(WeatherRepository.getWeatherByCoord(lat, lon)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { weatherData ->
                weatherData.isLastKnownLocation = true
                weatherData.subWeather.forEach {
                    it.isLastKnownLocation = true
                }
                WeatherRepository.saveLastKnownLocation(weatherData)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ weatherData ->
                _currentWeather.postValue(weatherData)
                _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                _isLoading.value = false
            }, {throwable->
                when (throwable){
                    is UnknownHostException -> {
                        _exception.value = Exceptions.noInternet
                        _isLoading.value = false
                    }
                    else -> {
                        _exception.value = Exceptions.others
                        _isLoading.value = false
                    }
                }
            })
            )
    }

    override fun onCleared() {
        super.onCleared()
        location.currentLocation.removeObserver(locationObserver)
        compositeDisposable.clear()
    }
}

