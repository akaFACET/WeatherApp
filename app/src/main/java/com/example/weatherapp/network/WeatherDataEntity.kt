package com.example.weatherapp.network

import androidx.room.Embedded
import androidx.room.Relation
import com.example.weatherapp.db.SubWeatherEntity


data class WeatherDataEntity(
    val cityId: Int,
    val isLastKnownLocation: Boolean,
    val name: String,
    val country:String,
    val updateDt:String,
    val sunrise : Int,
    val sunset : Int,
    val coordLon: Double,
    val coordLat: Double,
    @Relation(parentColumn = "cityId", entityColumn = "cityId", entity = SubWeatherEntity::class)
    val subWeatherEntity: List<SubWeatherEntity>
)