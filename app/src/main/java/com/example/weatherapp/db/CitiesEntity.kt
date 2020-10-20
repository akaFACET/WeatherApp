package com.example.weatherapp.db

import androidx.room.Entity


@Entity(primaryKeys = arrayOf("cityId", "isLastKnownLocation"))
data class CitiesEntity(
    val cityId: Int,
    val isLastKnownLocation: Boolean,
    val name: String,
    val country:String,
    val updateDt:Long,
    val sunrise : Int,
    val sunset : Int,
    val coordLon: Double,
    val coordLat: Double
)