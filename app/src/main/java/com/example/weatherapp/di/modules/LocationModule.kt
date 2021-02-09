package com.example.weatherapp.di.modules

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.example.weatherapp.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient {
        return  LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationManager(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideLocation(
        context: Context,
        locationManager: LocationManager,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): Location {
        return Location(context,fusedLocationProviderClient, locationManager)
    }
}