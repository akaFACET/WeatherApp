package com.example.weatherapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherDetailsViewModelFactory(
    private val application: Application,
    private val cityId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(application, cityId) as T
    }
}