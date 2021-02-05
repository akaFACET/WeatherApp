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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherDetailsViewModel(application: Application, private val cityId: Int) :
    AndroidViewModel(application) {

    private var _currentWeather = MutableLiveData<WeatherData?>()
    private var _listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    private var _weatherPerHour = MutableLiveData<WeatherPerHour>()
    private val compositeDisposable = CompositeDisposable()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _exception = MutableLiveData<Exceptions>()


    @Inject
    lateinit var weatherRepository: WeatherRepository
    var currentWeather: LiveData<WeatherData?> = _currentWeather
    var listWeatherPerDay: LiveData<List<WeatherPerDay>?> = _listWeatherPerDay
    var weatherPerHour: LiveData<WeatherPerHour> = _weatherPerHour
    var isLoading: LiveData<Boolean> = _isLoading
    var exception: LiveData<Exceptions> = _exception

    fun updateWeatherPerHour(weatherPerHour: WeatherPerHour) {
        _weatherPerHour.postValue(weatherPerHour)
    }

    fun getWeatherFromDb() {
        compositeDisposable.add(
            weatherRepository.getWeatherDataByCityIdFromDb(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherData ->
                    _currentWeather.postValue(weatherData)
                    _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                    _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                }, { throwable ->
                    _exception.value = Exceptions.others
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
                    when (throwable) {
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
        compositeDisposable.clear()
    }
}