package com.example.weatherapp.fragments

import android.Manifest
import android.content.Context
import android.content.Context.*
import android.provider.Settings
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.CurrentWeatherViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.viewModels.SettingsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

//    private lateinit var viewModel: SettingsViewModel

    val PERMISSION_ID = 42

    private fun checkPermissions(): Boolean {
        if (checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
//                viewModel.getLocation()
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


//    private fun getLastLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
//                    var location: Location? = task.result
//                    if (location == null) {
//                        Log.e("err", "requestNewLocationData()")
//                        requestNewLocationData()
//                    } else {
//                        text_test_lon.text = location.latitude.toString()
//                        text_test_lat.text = location.longitude.toString()
//                    }
//                }
//            } else {
//                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }

//    private fun requestNewLocationData(){
//        locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
//        try {
//            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,
//                Looper.myLooper() )
//        }catch (ex: SecurityException){
//            Log.e("err", "SecurityException = $ex")
//        }
//
//    }

//    private val locationListener : LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location?) {
//            Log.e("err" , "onLocationChanged has null")
//            if (location !=null){
//                text_test_lat.text = location.latitude.toString()
//                text_test_lon.text = location.longitude.toString()
//            }
//        }
//
//        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onProviderEnabled(provider: String?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onProviderDisabled(provider: String?) {
//        }
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSettingsFragment")
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this,CurrentWeatherViewModelFactory(context!!)).get(SettingsViewModel::class.java)

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

//        button_test.setOnClickListener {
//            getLastLocation()
//        }

//        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
//
//            Log.e("err", "it = $it")
//            if (it!=null){
//                text_test_lat.text = it.latitude.toString()
//                text_test_lon.text = it.longitude.toString()
//            }
//        })

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSettingsFragment")
    }

}

