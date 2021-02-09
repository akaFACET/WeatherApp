package com.example.weatherapp.utils

import java.util.*

object TimeFormatter {

    fun getDateFromUnixTime(unixTime: Long): String{
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000)
        return sdf.format(date)
    }

    fun getTimeFromUnixTime(unixTime: Long): String{
        val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(unixTime.toLong()*1000)
        return sdf.format(date)
    }

    fun getDMFromUnixTime(unixTime: Long): String{
        val sdf = java.text.SimpleDateFormat("dd.MM", Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000)
        return sdf.format(date)
    }

    fun getTempUnits(units: String): String {
        return when(units){
            "metric" -> "°C"
            "imperial" -> "°F"
            "" -> "K"
            else -> ""
        }
    }
}