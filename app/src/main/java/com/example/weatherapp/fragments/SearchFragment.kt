package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.weatherapp.R
import com.example.weatherapp.adapters.OnItemClickListener
import com.example.weatherapp.adapters.SearchWeatherAdapter
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapterRv: SearchWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSearchFragment")
        val fragmentLayout = inflater.inflate(R.layout.search_fragment, container, false)
        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        adapterRv = SearchWeatherAdapter(
            emptyList(),
            context!!,
            object : OnItemClickListener{
                override fun onItemClick(weatherData: WeatherData) {
                    viewModel.saveData(weatherData)
                    findNavController().navigate(R.id.action_searchFragment_to_savedWeatherFragment)
                }
            }
        )

        search_rv.adapter = adapterRv


        viewModel.searchedWeather.observe(viewLifecycleOwner, Observer {
            adapterRv.values = it
            adapterRv.notifyDataSetChanged()
        })


        search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchWeatherByCityName(query)
                adapterRv.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSearchFragment")
    }
}
