package com.example.weatherapp.data

import androidx.annotation.StringRes
import com.example.weatherapp.R

enum class Exceptions(
    @StringRes val title: Int
) {
    noException(R.string.noException),
    noInternet(R.string.noInternetConnection),
    noGPS(R.string.noGPS),
    noCity(R.string.noCity),
    others(R.string.error)
}