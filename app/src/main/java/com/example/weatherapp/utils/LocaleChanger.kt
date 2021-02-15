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

    fun overrideLocale(context: Context, locale: Locale) {

        Locale.setDefault(locale)

        val newConfig = Configuration()
        newConfig.setLocale(locale)

        context.createConfigurationContext(newConfig)

        if (context != context.applicationContext) {
            context.applicationContext.run { createConfigurationContext(newConfig) }
        }
    }
}

//object LocaleChanger {
//
//    fun wrapContext(context: Context, locale: Locale): Context {
//
//        val savedLocale = locale
//
//        Locale.setDefault(savedLocale)
//
//        val newConfig = Configuration()
//        newConfig.setLocale(savedLocale)
//
//        return context.createConfigurationContext(newConfig)
//    }
//
//    fun overrideLocale(context: Context, locale: Locale) {
//
//        val savedLocale = locale
//
//        Locale.setDefault(savedLocale)
//
//        val newConfig = Configuration()
//        newConfig.setLocale(savedLocale)
//
//        context.createConfigurationContext(newConfig)
//
//        if (context != context.applicationContext) {
//            context.applicationContext.run { createConfigurationContext(newConfig) }
//        }
//    }
//}