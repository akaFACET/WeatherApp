package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.*
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.adapters.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.charts_layout.*
import kotlinx.android.synthetic.main.content_bottom.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.layout_header.*

class WeatherDetailsFragment : Fragment() {

    private lateinit var viewModel: WeatherDetailsViewModel
    private lateinit var daysScrollAdapter: DaysScrollAdapter
    private lateinit var chartViewPagerAdapter: ChartViewPagerAdapter
    private var citiId: Int = 0
    private lateinit var navController: NavController
    private lateinit var refreshItem: View
    private lateinit var animation: Animation

    private fun getLastLocation() {
        viewModel.getWeatherFromInternet()
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
        citiId = WeatherDetailsFragmentArgs.fromBundle(requireArguments()).cityId
        viewModel = ViewModelProviders.of(
            this,
            WeatherDetailsViewModelFactory(requireActivity().application, citiId)
        ).get(
            WeatherDetailsViewModel::class.java
        )
        viewModel.getWeatherFromDb()

        chartViewPagerAdapter = ChartViewPagerAdapter(requireContext(), emptyList(), object :
            OnChartItemClickListener {
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

        createTabLayoutMediator()

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
        weatherDesc_tv.text = weatherPerHour.weatherDescription.capitalize()
        temp_tv.text = weatherPerHour.mainTemp.toString()
        tempDesc_tv.text = Util.getTempUnits(weatherPerHour.units)
        pressure_tv.text =
            weatherPerHour.mainPressure.toString() + " " + getString(R.string.millimeters)
        humidity_tv.text =
            weatherPerHour.mainHumidity.toString() + " " + getString(R.string.percentage)
        wind_tv.text =
            weatherPerHour.windSpeed.toString() + " " + getString(R.string.metersPerSeconds)
        feelsTemp_tv.text =
            weatherPerHour.mainFeels_like.toString() + Util.getTempUnits(weatherPerHour.units)
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