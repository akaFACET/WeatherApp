package com.example.weatherapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val currentDate: String,
    val coordLon: Double,
    val coordLat: Double,
    val weatherId: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val base: String,
    val mainTemp: Int,
    val mainFeels_like: Double,
    val mainTemp_min: Int,
    val mainTemp_max: Int,
    val mainPressure: Int,
    val mainHumidity: Int,
    val visability: Int,
    val windSpeed: Int,
    val windDeg: Int,
    val cloudsAll: Int,
    val dt: Int,
    val sysType: Int,
    val sysId: Int,
    val sysCountry: String,
    val sysSunrise: Int,
    val sysSunset: Int,
    val timezone: Int,
    val name: String,
    val cod: Int
)