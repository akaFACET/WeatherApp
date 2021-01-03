package com.example.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.network.SubWeather
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.network.WeatherDataEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface WeatherDAO {

    @Insert(onConflict = REPLACE)
    fun saveCitiesEntity(citiesEntity: CitiesEntity)

    @Insert(onConflict = REPLACE)
    fun saveSubWeatherEntity(subWeatherEntity: SubWeatherEntity)

    @Transaction
    fun saveWeatherData(weatherData: WeatherData) {
        if (!weatherData.isLastKnownLocation) {
            saveCitiesEntity(Mapper.mapWeatherDataToCitiesEntity(weatherData))
            for (weather: SubWeather in weatherData.subWeather) {
                saveSubWeatherEntity(Mapper.mapSubWeatherToSubWeatherEntity(weather))
            }
        }
    }

    @Transaction
    fun saveLastKnownLocation(weatherData: WeatherData) {
        if (weatherData.isLastKnownLocation) {
            saveCitiesEntity(Mapper.mapWeatherDataToCitiesEntity(weatherData))
            for (weather: SubWeather in weatherData.subWeather) {
                saveSubWeatherEntity(Mapper.mapSubWeatherToSubWeatherEntity(weather))
            }
        }
    }

    @Delete
    fun deleteCitiesEntity(citiesEntity: CitiesEntity)

    @Delete
    fun deleteSubWeatherEntity(subWeatherEntity: List<SubWeatherEntity>)

    @Query("DELETE FROM CitiesEntity WHERE isLastKnownLocation = 1")
    fun deleteLastKnownCitiesEntity()

    @Query("DELETE FROM SubWeatherEntity WHERE isLastKnownLocation = 1")
    fun deleteLastKnownSubWeatherEntity()

    @Transaction
    fun delete(weatherData: WeatherData) {
        deleteCitiesEntity(Mapper.mapWeatherDataToCitiesEntity(weatherData))
        deleteSubWeatherEntity(Mapper.mapSubWeatherToSubWeatherEntity(weatherData.subWeather))
    }

    @Transaction
    fun deleteLastKnownWeather() {
        deleteLastKnownCitiesEntity()
        deleteLastKnownSubWeatherEntity()
    }

    @Transaction
    @Query("SELECT * FROM CitiesEntity WHERE isLastKnownLocation = 1")
    fun getLastKnownWeatherData(): Single<WeatherDataEntity>

    @Transaction
    @Query("SELECT * FROM CitiesEntity WHERE isLastKnownLocation = 0")
    fun getAllWeatherData(): LiveData<List<WeatherDataEntity>>

    @Transaction
    @Query("SELECT * FROM CitiesEntity WHERE cityId = :cityId ")
    fun getWeatherDataByCityId(cityId: Int): Single<WeatherDataEntity>

}