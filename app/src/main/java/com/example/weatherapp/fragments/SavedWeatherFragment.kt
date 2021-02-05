package com.example.weatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.Utils.SwipeToDeleteCallback

import com.example.weatherapp.adapters.OnSavedItemClickListener
import com.example.weatherapp.adapters.SavedWeatherAdapter
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.databinding.SavedWeatherFragmentBinding
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import com.example.weatherapp.viewModels.SavedWeatherViewModelFactory
import kotlinx.android.synthetic.main.saved_weather_fragment.*
import kotlinx.android.synthetic.main.saved_weather_fragment.view.*
import javax.inject.Inject

class SavedWeatherFragment : Fragment() {

    private lateinit var binding: SavedWeatherFragmentBinding
    private lateinit var savedWeatherAdapter: SavedWeatherAdapter
    private lateinit var weather: List<WeatherData>
    private lateinit var navController: NavController

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SavedWeatherViewModelFactory()
        ).get(SavedWeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        App.get(requireContext()).applicationComponent.inject(this)

        navController = NavHostFragment.findNavController(this)
        createAdapter()

        binding = SavedWeatherFragmentBinding.inflate(inflater, container, false)

        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            root.saved_rv.adapter = savedWeatherAdapter

            floatingActionButton.setOnClickListener {
                navController.navigate(R.id.searchFragment)
            }
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWeatherFromDB()
        createSwipeToDeleteHelper()
    }

    private fun createAdapter() {
        savedWeatherAdapter = SavedWeatherAdapter(
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
    }

    private fun createSwipeToDeleteHelper() {
        val swipeHelper = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteData(weather.get(viewHolder.absoluteAdapterPosition))
                savedWeatherAdapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(saved_rv)
    }

    //TODO перенести это безобразие в мьюмодель и обсервить от туда
    @Inject
    lateinit var weatherRepository: WeatherRepository


    private fun observeWeatherFromDB() {
//        WeatherRepository.db.getAllWeatherData().observe(viewLifecycleOwner, Observer {
//            val weatherData = Mapper.mapWeatherDataEntityToWeatherData(it)
//            weather = weatherData
//            savedWeatherAdapter.values = weatherData
//            savedWeatherAdapter.notifyDataSetChanged()
//        })
        weatherRepository.db.getAllWeatherData().observe(viewLifecycleOwner, Observer {
            val weatherData = Mapper.mapWeatherDataEntityToWeatherData(it)
            weather = weatherData
            savedWeatherAdapter.values = weatherData
            savedWeatherAdapter.notifyDataSetChanged()
        })
    }

}
