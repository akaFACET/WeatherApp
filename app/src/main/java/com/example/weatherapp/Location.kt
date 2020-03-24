package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.settings_fragment.*

class Location(context: Context) {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    val context = context
    var currentLocation = MutableLiveData<LocationData>()


    fun getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            var location: Location? = task.result
            if (location == null) {
                Log.e("err", "requestNewLocationData()")
                requestNewLocationData()
            } else {
                Log.e("err", "class Location  = $location.latitude")
                currentLocation.value = LocationData(location.latitude, location.longitude)
            }
        }

    }


    private fun requestNewLocationData() {
        locationManager = App.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER, locationListener,
                Looper.myLooper()
            )
        } catch (ex: SecurityException) {
            Log.e("err", "SecurityException = $ex")
        }

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Log.e("err", "onLocationChanged has null")
            if (location != null) {
                currentLocation.value = LocationData(location.latitude, location.longitude)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }

}