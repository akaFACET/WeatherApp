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
import com.example.weatherapp.utils.SwipeToDeleteCallback

import com.example.weatherapp.adapters.OnSavedItemClickListener
import com.example.weatherapp.adapters.SavedWeatherAdapter
import com.example.weatherapp.databinding.SavedWeatherFragmentBinding
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import kotlinx.android.synthetic.main.saved_weather_fragment.*
import kotlinx.android.synthetic.main.saved_weather_fragment.view.*
import javax.inject.Inject

class SavedWeatherFragment : Fragment() {

    @Inject
    lateinit var weatherViewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SavedWeatherViewModel

    private lateinit var binding: SavedWeatherFragmentBinding
    private lateinit var savedWeatherAdapter: SavedWeatherAdapter
    private lateinit var weather: List<WeatherData>
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        App.get(requireContext()).applicationComponent.inject(this)
        viewModel = ViewModelProvider(this, weatherViewModelFactory)
            .get(SavedWeatherViewModel::class.java)

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
        observeWeatherData()
        createSwipeToDeleteHelper()
    }

    private fun createAdapter() {
        savedWeatherAdapter = SavedWeatherAdapter(
            emptyList(),
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
                viewModel.deleteData(weather[viewHolder.absoluteAdapterPosition])
                savedWeatherAdapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(saved_rv)
    }

    private fun observeWeatherData() {
        viewModel.weatherData.observe(viewLifecycleOwner, Observer {weatherData->
            weather = weatherData
            savedWeatherAdapter.values = weatherData
            savedWeatherAdapter.notifyDataSetChanged()
        })
    }

}
