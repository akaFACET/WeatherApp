package com.example.weatherapp.Utils

import java.util.*

object Util {
    fun getDateFromUnixTime(unixTime: Long): String{
        //val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
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
}