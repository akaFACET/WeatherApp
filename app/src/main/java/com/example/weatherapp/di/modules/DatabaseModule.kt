package com.example.weatherapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.db.WeatherDAO
import com.example.weatherapp.db.WeatherDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDB(context: Context): WeatherDB {
        return Room.databaseBuilder(
            context,
            WeatherDB::class.java,
            "WeatherDB.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDB): WeatherDAO {
        return db.getSavedWeatherDAO()
    }

}