package com.example.weatherapp.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.App
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.function.Consumer

class Location(private val application: Application) {
    lateinit var locationManager: LocationManager

    private var _currentLocation = MutableLiveData<LocationData>()
    var currentLocation: LiveData<LocationData> = _currentLocation
    private var mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application.applicationContext)
    private val GmsStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(application.applicationContext)


    fun getLastLocation() {
        if (GmsStatus == ConnectionResult.SUCCESS){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)

            if (ActivityCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val location: Location? = task.result
                if (location == null) {
                    getLocationFromLocationManager()
                } else {
                    _currentLocation.value = LocationData(location.latitude, location.longitude)
                }
            }
        }else{
            getLocationFromLocationManager()
        }
    }


    private fun getLocationFromLocationManager() {

        locationManager = App.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {

            val providers = locationManager.getAllProviders()

            if (providers.contains(LocationManager.NETWORK_PROVIDER)){
                locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER, locationListener,
                    Looper.getMainLooper()
                )
            }

            if (providers.contains(LocationManager.GPS_PROVIDER)){
                locationManager.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER, locationListener,
                    Looper.getMainLooper()
                )
            }

        } catch (ex: SecurityException) {

        }

    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
                _currentLocation.value = LocationData(location.latitude, location.longitude)
        }
    }
}

