package com.example.weatherapp.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "cod")
    val cod: Int?,
    @Json(name = "count")
    val count: Int?,
    @Json(name = "list")
    val list: List<WeatherList>?
)

@JsonClass(generateAdapter = true)
data class WeatherList(
    @Json(name = "coord")
    val coord: Coord,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "base")
    val base: String?,
    @Json(name = "main")
    val main: Main,
    @Json(name = "visibility")
    val visibility: Int?,
    @Json(name = "wind")
    val wind: Wind,
    @Json(name = "clouds")
    val clouds: Clouds,
    @Json(name = "dt")
    val dt: Int?,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "timezone")
    val timezone: Int?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "cod")
    val cod: Int?
)

@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all")
    val all: Int?
)

@JsonClass(generateAdapter = true)
data class Coord(
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "lat")
    val lat: Double?
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp")
    val temp: Double?,
    @Json(name = "feels_like")
    val feels_like: Double?,
    @Json(name = "temp_min")
    val temp_min: Double?,
    @Json(name = "temp_max")
    val temp_max: Double?,
    @Json(name = "pressure")
    val pressure: Int?,
    @Json(name = "humidity")
    val humidity: Int?
)

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "type")
    val type: Int?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "sunrise")
    val sunrise: Int?,
    @Json(name = "sunset")
    val sunset: Int?
)

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "main")
    val main: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "icon")
    val icon: String?
)

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed")
    val speed: Double?,
    @Json(name = "deg")
    val deg: Int?
)