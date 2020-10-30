package com.example.weatherapp.adapters

import com.example.weatherapp.network.FoundCities

interface OnSearchItemClickListener {
    fun onItemClick(foundCities: FoundCities)
}

