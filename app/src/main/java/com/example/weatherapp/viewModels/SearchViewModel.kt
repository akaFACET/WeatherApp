package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.FoundCities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {

    private var _exception = MutableLiveData<Exceptions>()
    private val compositeDisposable = CompositeDisposable()

    var searchedWeather = MutableLiveData<List<FoundCities>>()
    var exception: LiveData<Exceptions> = _exception


    fun searchWeatherByCityName(query: String?) {

        if (query == null) return

        _exception.value = Exceptions.NoException

        compositeDisposable.add(
            weatherRepository.getWeatherByCity(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ foundCities ->
                    searchedWeather.postValue(foundCities)
                    if (foundCities.isEmpty()) {
                        _exception.postValue(Exceptions.NoCity)
                    }
                }, { throwable ->
                    _exception.value = Exceptions.setException(throwable)
                })
        )
    }

    fun saveData(foundCities: FoundCities) {
        compositeDisposable.add(
            weatherRepository.getWeatherByCityId(foundCities.cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ response ->
                    weatherRepository.saveData(response)
                }, { throwable ->
                    Log.e("savingDataError", "${throwable.message}")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
