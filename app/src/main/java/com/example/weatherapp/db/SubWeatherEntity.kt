package com.example.weatherapp.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CitiesEntity::class,
        parentColumns = ["cityId", "isLastKnownLocation"],
        childColumns = ["cityId", "isLastKnownLocation"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class SubWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cityId:Int,
    var isLastKnownLocation: Boolean,
    val weatherId: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val dt:Long,
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
    val snow: Double,
    val rain: Double,
    val units: String

)