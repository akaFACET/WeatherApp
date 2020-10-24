package com.example.weatherapp

import android.os.Build
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate

enum class NightModeType(
    val customOrdinal: Int,
    @AppCompatDelegate.NightMode val value: Int,
    val title: String
    //@StringRes val title: Int

) {
    MODE_NIGHT_NO(
        0,
        AppCompatDelegate.MODE_NIGHT_NO,
        "Выключен"
    ),

    MODE_NIGHT_YES(
        1,
        AppCompatDelegate.MODE_NIGHT_YES,
        "Включен"

    ),

    MODE_NIGHT_FOLLOW_SYSTEM(
        2,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        "Авто"
    ),
    MODE_NIGHT_AUTO_BATTERY(
        2,
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
        "Авто"
    );

    companion object {
        fun fromValue(@AppCompatDelegate.NightMode value: Int) = values().firstOrNull {
            it.value == value
        } ?: getDefaultMode()

        fun fromCustomOrdinal(ordinal: Int): NightModeType {
            return if (ordinal == 2){
                getDefaultMode()
            }else{
                values().firstOrNull { it.customOrdinal == ordinal } ?: getDefaultMode()
            }
        }

        fun getDefaultMode(): NightModeType {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MODE_NIGHT_FOLLOW_SYSTEM
            }else{
                MODE_NIGHT_AUTO_BATTERY
            }
        }
    }


}


