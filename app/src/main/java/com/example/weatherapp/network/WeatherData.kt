package com.example.weatherapp.network


data class WeatherData(
    val cityId: Int,
    var isLastKnownLocation: Boolean,
    val name: String,
    val country: String,
    val updateDt: String,
    val sunrise: Int,
    val sunset: Int,
    val coordLon: Double,
    val coordLat: Double,
    val subWeather: List<SubWeather>
)

data class SubWeather(
    val id: Int,
    val cityId: Int,
    var isLastKnownLocation: Boolean,
    val weatherId: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val dt: Int,
    val dt_txt: String,
    val mainTemp: Int,
    val mainFeels_like: Double,
    val mainTemp_min: Int,
    val mainTemp_max: Int,
    val mainPressure: Int,
    val mainHumidity: Int,
    val windSpeed: Int,
    val windDeg: Int,
    val cloudsAll: Int,
    val rain: Double,
    val snow: Double
)