package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.example.weatherapp.viewModels.SettingsViewModel


class CurrentWeatherViewModelFactory(private val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(context) as T

    }
}

