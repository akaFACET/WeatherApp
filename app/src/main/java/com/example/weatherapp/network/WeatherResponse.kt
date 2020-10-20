package com.example.weatherapp.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "cod")
    val cod: Int?,
    @Json(name = "message")
    val message: Int?,
    @Json(name = "cnt")
    val cnt: Int?,
    @Json(name = "list")
    val list: List<SubWeatherList>?,
    @Json(name = "city")
    val city: City
)

@JsonClass(generateAdapter = true)
data class SubWeatherList (
    @Json(name = "dt")
    val dt : Long?,
    @Json(name = "main")
    val main : Main,
    @Json(name = "weather")
    val weather : List<Weather>?,
    @Json(name = "clouds")
    val clouds : Clouds,
    @Json(name = "wind")
    val wind : Wind,
    @Json(name = "rain")
    val rain : Rain?,
    @Json(name = "snow")
    val snow: Snow?,
    @Json(name = "sys")
    val sys : Sys,
    @Json(name = "dt_txt")
    val dt_txt : String?
)

@JsonClass(generateAdapter = true)
data class Main (
    @Json(name = "temp")
    val temp : Double?,
    @Json(name = "feels_like")
    val feels_like : Double?,
    @Json(name = "temp_min")
    val temp_min : Double?,
    @Json(name = "temp_max")
    val temp_max : Double?,
    @Json(name = "pressure")
    val pressure : Int?,
    @Json(name = "sea_level")
    val sea_level : Int?,
    @Json(name = "grnd_level")
    val grnd_level : Int?,
    @Json(name = "humidity")
    val humidity : Int?,
    @Json(name = "temp_kf")
    val temp_kf : Double?
)

@JsonClass(generateAdapter = true)
data class Sys (
    @Json(name = "pod")
    val pod : String?
)

@JsonClass(generateAdapter = true)
data class Weather (
    @Json(name = "id")
    val id : Int?,
    @Json(name = "main")
    val main : String?,
    @Json(name = "description")
    val description : String?,
    @Json(name = "icon")
    val icon : String?
)

@JsonClass(generateAdapter = true)
data class Coord (
    @Json(name = "lat")
    val lat : Double?,
    @Json(name = "lon")
    val lon : Double?
)

@JsonClass(generateAdapter = true)
data class Clouds (
    @Json(name = "all")
    val all : Int?
)

@JsonClass(generateAdapter = true)
data class City (
    @Json(name = "id")
    val id : Int?,
    @Json(name = "name")
    val name : String?,
    @Json(name = "coord")
    val coord : Coord,
    @Json(name = "country")
    val country : String?,
    @Json(name = "timezone")
    val timezone : Int?,
    @Json(name = "sunrise")
    val sunrise : Int?,
    @Json(name = "sunset")
    val sunset : Int?
)

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed")
    val speed: Double?,
    @Json(name = "deg")
    val deg: Int?
)

@JsonClass(generateAdapter = true)
data class Rain(
    @Json(name = "3h")
    val rainVolume : Double?
)

@JsonClass(generateAdapter = true)
data class Snow(
    @Json(name = "3h")
    val snowVolume: Double?

)