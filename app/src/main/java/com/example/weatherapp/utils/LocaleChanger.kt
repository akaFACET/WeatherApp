package com.example.weatherapp.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*


class LocaleChanger {

    fun wrapContext(context: Context, locale: Locale): Context {

        Locale.setDefault(locale)

        val newConfig = Configuration()
        newConfig.setLocale(locale)
        return context.createConfigurationContext(newConfig)
    }

}
