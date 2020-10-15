package com.example.weatherapp.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("find")
    fun getWeatherByCity(
        @Query("appid") appid: String, @Query("units") units: String,
        @Query("lang") lang: String, @Query("q") q: String
    ): Deferred<FoundCitiesResponse>

    @GET("forecast")
    fun getWeatherByCoord(
        @Query("appid") appid: String, @Query("units") units: String,
        @Query("lang") lang: String, @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<WeatherResponse>

    @GET("forecast")
    fun getWeatherByCityId(
        @Query("appid") appid: String, @Query("units") units: String,
        @Query("lang") lang: String, @Query("id") id: Int
    ): Deferred<WeatherResponse>
}