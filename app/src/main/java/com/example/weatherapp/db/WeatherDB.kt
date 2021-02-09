package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CitiesEntity::class, SubWeatherEntity::class), version = 2)

abstract class WeatherDB : RoomDatabase() {
    abstract fun getSavedWeatherDAO(): WeatherDAO
}