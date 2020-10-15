package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*

import com.example.weatherapp.adapters.OnSavedItemClickListener
import com.example.weatherapp.adapters.OnSearchItemClickListener
import com.example.weatherapp.adapters.SavedWeatherAdapter
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import kotlinx.android.synthetic.main.saved_weather_fragment.*
import kotlinx.android.synthetic.main.saved_weather_fragment.view.*

class SavedWeatherFragment : Fragment() {


    private lateinit var viewModel: SavedWeatherViewModel
    private lateinit var adapterRv: SavedWeatherAdapter
    private lateinit var weather: List<WeatherData>
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(R.layout.saved_weather_fragment, container, false)

        navController = NavHostFragment.findNavController(this)

        fragmentLayout.floating_action_button.setOnClickListener{
            navController.navigate(R.id.searchFragment)
        }
        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SavedWeatherViewModel::class.java)
        adapterRv = SavedWeatherAdapter(
            emptyList(),
            requireContext(),
            object : OnSavedItemClickListener {
                override fun onSavedItemClick(cityId: Int) {
                    val action = SavedWeatherFragmentDirections.weatherDetailsFragment()
                    action.cityId = cityId
                    navController.navigate(action)
                }
            }
        )
        saved_rv.adapter = adapterRv


        WeatherRepository.db.getAllWeatherData().observe(viewLifecycleOwner, Observer {
            val weatherData = Mapper.mapWeatherDataEntityToWeatherData(it)
            weather = weatherData
            adapterRv.values = weatherData
            adapterRv.notifyDataSetChanged()
        })


        val swipeHelper = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteData(weather.get(viewHolder.adapterPosition))
                adapterRv.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(saved_rv)

    }

}
