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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.weatherapp.App
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.R
import com.example.weatherapp.adapters.*
import com.example.weatherapp.data.WeatherPerHour
import com.example.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.charts_layout.view.*
import kotlinx.android.synthetic.main.current_weather_bottom.view.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import javax.inject.Inject

class CurrentWeatherFragment : Fragment() {

    @Inject
    lateinit var weatherViewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CurrentWeatherViewModel

    private lateinit var binding: CurrentWeatherFragmentBinding
    private lateinit var daysScrollAdapter: DaysScrollAdapter
    private lateinit var chartViewPagerAdapter: ChartViewPagerAdapter
    private lateinit var navController: NavController
    private lateinit var refreshItem: View
    private lateinit var animation: Animation

    private val PERMISSION_ID = 42

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        App.get(requireActivity().application).applicationComponent.inject(this)
        viewModel = ViewModelProvider(this, weatherViewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)

        createAdapters()

        setHasOptionsMenu(true)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            root.charts_vp.adapter = chartViewPagerAdapter
            root.content_bottom_rv.adapter = daysScrollAdapter
            TabLayoutMediator(root.tabLayout, root.charts_vp) { tab, position ->
                tab.text = getTabName(position)
            }.attach()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDataObservers()
        createExceptionObservers()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

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

    private fun createAdapters() {
        chartViewPagerAdapter =
            ChartViewPagerAdapter(requireContext(), emptyList(), object : OnChartItemClickListener {
                override fun onChartItemClick(weatherPerHour: WeatherPerHour) {
                    viewModel.updateWeatherPerHour(weatherPerHour)
                }
            })

        daysScrollAdapter = DaysScrollAdapter(
            emptyList(),
            object : OnDaysScrollItemClickListener {
                override fun onItemClick(weatherPerHour: List<WeatherPerHour>) {
                    chartViewPagerAdapter.data = weatherPerHour
                    chartViewPagerAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    private fun createDataObservers() {
        viewModel.listWeatherPerDay.observe(viewLifecycleOwner, Observer { listWeatherPerDay ->
            if (listWeatherPerDay != null) {
                chartViewPagerAdapter.data = listWeatherPerDay[0].weatherPerHour
                chartViewPagerAdapter.notifyDataSetChanged()
                daysScrollAdapter.values = listWeatherPerDay
                daysScrollAdapter.notifyDataSetChanged()
            }
        })

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            if (weather != null) {
                requireActivity().toolbar.title = weather.name
            }
        })
    }

    private fun createExceptionObservers() {
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
    }

    private fun getTabName(position: Int): String{
        return when (position) {
            0 -> {
                getString(R.string.temperature)
            }
            1 -> {
                getString(R.string.precipitation)
            }
            2 -> {
                getString(R.string.wind)
            }
            3 -> {
                getString(R.string.cloudiness)
            }
            else -> ""
        }
    }

}
