package com.example.weatherapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Location(private val application: Application) {


    lateinit var locationManager: LocationManager
    var currentLocation = MutableLiveData<LocationData>()
    private var mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application.applicationContext)
    private val GmsStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(application.applicationContext)


    fun getLastLocation() {
        Log.e("errorGMS", "GMS = ${GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(application.applicationContext)}")
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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                var location: Location? = task.result
                if (location == null) {
                    Log.e("errorGPS", "requestNewLocationData()")
                    getLocationFromLocationManager()
                } else {
                    Log.e("errorGPS", "class Location  = $location.latitude")
                    currentLocation.value = LocationData(location.latitude, location.longitude)
                }
            }
        }else{
            getLocationFromLocationManager()
        }
    }




    private fun getLocationFromLocationManager() {
        locationManager = App.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {

            Log.e("errorGPS", "requestSingleUpdate")
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
            Log.e("errorGPS", "SecurityException = $ex")
        }

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Log.e("errorGPS", "onLocationChanged has null")
            if (location != null) {
                Log.e("errorGPS", "location = ${location.latitude},${location.longitude} ")
               // locTest = LocationData(location.latitude, location.longitude)
                currentLocation.value = LocationData(location.latitude, location.longitude)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.e("errorGPS", "onStatusChanged")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.e("errorGPS", "onProviderEnabled")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.e("errorGPS", "onProviderDisabled")
        }

    }

}

