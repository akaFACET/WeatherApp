package com.example.weatherapp.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.App
import com.example.weatherapp.CurrentWeatherViewModelFactory

import com.example.weatherapp.R
import com.example.weatherapp.Selector
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.current_weather_fragment.view.*
import kotlinx.android.synthetic.main.current_weather_fragment.view.test_btn

class CurrentWeatherFragment : Fragment() {

    private lateinit var viewModel: CurrentWeatherViewModel

    val PERMISSION_ID = 42

    private fun checkPermissions(): Boolean {
        if (PermissionChecker.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED){
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
                Log.e("err", "Permissons Garanted")
                getLastLocation()
            }else{
                Log.e("err", "Permissons not Garanted")
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
                viewModel.getLocation()
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateCurrentFragment")
        val fragmentLayout = inflater.inflate(R.layout.current_weather_fragment, container, false)

        val navController = NavHostFragment.findNavController(this)

        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, CurrentWeatherViewModelFactory(context!!)).get(CurrentWeatherViewModel::class.java)
        test_btn.setOnClickListener {
            getLastLocation()
        }
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {weather ->

            if (weather != null){
                title_tv.text = weather.name

                    image_iv.setImageResource(
                    Selector.iconPathSelector(weather.weatherId,weather.weatherIcon)
                )
                dateTime_tv.text = weather.currentDate
                weatherDesc_tv.text = weather.weatherDescription
                temp_tv.text = weather.mainTemp.toString()
                humidity_tv.text = weather.mainHumidity.toString() +" %"
                wind_tv.text = weather.windSpeed.toString() + " м/с"
                feelsTemp_tv.text = weather.mainFeels_like.toString()
                pressure_tv.text = weather.mainPressure.toString() + " мм"
            }

        })



    }



    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachCurrentFragment")
    }

}
