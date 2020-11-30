package com.example.weatherapp.Utils

import android.content.Context
import android.content.res.Configuration
import java.util.*


object LocaleChanger {

    fun wrapContext(context: Context, locale: Locale): Context {

        val savedLocale = locale

        Locale.setDefault(savedLocale)

        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        return context.createConfigurationContext(newConfig)
    }

    fun overrideLocale(context: Context, locale: Locale) {

        val savedLocale = locale

        Locale.setDefault(savedLocale)

        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        context.createConfigurationContext(newConfig)

        if (context != context.applicationContext) {
            context.applicationContext.run { createConfigurationContext(newConfig) }
        }
    }
}