package com.example.weatherapp.adapters

data class WeatherPerDay(
    val day:String,
    val weatherPerHour: List<WeatherPerHour>
)

data class WeatherPerHour(
    val id: Int,
    val cityId: Int,
    val weatherId: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    var dt: Long,
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
    val precipitation: Double
)