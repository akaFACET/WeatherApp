package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "weatherAppSettings"
        private const val KEY_NIGHT_MODE = "NIGTHT_MODE"
        private const val KEY_UNITS = "UNITS"

    }

    private var mPref: SharedPreferences

    init {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getSavedNightModeValue(): Int{
        return mPref.getInt(KEY_NIGHT_MODE, 0)
    }

    fun saveNightModeValue(nightMode: Int){
        mPref.edit().putInt(KEY_NIGHT_MODE,nightMode).apply()
    }

    fun getSavedUnitsValue(): String?{
        return mPref.getString(KEY_UNITS, "metric") ?: "metric"
    }

    fun saveUnitsValue(units: String){
        mPref.edit().putString(KEY_UNITS,units).apply()
    }


    var value: Long
        get() = mPref.getLong(KEY_NIGHT_MODE, 0)
        set(value) {
            mPref.edit()
                .putLong(KEY_NIGHT_MODE, value)
                .apply()
        }

    fun remove(key: String?) {
        mPref.edit()
            .remove(key)
            .apply()
    }

    fun clear(): Boolean {
        return mPref.edit()
            .clear()
            .commit()
    }
}
