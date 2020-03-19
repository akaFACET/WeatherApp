package com.example.weatherapp.adapters

import com.example.weatherapp.network.WeatherData

interface OnItemClickListener {
    fun onItemClick(weatherData: WeatherData)
}