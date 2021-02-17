package com.example.weatherapp.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoundCitiesResponse(

    @Json(name = "message")
    val message: String?,
    @Json(name = "cod")
    val cod: Int?,
    @Json(name = "count")
    val count: Int?,
    @Json(name = "list")
    val CitiesList: List<CitiesList>?
)

@JsonClass(generateAdapter = true)
data class CitiesList(

    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "coord")
    val coord: Coord,
    @Json(name = "main")
    val main: Main,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "sys")
    val sys: SysC
)

@JsonClass(generateAdapter = true)
data class SysC(
    @Json(name = "country")
    val country: String
)