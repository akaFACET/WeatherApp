package com.example.weatherapp.viewModels

import androidx.lifecycle.*
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.location.Location
import com.example.weatherapp.utils.Mapper
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherPerDay
import com.example.weatherapp.data.WeatherPerHour
import com.example.weatherapp.data.LocationData
import com.example.weatherapp.data.WeatherData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrentWeatherViewModel @Inject constructor(private val location: Location,
                                                  private val weatherRepository: WeatherRepository)
    : ViewModel() {

    private var _currentWeather = MutableLiveData<WeatherData?>()
    private var _listWeatherPerDay = MutableLiveData<List<WeatherPerDay>?>()
    private var _weatherPerHour = MutableLiveData<WeatherPerHour>()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _exception = MutableLiveData<Exceptions>()
    private val compositeDisposable = CompositeDisposable()

    private val locationObserver: Observer<LocationData> = Observer { location ->
        getWeatherByLocation(location.latitude, location.longitude)
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

    fun updateWeatherByLocation() {
        _isLoading.value = true
        _exception.value = Exceptions.NoException
        location.getLastLocation()
    }

    fun updateWeatherByNetwork(){
        _isLoading.value = true
        _exception.value = Exceptions.NoException
        currentWeather.value?.cityId?.let { getWeatherByNetwork(it) }
    }

    private fun getLastKnownLocation() {
        compositeDisposable.add(
            weatherRepository.getLastKnownWeather()
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

    fun clearExceptions(){
        _exception.value = Exceptions.NoException
    }

    private fun getWeatherByNetwork(cityId: Int){
        _exception.value = Exceptions.NoException
        compositeDisposable.add(weatherRepository.getWeatherByCityId(cityId)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { weatherData ->
                weatherData.isLastKnownLocation = true
                weatherData.subWeather.forEach {
                    it.isLastKnownLocation = true
                }
                weatherRepository.saveLastKnownLocation(weatherData)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ weatherData ->
                _currentWeather.postValue(weatherData)
                _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                _isLoading.value = false
            }, {throwable->
                _exception.value = Exceptions.setException(throwable)
                _isLoading.value = false
            })
        )
    }
    private fun getWeatherByLocation(lat: Double, lon: Double) {
        _exception.value = Exceptions.NoException
        compositeDisposable.add(weatherRepository.getWeatherByCoord(lat, lon)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { weatherData ->
                weatherData.isLastKnownLocation = true
                weatherData.subWeather.forEach {
                    it.isLastKnownLocation = true
                }
                weatherRepository.saveLastKnownLocation(weatherData)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ weatherData ->
                _currentWeather.postValue(weatherData)
                _listWeatherPerDay.postValue(Mapper.getWeatherPerDays(weatherData))
                _weatherPerHour.postValue(Mapper.getWeatherPerDays(weatherData)[0].weatherPerHour[0])
                _isLoading.value = false
            }, {throwable->
                _exception.value = Exceptions.setException(throwable)
                _isLoading.value = false
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        location.currentLocation.removeObserver(locationObserver)
        compositeDisposable.clear()
    }
}

