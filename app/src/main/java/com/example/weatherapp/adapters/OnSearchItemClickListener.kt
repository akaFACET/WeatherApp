package com.example.weatherapp.adapters

import com.example.weatherapp.data.FoundCities

interface OnSearchItemClickListener {
    fun onItemClick(foundCities: FoundCities)
}

