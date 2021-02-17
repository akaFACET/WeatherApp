package com.example.weatherapp.viewModels

import androidx.lifecycle.*
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.utils.Mapper
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherPerDay
import com.example.weatherapp.data.WeatherPerHour
import com.example.weatherapp.data.WeatherData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherDetailsViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {

    private var _currentWeather = MutableLiveData<WeatherData?>()
    private var _listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    private var _weatherPerHour = MutableLiveData<WeatherPerHour>()
    private val compositeDisposable = CompositeDisposable()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _exception = MutableLiveData<Exceptions>()
    private var cityId: Int = 0


    var currentWeather: LiveData<WeatherData?> = _currentWeather
    var listWeatherPerDay: LiveData<List<WeatherPerDay>?> = _listWeatherPerDay
    var weatherPerHour: LiveData<WeatherPerHour> = _weatherPerHour
    var isLoading: LiveData<Boolean> = _isLoading
    var exception: LiveData<Exceptions> = _exception

    fun updateWeatherPerHour(weatherPerHour: WeatherPerHour) {
        _weatherPerHour.postValue(weatherPerHour)
    }

    fun getWeatherFromDb(id: Int) {
        cityId = id
        compositeDisposable.add(
            weatherRepository.getWeatherDataByCityIdFromDb(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherData ->
                    _currentWeather.postValue(weatherData)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                }, { throwable ->
                    _exception.value = Exceptions.setException(throwable)
                })
        )

    }

    fun refreshWeatherFromInternet() {

        _isLoading.value = true

        compositeDisposable.add(
            weatherRepository.getWeatherByCityId(cityId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { weatherData ->
                    weatherRepository.saveData(weatherData)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherData ->
                    _currentWeather.postValue(weatherData)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                    _isLoading.value = false
                }, { throwable ->
                    _exception.value = Exceptions.setException(throwable)
                    _isLoading.value = false
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}