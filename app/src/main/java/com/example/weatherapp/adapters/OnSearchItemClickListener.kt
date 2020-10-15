package com.example.weatherapp.adapters

import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.network.WeatherData

interface OnSearchItemClickListener {
    fun onItemClick(foundCities: FoundCities)
}

