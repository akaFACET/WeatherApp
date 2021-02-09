package com.example.weatherapp.di.components

import android.app.Application
import com.example.weatherapp.MainActivity
import com.example.weatherapp.di.modules.AppModule
import com.example.weatherapp.di.modules.BindsViewModelModule
import com.example.weatherapp.di.modules.ViewModelModule
import com.example.weatherapp.di.modules.*
import com.example.weatherapp.fragments.*
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import com.example.weatherapp.viewModels.SearchViewModel
import com.example.weatherapp.viewModels.WeatherDetailsViewModel
import dagger.BindsInstance
import dagger.Component
import java.util.*
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class, LocationModule::class, PreferencesModule::class,
    RepositoryModule::class, NetworkModule::class, BindsViewModelModule::class, ViewModelModule::class,
    AppModule::class, LocaleModule::class])

interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(settingsFragment: SettingsFragment)
    fun inject(savedWeatherFragment: SavedWeatherFragment)
    fun inject(currentWeatherFragment: CurrentWeatherFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(weatherDetailsFragment: WeatherDetailsFragment)

    fun inject(currentWeatherViewModel: CurrentWeatherViewModel)
    fun inject(savedWeatherViewModel: SavedWeatherViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(weatherDetailsViewModel: WeatherDetailsViewModel)
}