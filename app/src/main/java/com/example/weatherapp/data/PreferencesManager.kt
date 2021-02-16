package com.example.weatherapp.data

import android.content.SharedPreferences

class PreferencesManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_NIGHT_MODE = "NIGTHT_MODE"
        private const val KEY_UNITS = "UNITS"
        private const val KEY_LANG = "LANG"
        private const val KEY_COUNTRY = "COUNTRY"
    }

    fun getSavedNightModeValue(): Int {
        return sharedPreferences.getInt(KEY_NIGHT_MODE, NightModeType.getDefaultMode().value)
    }

    fun saveNightModeValue(nightMode: Int) {
        sharedPreferences.edit().putInt(KEY_NIGHT_MODE, nightMode).apply()
    }

    fun getSavedUnitsValue(): String? {
        return sharedPreferences.getString(KEY_UNITS, UnitsType.getDefaultUnits().value)
    }

    fun saveUnitsValue(units: String) {
        sharedPreferences.edit().putString(KEY_UNITS, units).apply()
    }

    fun getSavedLanguage(): String {
        return sharedPreferences.getString(KEY_LANG, Language.getDefaultLanguage().value).toString()
    }

    fun saveLanguageValue(lang: String) {
        sharedPreferences.edit().putString(KEY_LANG, lang).apply()
    }

    fun getSavedCountry(): String {
        return sharedPreferences.getString(KEY_COUNTRY, Language.getDefaultLanguage().country).toString()
    }

    fun saveCountry(country: String) {
        sharedPreferences.edit().putString(KEY_COUNTRY, country).apply()
    }
}
