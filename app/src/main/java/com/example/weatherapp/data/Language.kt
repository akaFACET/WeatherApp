package com.example.weatherapp.data

import androidx.annotation.StringRes
import com.example.weatherapp.R

enum class Language(
    val customOrdinal: Int,
    val value: String,
    val country: String,
    @StringRes val title: Int
) {
    ENGLISH(
        0,
        "en",
        "US",
        R.string.english
    ),
    RUSSIAN(
        1,
        "ru",
        "RU",
        R.string.russian
    ),
    BELARUSIAN(
        2,
        "be",
        "BY",
        R.string.belarusian
    ),
    UKRAINIAN(
        3,
        "uk",
        "UA",
        R.string.ukrainian
    );

    companion object {
        fun fromValue(value: String) = values().firstOrNull {
            it.value == value
        } ?: getDefaultLanguage()

        fun fromCustomOrdinal(ordinal: Int): Language {
            return when (ordinal) {
                0 -> ENGLISH
                1 -> RUSSIAN
                2 -> BELARUSIAN
                3 -> UKRAINIAN
                else -> getDefaultLanguage()
            }
        }

        fun getDefaultLanguage(): Language {
            return ENGLISH
        }
    }
}