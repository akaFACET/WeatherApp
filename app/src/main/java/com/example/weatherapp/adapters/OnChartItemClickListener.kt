package com.example.weatherapp.adapters

import com.example.weatherapp.data.WeatherPerHour

interface OnChartItemClickListener {
    fun onChartItemClick(weatherPerHour: WeatherPerHour)
}