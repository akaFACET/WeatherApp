package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.adapters.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.charts_layout.*
import kotlinx.android.synthetic.main.content_bottom.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.weather_details_fragment.*

class WeatherDetailsFragment : Fragment() {

    private lateinit var viewModel: WeatherDetailsViewModel
    private lateinit var adapterRv: DaysScrollAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var citiId:Int = 0
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
        viewModel = ViewModelProviders.of(this, WeatherDetailsViewModelFactory(requireActivity().application, citiId)).get(
            WeatherDetailsViewModel::class.java)
        viewModel.getWeatherFromDb()


        viewPagerAdapter = ViewPagerAdapter(requireContext(), emptyList(), object :
            OnChartItemClickListener {
            override fun onChartItemClick(weatherPerHour: WeatherPerHour) {
                setBottomData(weatherPerHour)
            }
        })

        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = viewPagerAdapter

        adapterRv = DaysScrollAdapter(
            emptyList(),
            requireContext(),
            object : OnDaysScrollItemClickListener {
                override fun onItemClick(weatherPerHour: List<WeatherPerHour>) {
                    viewPagerAdapter.data = weatherPerHour
                    viewPagerAdapter.notifyDataSetChanged()
                }

            }
        )
        content_bottom_rv.adapter = adapterRv


        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Температура"
                    }
                    1 -> {
                        tab.text = "Осадки"
                    }
                    2 -> {
                        tab.text = "Ветер"
                    }
                    3 -> {
                        tab.text = "Облачность"
                    }
                }
            }).attach()

        viewModel.listWeatherPerDay.observe(viewLifecycleOwner, Observer { listWeatherPerDay ->
            if (listWeatherPerDay!=null){
                setBottomData(listWeatherPerDay[0].weatherPerHour[0])
                viewPagerAdapter.data = listWeatherPerDay[0].weatherPerHour
                viewPagerAdapter.notifyDataSetChanged()
                adapterRv.values = listWeatherPerDay
                adapterRv.selectedPosition = -1
                adapterRv.notifyDataSetChanged()
            }
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            when(exception){
                Exceptions.noInternet -> {
                    Snackbar.make(current_weather_fragment,
                        "No Internet Connection",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.others -> {
                    Snackbar.make(current_weather_fragment,
                        "Error",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

            }
        })
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {weather ->
            if (weather != null){
                title_tv.text = weather.name
                requireActivity().toolbar.title = weather.name // its work!
                image_iv.setImageResource(
                    Selector.iconPathSelector(
                        weather.subWeather[0].weatherId,
                        weather.subWeather[0].weatherIcon)
                )
                dateTime_tv.text = weather.updateDt
                weatherDesc_tv.text = weather.subWeather[0].weatherDescription
                temp_tv.text = weather.subWeather[0].mainTemp.toString()
                humidity_tv.text = weather.subWeather[0].mainHumidity.toString() +" %"
                wind_tv.text = weather.subWeather[0].windSpeed.toString() + " м/с"
                feelsTemp_tv.text = weather.subWeather[0].mainFeels_like.toString()
                pressure_tv.text = weather.subWeather[0].mainPressure.toString() + " мм"
            }
        })


    }

    private fun setBottomData(weatherPerHour: WeatherPerHour){
        imageBottom_iv.setImageResource(
            Selector.iconPathSelector(
                weatherPerHour.weatherId,
                weatherPerHour.weatherIcon)
        )
        weatherDescBottom_tv.text = weatherPerHour.weatherDescription.capitalize()
        tempBottom_tv.text = weatherPerHour.mainTemp.toString() + "°C"
        precipitationBottom_tv.text = weatherPerHour.precipitation.toString() + " мм"
        pressureBottom_tv.text = weatherPerHour.mainPressure.toString() + " мм"
        humidityBottom_tv.text = weatherPerHour.mainHumidity.toString() + " %"
        windBottom_tv.text = weatherPerHour.windSpeed.toString() + " м/с"
        cloudsBottom_tv.text = weatherPerHour.cloudsAll.toString() + " %"
        dateTimeBottom_tv.text = Util.getDataFromUnixTime(weatherPerHour.dt)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_item, menu)


        animation= RotateAnimation(
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
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {isLoading->
            if (isLoading)
                refreshItem.startAnimation(animation)
            else
                refreshItem.clearAnimation()
        })


        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onDestroyOptionsMenu() {
        Log.e("RefreshError", "Details OptionsMenu has destroy")
        refreshItem.clearAnimation()
        super.onDestroyOptionsMenu()
    }
    override fun onDestroyView() {
        Log.e("RefreshError", "DetailsWeather has destroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.e("RefreshError", "DetailsWeather has destroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.e("RefreshError", "DetailsWeather has detach")
        super.onDetach()
    }
}