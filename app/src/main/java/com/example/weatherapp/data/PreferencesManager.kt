package com.example.weatherapp.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "weatherAppSettings"
        private const val KEY_NIGHT_MODE = "NIGTHT_MODE"
        private const val KEY_UNITS = "UNITS"
        private const val KEY_LANG = "LANG"
        private const val KEY_COUNTRY = "COUNTRY"
    }

    private var mPref: SharedPreferences

    init {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getSavedNightModeValue(): Int{
        return mPref.getInt(KEY_NIGHT_MODE, NightModeType.getDefaultMode().value)
    }

    fun saveNightModeValue(nightMode: Int){
        mPref.edit().putInt(KEY_NIGHT_MODE,nightMode).apply()
    }

    fun getSavedUnitsValue(): String?{
        return mPref.getString(KEY_UNITS, UnitsType.getDefaultUnits().value)
    }

    fun saveUnitsValue(units: String){
        mPref.edit().putString(KEY_UNITS,units).apply()
    }

    fun getSavedLanguage(): String{
        return mPref.getString(KEY_LANG, Language.getDefaultLanguage().value)!!
    }

    fun saveLanguageValue(lang: String){
        mPref.edit().putString(KEY_LANG, lang).apply()
    }

    fun getSavedCountry(): String{
        return mPref.getString(KEY_COUNTRY, Language.getDefaultLanguage().country)!!
    }
    fun saveCountry(country: String){
        mPref.edit().putString(KEY_COUNTRY,country).apply()
    }
}
