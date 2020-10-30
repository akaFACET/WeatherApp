package com.example.weatherapp.fragments

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.CurrentWeatherViewModelFactory
import com.example.weatherapp.Exceptions
import com.example.weatherapp.R
import com.example.weatherapp.Selector
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.adapters.*
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.charts_layout.*
import kotlinx.android.synthetic.main.content_bottom.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.layout_header.*


class CurrentWeatherFragment : Fragment() {

    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var daysScrollAdapter: DaysScrollAdapter
    private lateinit var chartViewPagerAdapter: ChartViewPagerAdapter
    private val PERMISSION_ID = 42
    private lateinit var navController: NavController
    private lateinit var refreshItem: View
    private lateinit var animation: Animation

    private fun checkPermissions(): Boolean {
        if (PermissionChecker.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                viewModel.exception.value = Exceptions.noGPS
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                viewModel.getLocation()
            } else {
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
        val fragmentLayout = inflater.inflate(R.layout.current_weather_fragment, container, false)
        navController = NavHostFragment.findNavController(this)
        setHasOptionsMenu(true)
        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            CurrentWeatherViewModelFactory(requireActivity().application)
        ).get(CurrentWeatherViewModel::class.java)

        chartViewPagerAdapter =
            ChartViewPagerAdapter(requireContext(), emptyList(), object : OnChartItemClickListener {
                override fun onChartItemClick(weatherPerHour: WeatherPerHour) {
                    setData(weatherPerHour)
                }
            })

        charts_vp.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 1
            adapter = chartViewPagerAdapter
        }
        daysScrollAdapter = DaysScrollAdapter(
            emptyList(),
            requireContext(),
            object : OnDaysScrollItemClickListener {
                override fun onItemClick(weatherPerHour: List<WeatherPerHour>) {
                    chartViewPagerAdapter.data = weatherPerHour
                    chartViewPagerAdapter.notifyDataSetChanged()
                }
            }
        )
        content_bottom_rv.adapter = daysScrollAdapter

        createTabLayoutMediator()

        viewModel.listWeatherPerDay.observe(viewLifecycleOwner, Observer { listWeatherPerDay ->
            if (listWeatherPerDay != null) {
                setData(listWeatherPerDay[0].weatherPerHour[0])
                chartViewPagerAdapter.data = listWeatherPerDay[0].weatherPerHour
                chartViewPagerAdapter.notifyDataSetChanged()
                daysScrollAdapter.values = listWeatherPerDay
                daysScrollAdapter.selectedPosition = -1
                daysScrollAdapter.notifyDataSetChanged()
            }
        })


        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            when (exception) {
                Exceptions.noInternet -> {
                    Snackbar.make(
                        current_weather_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.others -> {
                    Snackbar.make(
                        current_weather_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.noGPS -> {
                    Snackbar.make(
                        current_weather_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.noCity -> {
                    Snackbar.make(
                        current_weather_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            if (weather != null) {
                requireActivity().toolbar.title = weather.name
            }
        })
    }

    private fun setData(weatherPerHour: WeatherPerHour) {
        image_iv.setImageResource(
            Selector.iconPathSelector(
                weatherPerHour.weatherId,
                weatherPerHour.weatherIcon
            )
        )
        feelsTemp_tv.text =
            weatherPerHour.mainFeels_like.toString() + Util.getTempUnits(weatherPerHour.units)
        weatherDesc_tv.text = weatherPerHour.weatherDescription.capitalize()
        temp_tv.text = weatherPerHour.mainTemp.toString()
        tempDesc_tv.text = Util.getTempUnits(weatherPerHour.units)
        pressure_tv.text =
            weatherPerHour.mainPressure.toString() + " " + getString(R.string.millimeters)
        humidity_tv.text =
            weatherPerHour.mainHumidity.toString() + " " + getString(R.string.percentage)
        wind_tv.text =
            weatherPerHour.windSpeed.toString() + " " + getString(R.string.metersPerSeconds)
        dateTime_tv.text = Util.getDateFromUnixTime(weatherPerHour.dt)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_item, menu)

        animation = RotateAnimation(
            0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            repeatCount = -1
            duration = 1500
        }

        refreshItem = menu.findItem(R.id.action_refresh).actionView

        refreshItem.setOnClickListener {
            getLastLocation()
        }
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading)
                refreshItem.startAnimation(animation)
            else
                refreshItem.clearAnimation()
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyOptionsMenu() {
        refreshItem.clearAnimation()
        super.onDestroyOptionsMenu()
    }

    private fun createTabLayoutMediator() {
        TabLayoutMediator(tabLayout, charts_vp,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.temperature)
                    }
                    1 -> {
                        tab.text = getString(R.string.precipitation)
                    }
                    2 -> {
                        tab.text = getString(R.string.wind)
                    }
                    3 -> {
                        tab.text = getString(R.string.cloudiness)
                    }
                }
            }).attach()
    }

}
