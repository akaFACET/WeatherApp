package com.example.weatherapp.di.modules

import com.example.weatherapp.data.PreferencesManager
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.db.WeatherDAO
import com.example.weatherapp.network.WeatherApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(preferencesManager: PreferencesManager,
                          weatherApiService: WeatherApiService,
                          db: WeatherDAO
    ): WeatherRepository{
        return WeatherRepository(preferencesManager,weatherApiService,db)
    }
}