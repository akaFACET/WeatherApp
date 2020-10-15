package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.example.weatherapp.viewModels.SettingsViewModel


class CurrentWeatherViewModelFactory(private val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(application) as T
    }
}



