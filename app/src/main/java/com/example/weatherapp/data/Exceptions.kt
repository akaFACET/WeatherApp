package com.example.weatherapp.data

import androidx.annotation.StringRes
import com.example.weatherapp.R

enum class Exceptions(
    @StringRes val title: Int
) {
    NoException(R.string.noException),
    NoInternet(R.string.noInternetConnection),
    NoGPS(R.string.noGPS),
    NoCity(R.string.noCity),
    Others(R.string.error)
}