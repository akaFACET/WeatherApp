package com.example.weatherapp.di.components

import android.content.Context
import com.example.weatherapp.MainActivity
import com.example.weatherapp.di.modules.*
import com.example.weatherapp.fragments.CurrentWeatherFragment
import com.example.weatherapp.fragments.SavedWeatherFragment
import com.example.weatherapp.fragments.SettingsFragment
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import com.example.weatherapp.viewModels.SearchViewModel
import com.example.weatherapp.viewModels.WeatherDetailsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class, LocationModule::class, PreferencesModule::class,
    RepositoryModule::class, NetworkModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(settingsFragment: SettingsFragment)
    fun inject(savedWeatherFragment: SavedWeatherFragment)

    fun inject(currentWeatherViewModel: CurrentWeatherViewModel)
    fun inject(savedWeatherViewModel: SavedWeatherViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(weatherDetailsViewModel: WeatherDetailsViewModel)



}