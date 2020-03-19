package com.example.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.Deferred

@Dao
interface WeatherDAO {

    @Insert(onConflict = REPLACE)
    fun saveData(weatherData: WeatherData)

    @Delete
    fun delete(weatherData: WeatherData)

    @Query("SELECT * FROM WeatherData")
    fun getData(): LiveData<List<WeatherData>>

    @Query("SELECT id FROM WeatherData")
    fun getIds(): List<Int>

    @Update
    fun update(weatherData: List<WeatherData>)

}