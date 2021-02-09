package com.example.weatherapp.adapters

import com.example.weatherapp.data.WeatherPerHour

interface OnDaysScrollItemClickListener {
    fun onItemClick(weatherPerHour: List<WeatherPerHour>)
}