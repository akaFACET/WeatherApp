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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.function.Consumer

class Location(
    private val context: Context,
    private val mFusedLocationClient: FusedLocationProviderClient,
    private val locationManager: LocationManager) {
    private var _currentLocation = MutableLiveData<LocationData>()

//    private var mFusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(application.applicationContext)

//    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val gmsStatus = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(context.applicationContext)

//    lateinit var locationManager: LocationManager
    var currentLocation: LiveData<LocationData> = _currentLocation

    fun getLastLocation() {
        if (gmsStatus == ConnectionResult.SUCCESS) {
//            mFusedLocationClient =
//                LocationServices.getFusedLocationProviderClient(application.applicationContext)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
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
        } else {
            getLocationFromLocationManager()
        }
    }


    private fun getLocationFromLocationManager() {
//        locationManager = App.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            val providers = locationManager.getAllProviders()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                val locationCallback = Consumer<Location>{ location ->
                    _currentLocation.value = LocationData(location.latitude, location.longitude)
                }
                if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.getCurrentLocation(
                        LocationManager.NETWORK_PROVIDER,
                        null,
                        context.mainExecutor,
                        locationCallback
                    )
                }
                if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    locationManager.getCurrentLocation(
                        LocationManager.GPS_PROVIDER,
                        null,
                        context.mainExecutor,
                        locationCallback
                    )
                }
            } else {
                if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER, locationListener,
                        Looper.getMainLooper()
                    )
                }
                if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestSingleUpdate(
                        LocationManager.GPS_PROVIDER, locationListener,
                        Looper.getMainLooper()
                    )
                }
            }

        } catch (ex: SecurityException) {

        }
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
                _currentLocation.value = LocationData(location.latitude, location.longitude)
        }
        override fun onProviderEnabled(provider: String) {

        }
        override fun onProviderDisabled(provider: String) {

        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
    }


}

