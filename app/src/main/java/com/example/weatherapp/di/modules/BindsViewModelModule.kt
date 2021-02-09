package com.example.weatherapp.di.modules

import androidx.lifecycle.ViewModel
import com.example.weatherapp.di.ViewModelKey
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import com.example.weatherapp.viewModels.SearchViewModel
import com.example.weatherapp.viewModels.WeatherDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BindsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CurrentWeatherViewModel::class)
    internal abstract fun currentWeatherViewModel(viewModel: CurrentWeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedWeatherViewModel::class)
    internal abstract fun savedWeatherViewModel(viewModel: SavedWeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherDetailsViewModel::class)
    internal abstract fun weatherDetailsViewModel(viewModel: WeatherDetailsViewModel): ViewModel
}