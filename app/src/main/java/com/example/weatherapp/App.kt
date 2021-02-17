package com.example.weatherapp

import android.app.Application
import android.content.Context
import com.example.weatherapp.di.components.ApplicationComponent
import com.example.weatherapp.di.components.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initApplicationComponent()
    }

    private fun initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .factory()
            .create(this)

    }

    companion object {
        fun get(context: Context): App {
            return context.applicationContext as App
        }
    }

}