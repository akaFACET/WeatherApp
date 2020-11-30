package com.example.weatherapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SavedWeatherViewModelFactory():ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SavedWeatherViewModel() as T
    }

}