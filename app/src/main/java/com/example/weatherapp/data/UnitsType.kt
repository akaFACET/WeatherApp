package com.example.weatherapp.data

import androidx.annotation.StringRes
import com.example.weatherapp.R


enum class UnitsType(
    val customOrdinal: Int,
    val value: String,
    @StringRes val title: Int
) {
    METRIC_UNITS(
        0,
        "metric",
        R.string.celsius
    ),
    IMPERIAL_UNITS(
        1,
        "imperial",
        R.string.fahrenheit
    ),
    ABSOLUTE_UNITS(
        2,
        "",
        R.string.kelvin
    );

    companion object {
        fun fromValue(value: String) = values().firstOrNull {
            it.value == value
        } ?: getDefaultUnits()

        fun fromCustomOrdinal(ordinal: Int): UnitsType {
            return when (ordinal) {
                0 -> METRIC_UNITS
                1 -> IMPERIAL_UNITS
                2 -> ABSOLUTE_UNITS
                else -> getDefaultUnits()
            }
        }

        fun getDefaultUnits(): UnitsType {
            return METRIC_UNITS
        }
    }
}