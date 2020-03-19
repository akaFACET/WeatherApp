package com.example.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.network.WeatherData

@Database(entities = [WeatherData::class],version = 1, exportSchema = false)
abstract class WeatherDB : RoomDatabase(){
    companion object {
        private const val DB_NAME: String = "WeatherDB.db"

        var instance: WeatherDB? = null

        fun getInstance(context: Context): WeatherDB {

            if (instance == null) {
                instance = createInstance(context)
            }
            return instance as WeatherDB
        }

        private fun createInstance(context: Context): WeatherDB {
            return Room.databaseBuilder(
                context,
                WeatherDB::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
    abstract fun getSavedWeatherDAO():WeatherDAO
}