package com.example.weatherapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.data.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context):SharedPreferences {
        return context.getSharedPreferences("weatherAppSettings", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(
        sharedPreferences: SharedPreferences
    ): PreferencesManager {
        return PreferencesManager(sharedPreferences)
    }
}