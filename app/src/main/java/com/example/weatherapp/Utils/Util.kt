package com.example.weatherapp.Utils

import java.util.*

object Util {
    fun getDataFromUnixTime(unixTime: Int): String{
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000)
        return sdf.format(date)
    }
    fun getTimeFromUnixTime(unixTime: Int): String{
        val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000)
        return sdf.format(date)
    }
    fun getMDFromUnixTime(unixTime: Int): String{
        val sdf = java.text.SimpleDateFormat("mm-dd", Locale.getDefault())
        val date = Date(unixTime.toLong() * 1000)
        return sdf.format(date)
    }
}