package com.example.weatherapp.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.viewModels.WeatherViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: WeatherViewModelFactory)
            :ViewModelProvider.Factory
}