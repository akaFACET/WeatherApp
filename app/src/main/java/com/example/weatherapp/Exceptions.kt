package com.example.weatherapp

import androidx.annotation.StringRes

enum class Exceptions(
    @StringRes val title: Int
) {
    noException(R.string.noException),
    noInternet(R.string.noInternetConnection),
    noGPS(R.string.noGPS),
    noCity(R.string.noCity),
    others(R.string.error)
}