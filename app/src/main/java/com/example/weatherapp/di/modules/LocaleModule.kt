package com.example.weatherapp.di.modules

import com.example.weatherapp.utils.LocaleChanger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocaleModule {

    @Provides
    @Singleton
    fun provideLocaleChanger(): LocaleChanger{
        return LocaleChanger()
    }

}