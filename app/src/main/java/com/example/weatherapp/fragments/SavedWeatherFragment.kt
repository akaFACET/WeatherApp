package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherapp.R
import com.example.weatherapp.SwipeToDeleteCallback
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.adapters.OnItemClickListener
import com.example.weatherapp.adapters.SavedWeatherAdapter
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.viewModels.SavedWeatherViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.saved_weather_fragment.*
import kotlinx.android.synthetic.main.saved_weather_fragment.view.*
import kotlinx.android.synthetic.main.saved_weather_fragment.view.saved_rv
import java.util.*

class SavedWeatherFragment : Fragment() {


    private lateinit var viewModel: SavedWeatherViewModel
    private lateinit var adapterRv: SavedWeatherAdapter
    private lateinit var weather: List<WeatherData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSavedWeatherFragment")
        val fragmentLayout = inflater.inflate(R.layout.saved_weather_fragment, container, false)

        val navController = NavHostFragment.findNavController(this)

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
            context!!,
            object : OnItemClickListener {
                override fun onItemClick(weatherData: WeatherData) {
                    Toast.makeText(context, "Hi!", Toast.LENGTH_SHORT).show()
                }
            }
        )
        saved_rv.adapter = adapterRv

        WeatherRepository.db.getData().observe(viewLifecycleOwner, Observer {
            weather = it
            adapterRv.values = it
            adapterRv.notifyDataSetChanged()
        })

        val swipeHelper = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteData(weather.get(viewHolder.adapterPosition))
                adapterRv.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(saved_rv)

        swipe_to_refresh_saved.setOnRefreshListener {
            viewModel.update()
        }
        viewModel.isUpdate.observe(viewLifecycleOwner, Observer {
            if(!it){
                swipe_to_refresh_saved.isRefreshing = false
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSavedWeatherFragment")
    }

}
